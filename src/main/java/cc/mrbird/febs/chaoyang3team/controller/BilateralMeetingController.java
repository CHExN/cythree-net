package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.BilateralMeeting;
import cc.mrbird.febs.chaoyang3team.service.BilateralMeetingFileService;
import cc.mrbird.febs.chaoyang3team.service.BilateralMeetingService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("bilateralMeeting")
public class BilateralMeetingController extends BaseController {

    private String message;

    @Autowired
    private BilateralMeetingService bilateralMeetingService;
    @Autowired
    private BilateralMeetingFileService bilateralMeetingFileService;

    @Log("修改上会议题意见")
    @PutMapping("updateOpinion")
    @RequiresPermissions("bilateralMeeting:update")
    public void updateBilateralMeetingOpinion(@Valid BilateralMeeting bilateralMeeting) throws FebsException {
        try {
            this.bilateralMeetingService.updateBilateralMeetingOpinion(bilateralMeeting);
        } catch (Exception e) {
            message = "修改上会议题失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("bilateralMeetingFiles")
    public FebsResponse findFilesByBilateralMeetingId(String bilateralMeetingId) {
        return this.bilateralMeetingFileService.findFilesByBilateralMeetingId(bilateralMeetingId);
    }

    @Log("上传上会议题附件")
    @PostMapping("uploadBilateralMeetingFile")
    public FebsResponse uploadBilateralMeetingFile(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.bilateralMeetingService.uploadBilateralMeetingFile(file, id);
        } catch (Exception e) {
            message = "上传上会议题附件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping
    @RequiresPermissions("bilateralMeeting:view")
    public Map<String, Object> bilateralMeetingList(QueryRequest request, BilateralMeeting bilateralMeeting, ServletRequest servletRequest) {
        return getDataTable(this.bilateralMeetingService.findBilateralMeetingDetail(request, bilateralMeeting, servletRequest));
    }

    @Log("新增上会议题")
    @PostMapping
    @RequiresPermissions("bilateralMeeting:add")
    public void addBilateralMeeting(@Valid BilateralMeeting bilateralMeeting, ServletRequest request) throws FebsException {
        try {
            this.bilateralMeetingService.createBilateralMeeting(bilateralMeeting, request);
        } catch (Exception e) {
            message = "新增上会议题失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改上会议题")
    @PutMapping
    @RequiresPermissions("bilateralMeeting:update")
    public void updateBilateralMeeting(@Valid BilateralMeeting bilateralMeeting) throws FebsException {
        try {
            this.bilateralMeetingService.updateBilateralMeeting(bilateralMeeting);
        } catch (Exception e) {
            message = "修改上会议题失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除上会议题")
    @DeleteMapping("/{bilateralMeetingIds}")
    @RequiresPermissions("bilateralMeeting:delete")
    public void deleteBilateralMeetings(@NotBlank(message = "{required}") @PathVariable String bilateralMeetingIds) throws FebsException {
        try {
            String[] ids = bilateralMeetingIds.split(StringPool.COMMA);
            this.bilateralMeetingService.deleteBilateralMeetings(ids);
        } catch (Exception e) {
            message = "删除上会议题失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除上会议题文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    public void deleteWcFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.bilateralMeetingService.deleteBilateralMeetingsFile(ids);
        } catch (Exception e) {
            message = "删除上会议题文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
