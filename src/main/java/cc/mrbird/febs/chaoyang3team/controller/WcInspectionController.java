package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcInspection;
import cc.mrbird.febs.chaoyang3team.service.WcInspectionService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("wcInspection")
public class WcInspectionController extends BaseController {

    private String message;

    @Autowired
    private WcInspectionService wcInspectionService;


    @GetMapping("getWcInspectionLoadData")
    @RequiresPermissions("wcInspection:view")
    public Map<String, Object> getLoadData(ServletRequest servletRequest) {
        return this.wcInspectionService.getWcInspectionLoadData(servletRequest);
    }

    @GetMapping
    @RequiresPermissions("wcInspection:view")
    public Map<String, Object> getWcInspectionDetail(QueryRequest request, WcInspection wcInspection, int pageSize1, int pageNum1) {
        return this.wcInspectionService.findWcInspectionAndWcTemplateDetail(request, wcInspection, pageSize1, pageNum1);
    }

    @GetMapping("weChat/getWcInspectionAndWcLocation")
    @RequiresPermissions("wcInspection:view")
    public Map<String, Object> getWcInspectionAndWcLocation(
            QueryRequest request, ServletRequest servletRequest, WcInspection wcInspection, String longitude, String latitude, Integer radius, Integer length) {
        return this.wcInspectionService.getWcInspectionAndWcLocation(request, servletRequest, wcInspection, longitude, latitude, radius, length);
    }

    @Log("新增公厕巡检")
    @PostMapping
    @RequiresPermissions("wcInspection:add")
    public void addWcInspection(@Valid WcInspection wcInspection, ServletRequest servletRequest) throws FebsException {
        try {
            this.wcInspectionService.createWcInspection(wcInspection, servletRequest);
        } catch (Exception e) {
            message = "新增公厕巡检失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改公厕巡检")
    @PutMapping
    @RequiresPermissions("wcInspection:update")
    public void updateWcInspection(@Valid WcInspection wcInspection) throws FebsException {
        try {
            this.wcInspectionService.updateWcInspection(wcInspection);
        } catch (Exception e) {
            message = "修改公厕巡检失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除公厕巡检")
    @DeleteMapping("/{id}")
    @RequiresPermissions("wcInspection:delete")
    public void deleteWcInspection(@NotBlank(message = "{required}") @PathVariable String id) throws FebsException {
        try {
            String[] ids = id.split(StringPool.COMMA);
            this.wcInspectionService.deleteWcInspection(ids);
        } catch (Exception e) {
            message = "删除公厕巡检失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
