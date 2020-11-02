package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcTemplate;
import cc.mrbird.febs.chaoyang3team.service.WcTemplateService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wcTemplate")
public class WcTemplateController extends BaseController {

    private String message;

    @Autowired
    private WcTemplateService wcTemplateService;


    /*@GetMapping("getWcTemplateLoadData")
    @RequiresPermissions("wcTemplate:view")
    public Map<String, Object> getLoadData(ServletRequest servletRequest) {
        return this.wcTemplateService.getWcTemplateLoadData(servletRequest);
    }*/

    @GetMapping
    @RequiresPermissions("wcTemplate:view")
    public Map<String, Object> getWcTemplateDetail(QueryRequest request, WcTemplate wcTemplate) {
        return getDataTable(this.wcTemplateService.findWcTemplateDetail(request, wcTemplate));
    }

    /*@GetMapping("weChat/getWcTemplateAndWcLocation")
    @RequiresPermissions("wcTemplate:view")
    public Map<String, Object> getWcTemplateAndWcLocation(
            QueryRequest request, WcTemplate wcTemplate, String longitude, String latitude, Integer radius, Integer length) {
        return this.wcTemplateService.getWcTemplateAndWcLocation(request, wcTemplate, longitude, latitude, radius, length);
    }*/

    @Log("新增公厕巡检模板")
    @PostMapping
    @RequiresPermissions("wcTemplate:add")
    public void addWcTemplate(@Valid WcTemplate wcTemplate) throws FebsException {
        try {
            this.wcTemplateService.createWcTemplate(wcTemplate);
        } catch (Exception e) {
            message = "新增公厕巡检模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改公厕巡检模板")
    @PutMapping
    @RequiresPermissions("wcTemplate:update")
    public void updateWcTemplate(@Valid WcTemplate wcTemplate) throws FebsException {
        try {
            this.wcTemplateService.updateWcTemplate(wcTemplate);
        } catch (Exception e) {
            message = "修改公厕巡检模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除公厕巡检模板")
    @DeleteMapping("/{id}")
    @RequiresPermissions("wcTemplate:delete")
    public void deleteWcTemplate(@NotBlank(message = "{required}") @PathVariable String id) throws FebsException {
        try {
            String[] ids = id.split(StringPool.COMMA);
            this.wcTemplateService.deleteWcTemplate(ids);
        } catch (Exception e) {
            message = "删除公厕巡检模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
