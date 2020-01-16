package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Canteen;
import cc.mrbird.febs.chaoyang3team.service.CanteenService;
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
@RequestMapping("canteen")
public class CanteenController extends BaseController {

    private String message;

    @Autowired
    private CanteenService canteenService;

    @GetMapping
    @RequiresPermissions("canteen:view")
    public List<Canteen> CanteenList() {
        return this.canteenService.getCanteens();
    }

    @Log("新增食堂用品类别")
    @PostMapping
    @RequiresPermissions("canteen:add")
    public Long addCanteen(@Valid Canteen canteen) throws FebsException {
        try {
            return this.canteenService.createCanteen(canteen);
        } catch (Exception e) {
            message = "新增食堂用品类别失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除食堂用品类别")
    @DeleteMapping("/{canteenIds}")
    @RequiresPermissions("canteen:delete")
    public void deleteCanteens(@NotBlank(message = "{required}") @PathVariable String canteenIds) throws FebsException {
        try {
            String[] ids = canteenIds.split(StringPool.COMMA);
            this.canteenService.deleteCanteens(ids);
        } catch (Exception e) {
            message = "删除食堂用品类别失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改食堂用品类别")
    @PutMapping
    @RequiresPermissions("canteen:update")
    public void updateCanteen(@Valid Canteen canteen) throws FebsException {
        try {
            this.canteenService.updateCanteen(canteen);
        } catch (Exception e) {
            message = "修改食堂用品类别失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}