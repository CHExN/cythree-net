package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import cc.mrbird.febs.chaoyang3team.service.VacationService;
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
@RequestMapping("vacation")
public class VacationController extends BaseController {

    private String message;

    @Autowired
    private VacationService vacationService;

    @GetMapping("getType")
    public List<String> getType() {
        return this.vacationService.getType();
    }

    @GetMapping
    @RequiresPermissions("vacation:view")
    public Map<String, Object> vacationList(QueryRequest request, Vacation vacation) {
        return getDataTable(this.vacationService.findVacationDetail(request, vacation));
    }

    @GetMapping("insideAnnualLeave")
    @RequiresPermissions("vacation:view")
    public Map<String, Object> insideAnnualLeaveList(QueryRequest request, Vacation vacation) {
        return getDataTable(this.vacationService.findInsideAnnualLeave(request, vacation));
    }

    @GetMapping("insOutVacation")
    @RequiresPermissions("vacation:view")
    public Map<String, Object> insOutVacation(QueryRequest request, Vacation vacation) {
        return getDataTable(this.vacationService.findInsOutVacation(request, vacation));
    }

    @Log("新增人员休假信息")
    @PostMapping
    @RequiresPermissions("vacation:add")
    public void addVacation(@Valid Vacation vacation) throws FebsException {
        try {
            this.vacationService.createVacation(vacation);
        } catch (Exception e) {
            message = "新增人员休假信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改人员休假信息")
    @PutMapping
    @RequiresPermissions("vacation:update")
    public void updateVacation(@Valid Vacation vacation) throws FebsException {
        try {
            this.vacationService.updateVacation(vacation);
        } catch (Exception e) {
            message = "修改人员休假信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除人员休假信息")
    @DeleteMapping("/{vacationIds}")
    @RequiresPermissions("vacation:delete")
    public void deleteVacations(@NotBlank(message = "{required}") @PathVariable String vacationIds) throws FebsException {
        try {
            String[] ids = vacationIds.split(StringPool.COMMA);
            this.vacationService.deleteVacation(ids);
        } catch (Exception e) {
            message = "删除人员休假信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
