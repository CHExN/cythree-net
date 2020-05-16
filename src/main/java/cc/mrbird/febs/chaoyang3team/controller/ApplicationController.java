package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Application1;
import cc.mrbird.febs.chaoyang3team.domain.Plan;
import cc.mrbird.febs.chaoyang3team.service.ApplicationFileService;
import cc.mrbird.febs.chaoyang3team.service.ApplicationPlanService;
import cc.mrbird.febs.chaoyang3team.service.ApplicationService;
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
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("application")
public class ApplicationController extends BaseController {

    private String message;

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationPlanService applicationPlanService;
    @Autowired
    private ApplicationFileService applicationFileService;

    @GetMapping("applicationFiles")
    @RequiresPermissions("application:view")
    public FebsResponse findFilesByApplicationId(String applicationId) {
        return this.applicationFileService.findFilesByApplicationId(applicationId);
    }

    @Log("上传采购申请单图片")
    @PostMapping("uploadApplicationPhoto")
    @RequiresPermissions("application:addDeletePhoto")
    public FebsResponse uploadApplicationPhoto(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.applicationService.uploadApplicationPhoto(file, id);
        } catch (Exception e) {
            message = "上传采购申请单图片失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping
    @RequiresPermissions("application:view")
    public Map<String, Object> ApplicationList(QueryRequest request, Application1 application, ServletRequest servletRequest) {
        return getDataTable(this.applicationService.findApplicationDetail(request, application, servletRequest));
    }

    @GetMapping("applicationPlan")
    @RequiresPermissions("application:view")
    public List<Plan> findPlansByApplicationId(String applicationId, Boolean status) {
        return this.applicationPlanService.findPlansByApplicationId(applicationId, status);
    }

    @Log("新增采购申请单")
    @PostMapping
    @RequiresPermissions("application:add")
    public void addApplication(@Valid Application1 application, ServletRequest request) throws FebsException {
        try {
            this.applicationService.createApplication(application, request);
        } catch (Exception e) {
            message = "新增采购申请单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改采购申请单")
    @PutMapping
    @RequiresPermissions("application:update")
    public void updateApplication(@Valid Application1 application) throws FebsException {
        try {
            this.applicationService.updateApplication(application);
        } catch (Exception e) {
            message = "修改采购申请单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除采购申请单")
    @DeleteMapping("/{applicationIds}")
    @RequiresPermissions("application:delete")
    public void deleteApplications(@NotBlank(message = "{required}") @PathVariable String applicationIds) throws FebsException {
        try {
            String[] ids = applicationIds.split(StringPool.COMMA);
            this.applicationService.deleteApplications(ids);
        } catch (Exception e) {
            message = "删除采购申请单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除采购申请单文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    @RequiresPermissions("application:addDeletePhoto")
    public void deleteApplicationsFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.applicationService.deleteApplicationsFile(ids);
        } catch (Exception e) {
            message = "删除采购申请单文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
