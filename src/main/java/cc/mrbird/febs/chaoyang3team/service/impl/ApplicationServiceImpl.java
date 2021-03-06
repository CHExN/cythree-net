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
import java.util.*;

/**
 * @author CHExN
 */
@Slf4j
@Service("applicationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application1> implements ApplicationService {

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
    public IPage<Application1> findApplicationDetail(QueryRequest request, Application1 application, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            application.setUsername(username);
            User user = this.userManager.getUser(username);
            application.setDeptId(user.getDeptId());

            //判断用户角色是否包含“审核采购申请”（id：86）或 “保洁劳保库管”（id：87）、“维修库管”（id：92）、“行政后勤”（id：73）
            //这四个是后勤部门所拥有的角色，如果包含其中一项，则能不受“只看本部门”的查询影响
            boolean isLogistics = Arrays.asList(user.getRoleId().split(",")).contains("86");
            boolean isLbbjkg = Arrays.asList(user.getRoleId().split(",")).contains("87");
            boolean isWxclkg = Arrays.asList(user.getRoleId().split(",")).contains("92");
            boolean isXzhqbm = Arrays.asList(user.getRoleId().split(",")).contains("73");

            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);
            // 如果拥有以下任意一个或复数权限，就代表只能看到这些物品的权限，搜索也是一样，搜只会出现这些的物资类别
            List<String> typeApplicationAuthorityList = new ArrayList<>();
            if (userPermissions.contains("storeroom:view1")) {
                typeApplicationAuthorityList.add("1");
            }
            if (userPermissions.contains("storeroom:view2")) {
                typeApplicationAuthorityList.add("2");
            }
            if (userPermissions.contains("storeroom:view3")) {
                typeApplicationAuthorityList.add("3");
            }
            if (userPermissions.contains("storeroom:view4")) {
                typeApplicationAuthorityList.add("4");
            }
            if (userPermissions.contains("storeroom:view5")) {
                typeApplicationAuthorityList.add("5");
            }
            if (userPermissions.contains("storeroom:view6")) {
                typeApplicationAuthorityList.add("6");
            }
            if (userPermissions.contains("storeroom:view7")) {
                typeApplicationAuthorityList.add("7");
            }
            if (userPermissions.contains("storeroom:view8")) {
                typeApplicationAuthorityList.add("8");
            }
            if (userPermissions.contains("storeroom:view9")) {
                typeApplicationAuthorityList.add("9");
            }
            application.setTypeApplicationAuthority(String.join(",", typeApplicationAuthorityList));

            boolean is3 = false;
            if (application.getTypeApplication() != null) {
                is3 = application.getTypeApplication().equals("3");
            }
            Page<Application1> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findApplicationDetail(page, application, is3, isLogistics || isLbbjkg || isWxclkg || isXzhqbm);
        } catch (Exception e) {
            log.error("查询采购申请单信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createApplication(Application1 application, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        application.setUsername(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
        application.setCreateDate(LocalDate.now());
        if (application.getTypeApplication().equals("3")) { // 如果为办公室用品
            application.setReview("logistics"); // 后勤 审核
        } else if (application.getIsFixedAssets() != null && application.getIsFixedAssets().equals("1") && application.getTypeApplication().equals("5")) { // 如果为固定资产,并且标识为固定资产申请页面提交的
            application.setReview("xzhqbm,finance,vice,captain"); // 行政后勤库管，财务，副队长，队长 依次审核
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
    public void updateApplication(Application1 application) {
        if (application.getPlanList() != null) {
            // 将JSON字符串转换为List<Plan>格式
            JSONArray jsonArray = JSONArray.fromObject(application.getPlanList());
            List<Plan> planList = (List<Plan>) JSONArray.toCollection(jsonArray, Plan.class);

            // 取出传过来的plan，status为1的id，这里的plan都是status为1或者新添加的
            List<String> planIdList = new ArrayList<>();
            int count = 0;
            for (Plan e : planList) {
                if (e.getStatus().equals("2")) count++;
                if (e.getId() != null) planIdList.add(e.getId().toString());
            }
            if (count == planList.size()) application.setProcess(2);

            // 删除全部plan
            // if (!planIdList.isEmpty()) this.planService.deletePlans(planIdList.toArray(new String[0]));
            if (!planIdList.isEmpty()) {
                this.planService.deletePlansByApplicationIds(new String[]{application.getId().toString()});
            }
            // 设置插入id为null，表示插入的为新数据
            planList.forEach(plan -> plan.setId(null));
            this.planService.batchInsertPlan(planList);

            // 与applicationId绑定
            List<ApplicationPlan> applicationPlanList = new ArrayList<>();
            for (Plan plan : planList) {
                applicationPlanList.add(new ApplicationPlan(application.getId(), plan.getId()));
            }
            this.applicationPlanService.batchInsertApplicationPlan(applicationPlanList);
        }

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
                    if (application.getIsFixedAssets() != null && application.getIsFixedAssets().equals("1")) {
                        message.append("申请日期为 ").append(application.getCreateDate()).append(" 的固定资产申请已审核成功");
                    } else {
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
                    }
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
    @Transactional
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
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }
}
