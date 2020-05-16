package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.PlanStable;
import cc.mrbird.febs.chaoyang3team.service.PlanStableService;
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
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("planStable")
public class PlanStableController extends BaseController {

    private String message;

    @Autowired
    private PlanStableService planStableService;

    @GetMapping("getList")
    public List<PlanStable> planStableList() {
        return this.planStableService.planStableList();
    }

    @GetMapping
    @RequiresPermissions("planStable:view")
    public Map<String, Object> findWcDetail(QueryRequest request, PlanStable planStable) {
        return getDataTable(this.planStableService.findPlanStable(request, planStable));
    }

    @Log("新增固定办公用品选项")
    @PostMapping
    @RequiresPermissions("planStable:add")
    public void addPlanStable(@Valid PlanStable planStable) throws FebsException {
        try {
            this.planStableService.createPlanStable(planStable);
        } catch (Exception e) {
            message = "新增固定办公用品选项失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改固定办公用品选项")
    @PutMapping
    @RequiresPermissions("planStable:update")
    public void updatePlanStable(@Valid PlanStable planStable) throws FebsException {
        try {
            this.planStableService.updatePlanStable(planStable);
        } catch (Exception e) {
            message = "修改固定办公用品选项失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除固定办公用品选项")
    @DeleteMapping("/{planStableIds}")
    @RequiresPermissions("planStable:delete")
    public void deletePlanStables(@NotBlank(message = "{required}") @PathVariable String planStableIds) throws FebsException {
        try {
            String[] ids = planStableIds.split(StringPool.COMMA);
            this.planStableService.deletePlanStable(ids);
        } catch (Exception e) {
            message = "删除固定办公用品选项失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
