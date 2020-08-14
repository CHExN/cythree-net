package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.BilateralMeetingMapper;
import cc.mrbird.febs.chaoyang3team.domain.BilateralMeeting;
import cc.mrbird.febs.chaoyang3team.domain.BilateralMeetingFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.service.BilateralMeetingFileService;
import cc.mrbird.febs.chaoyang3team.service.BilateralMeetingService;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("bilateralMeetingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BilateralMeetingServiceImpl extends ServiceImpl<BilateralMeetingMapper, BilateralMeeting> implements BilateralMeetingService {

    @Autowired
    private FileService fileService;
    @Autowired
    private BilateralMeetingFileService bilateralMeetingFileService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageService messageService;

    @Override
    public FebsResponse uploadBilateralMeetingFile(MultipartFile file, String bilateralMeetingId) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.bilateralMeetingFileService.createBilateralMeetingFile(new BilateralMeetingFile(Long.valueOf(bilateralMeetingId), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }

    @Override
    public IPage<BilateralMeeting> findBilateralMeetingDetail(QueryRequest request, BilateralMeeting bilateralMeeting, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“办公室”（id：79）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("79");
            if (!isRoleId) bilateralMeeting.setDeptId(user.getDeptId());

            Page<BilateralMeeting> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findBilateralMeeting(page, bilateralMeeting);
        } catch (Exception e) {
            log.error("上会议题信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createBilateralMeeting(BilateralMeeting bilateralMeeting, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        bilateralMeeting.setReview("office,captain");
        bilateralMeeting.setUsername(username);
        bilateralMeeting.setDeptId(user.getDeptId());
        bilateralMeeting.setDeptName(user.getDeptName());
        bilateralMeeting.setCreateTime(LocalDateTime.now());
        this.save(bilateralMeeting);
        // 插入附件与上会议题的关联
        if (bilateralMeeting.getFileId() != null) {
            String[] fileIds = bilateralMeeting.getFileId().split(",");
            for (String fileId : fileIds) {
                this.bilateralMeetingFileService.createBilateralMeetingFile(new BilateralMeetingFile(bilateralMeeting.getId(), Long.valueOf(fileId)));
            }
        }
    }

    @Override
    @Transactional
    public void updateBilateralMeeting(BilateralMeeting bilateralMeeting) {
        // 判断更新的是否是更新状态的
        if (bilateralMeeting.getBilateralMeeting() == null && bilateralMeeting.getProposedCauseSummary() == null && bilateralMeeting.getProcess() == 1) {
            String message = "您有一条上会议题审核通过";
            messageService.oneToOne(new Message(
                    null,
                    null,
                    message,
                    "bot",
                    "系统",
                    bilateralMeeting.getUsername(),
                    null)
            );
        } else {
            bilateralMeeting.setModifyTime(LocalDateTime.now());
        }
        this.baseMapper.updateById(bilateralMeeting);
    }

    @Override
    public void updateBilateralMeetingOpinion(BilateralMeeting bilateralMeeting) {
        if (bilateralMeeting.getOpinionOffice() != null) {
            bilateralMeeting.setDateOffice(LocalDateTime.now());
        } else if(bilateralMeeting.getOpinionCaptain() != null) {
            bilateralMeeting.setDateCaptain(LocalDateTime.now());
        }
        this.baseMapper.updateById(bilateralMeeting);
    }

    @Override
    @Transactional
    public void deleteBilateralMeetings(String[] bilateralMeetingIds) {
        List<String> list = Arrays.asList(bilateralMeetingIds);
        this.baseMapper.deleteBatchIds(list);
        // 根据上会议题id查找对应文件id
        List<String> fileIdList = this.bilateralMeetingFileService.findFileIdsByBilateralMeetingIds(bilateralMeetingIds);

        if (!fileIdList.isEmpty()) {
            String[] fileIds = fileIdList.toArray(new String[0]);
            this.deleteBilateralMeetingsFile(fileIds);
        }
    }

    @Override
    public void deleteBilateralMeetingsFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.bilateralMeetingFileService.deleteBilateralMeetingFilesByFileId(fileIds);
    }
}
