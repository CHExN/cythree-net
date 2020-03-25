package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WageRemark;
import cc.mrbird.febs.chaoyang3team.service.WageRemarkService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
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

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wageRemark")
public class WageRemarkController extends BaseController {

    private String message;

    @Autowired
    private WageRemarkService wageRemarkService;

    @GetMapping("getOne")
    @RequiresPermissions("wageRemark:view")
    public WageRemark getOneWageRemark(WageRemark wageRemark) {
        return this.wageRemarkService.getOneWageRemark(wageRemark);
    }

    @GetMapping
    @RequiresPermissions("wageRemark:view")
    public List<WageRemark> getWageRemark(WageRemark wageRemark) {
        return this.wageRemarkService.findWageRemarkDetail(wageRemark);
    }

    @Log("新增工资备注信息")
    @PostMapping
    @RequiresPermissions("wageRemark:add")
    public void addWageRemark(@Valid WageRemark wageRemark) throws FebsException {
        try {
            this.wageRemarkService.createWageRemark(wageRemark);
        } catch (Exception e) {
            message = "新增工资备注信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改工资备注信息")
    @PutMapping
    @RequiresPermissions("wageRemark:update")
    public void updateWageRemark(@Valid WageRemark wageRemark) throws FebsException {
        try {
            this.wageRemarkService.updateWageRemark(wageRemark);
        } catch (Exception e) {
            message = "修改工资备注信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除工资备注信息")
    @DeleteMapping("/{wageRemarkIds}")
    @RequiresPermissions("wageRemark:delete")
    public void deleteWageRemarks(@NotBlank(message = "{required}") @PathVariable String wageRemarkIds) throws FebsException {
        try {
            String[] ids = wageRemarkIds.split(StringPool.COMMA);
            this.wageRemarkService.deleteWageRemark(ids);
        } catch (Exception e) {
            message = "删除工资备注信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
