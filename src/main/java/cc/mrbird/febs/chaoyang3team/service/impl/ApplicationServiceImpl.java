package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ApplicationMapper;
import cc.mrbird.febs.chaoyang3team.domain.*;
import cc.mrbird.febs.chaoyang3team.service.*;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("applicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Autowired
    private FileService fileService;
    @Autowired
    private ApplicationFileService applicationFileService;
    @Autowired
    private PlanService planService;
    @Autowired
    private ApplicationPlanService applicationPlanService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageService messageService;

    @Override
    public IPage<Application> findApplicationDetail(QueryRequest request, Application application, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            application.setUsername(username);
            User user = this.userManager.getUser(username);
            application.setDeptId(user.getDeptId());

            //判断用户角色是否包含“行政后勤”（id：73） 如果isLogistics为true，则能看到all data
            boolean isLogistics = Arrays.asList(user.getRoleId().split(",")).contains("73");

            Page<Application> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findApplicationDetail(page, application, isLogistics);
        } catch (Exception e) {
            log.error("查询采购申请单信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createApplication(Application application, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        application.setUsername(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
        application.setCreateDate(LocalDate.now());
//        if (application.getTypeApplication().equals("5")) { // 如果为固定资产
//            application.setReview("caiwu,fuzhi,zhengzhi");
//        } else {
//            application.setReview("xzhisoft");
//        }
        // 这里注释的是真的，上面是测试用的
        if (application.getTypeApplication().equals("3")) { // 如果为办公室用品
            application.setReview("logistics"); // 后勤 审核
        } else {
            application.setReview("logistics,finance,vice,captain"); // 后勤，财务，副队长，队长 依次审核
        }
        this.save(application);
        // 将JSON字符串转换为List<Plan>格式
        JSONArray jsonArray = JSONArray.fromObject(application.getPlanList());
        List<Plan> planList = (List<Plan>) JSONArray.toCollection(jsonArray, Plan.class);
        this.planService.batchInsertPlan(planList);

        List<ApplicationPlan> applicationPlanList = new ArrayList<>();
        for (Plan plan : planList) {
            applicationPlanList.add(new ApplicationPlan(application.getId(), plan.getId()));
        }
        this.applicationPlanService.batchInsertApplicationPlan(applicationPlanList);
    }

    @Override
    @Transactional
    public void updateApplication(Application application) {
        this.baseMapper.updateById(application);
        if (application.getProcess() != null && application.getProcess() != 0) {
            StringBuilder message = new StringBuilder();
            // 判断如果是办公用品或者固定资产，这两个的申请时每月申请单号的，也就是说application.getNum() == null，所以要新建立一个
            if (application.getTypeApplication().equals("3")) {
                if (application.getProcess() == 1) {
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的办公用品申请通过了");
                    messageService.oneToOne(new Message(
                            null,
                            null,
                            message.toString(),
                            "bot",
                            "系统",
                            "logistics",
                            null)
                    );
                    message.delete(0, message.length());
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的办公用品申请已审核成功，等待入库");
                } else if (application.getProcess() == -1) {
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的办公用品申请审核失败");
                } else if (application.getProcess() == 2) {
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的办公用品申请已入库成功");
                }
            } else if (application.getTypeApplication().equals("5")) {
                if (application.getProcess() == 1) {
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的固定资产申请通过了");
                    messageService.oneToOne(new Message(
                            null,
                            null,
                            message.toString(),
                            "bot",
                            "系统",
                            "logistics",
                            null)
                    );
                    message.delete(0, message.length());
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的固定资产申请已审核成功，等待入库");
                } else if (application.getProcess() == -1) {
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的固定资产申请审核失败");
                } else if (application.getProcess() == 2) {
                    message.append("申请日期为 ").append(application.getCreateDate()).append(" 的固定资产申请已入库成功");
                }
            } else {
                if (application.getProcess() == 1) {
                    message.append("单号为 ").append(application.getNum()).append(" 的单采购申请单通过了");
                    messageService.oneToOne(new Message(
                            null,
                            null,
                            message.toString(),
                            "bot",
                            "系统",
                            "logistics",
                            null)
                    );
                    message.delete(0, message.length());
                    message.append("您单号为 ").append(application.getNum()).append(" 的采购申请单已审核成功，等待入库");
                } else if (application.getProcess() == -1) {
                    message.append("您单号为 ").append(application.getNum()).append(" 的采购申请单审核失败");
                } else if (application.getProcess() == 2) {
                    message.append("您单号为 ").append(application.getNum()).append(" 的采购申请单已入库成功");
                }
            }
            this.messageService.oneToOne(new Message(
                    null,
                    null,
                    message.toString(),
                    "bot",
                    "系统",
                    application.getUsername(),
                    null)
            );
        }
    }

    @Override
    @Transactional
    public void deleteApplications(String[] applicationIds) {
        List<String> list = Arrays.asList(applicationIds);
        this.baseMapper.deleteBatchIds(list);
        // 根据采购申请id查询计划id
        List<String> planIdList = this.applicationPlanService.findPlanIdsByApplicationIds(applicationIds);
        if (!planIdList.isEmpty()) {
            this.planService.deletePlans(planIdList.toArray(new String[0]));
        }
        // 根据采购申请id查找对应照片id
        List<String> fileIdList = this.applicationFileService.findFileIdsByApplicationIds(applicationIds);

        if (!fileIdList.isEmpty()) {
            String[] fileIds = fileIdList.toArray(new String[0]);
            this.deleteApplicationsFile(fileIds);
        }
    }

    @Override
    public void deleteApplicationsFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.applicationFileService.deleteApplicationFilesByFileId(fileIds);
    }

    @Override
    public FebsResponse uploadApplicationPhoto(MultipartFile file, String id) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.applicationFileService.createApplicationFile(new ApplicationFile(Long.valueOf(id), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(7)
        );
        return new FebsResponse().data(result);
    }
}
