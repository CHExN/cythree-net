package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.FireExtinguisher;
import cc.mrbird.febs.chaoyang3team.service.FireExtinguisherService;
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
@RequestMapping("fireExtinguisher")
public class FireExtinguisherController extends BaseController {

    private String message;

    @Autowired
    private FireExtinguisherService fireExtinguisherService;

    @GetMapping
    @RequiresPermissions("fireExtinguisher:view")
    public Map<String, Object> FireExtinguisherList(QueryRequest request, FireExtinguisher fireExtinguisher) {
        return getDataTable(this.fireExtinguisherService.findFireExtinguisherDetail(request, fireExtinguisher));
    }

    @Log("新增灭火器")
    @PostMapping
    @RequiresPermissions("fireExtinguisher:add")
    public void addFireExtinguisher(@Valid FireExtinguisher fireExtinguisher) throws FebsException {
        try {
            this.fireExtinguisherService.createFireExtinguisher(fireExtinguisher);
        } catch (Exception e) {
            message = "新增灭火器失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改灭火器")
    @PutMapping
    @RequiresPermissions("fireExtinguisher:update")
    public void updateFireExtinguisher(@Valid FireExtinguisher fireExtinguisher) throws FebsException {
        try {
            this.fireExtinguisherService.updateFireExtinguisher(fireExtinguisher);
        } catch (Exception e) {
            message = "修改灭火器失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除灭火器")
    @DeleteMapping("/{fireExtinguisherIds}")
    @RequiresPermissions("fireExtinguisher:delete")
    public void deleteFireExtinguishers(@NotBlank(message = "{required}") @PathVariable String fireExtinguisherIds) throws FebsException {
        try {
            String[] ids = fireExtinguisherIds.split(StringPool.COMMA);
            this.fireExtinguisherService.deleteFireExtinguisher(ids);
        } catch (Exception e) {
            message = "删除灭火器失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
