package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.FixedAssetsAcceptance;
import cc.mrbird.febs.chaoyang3team.service.FixedAssetsAcceptanceService;
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
@RequestMapping("fixedAssetsAcceptance")
public class FixedAssetsAcceptanceController extends BaseController {

    private String message;

    @Autowired
    private FixedAssetsAcceptanceService fixedAssetsAcceptanceService;

    @GetMapping
    @RequiresPermissions("fixedAssetsAcceptance:view")
    public Map<String, Object> fixedAssetsAcceptanceList(QueryRequest request, FixedAssetsAcceptance fixedAssetsAcceptance) {
        return getDataTable(this.fixedAssetsAcceptanceService.findFixedAssetsAcceptance(request, fixedAssetsAcceptance));
    }


    @Log("新增固定资产验收")
    @PostMapping
    @RequiresPermissions("fixedAssetsAcceptance:add")
    public void addFixedAssetsAcceptance(@Valid FixedAssetsAcceptance fixedAssetsAcceptance) throws FebsException {
        try {
            this.fixedAssetsAcceptanceService.createFixedAssetsAcceptance(fixedAssetsAcceptance);
        } catch (Exception e) {
            message = "新增固定资产验收失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改固定资产验收")
    @PutMapping
    @RequiresPermissions("fixedAssetsAcceptance:update")
    public void updateFixedAssetsAcceptance(@Valid FixedAssetsAcceptance fixedAssetsAcceptance) throws FebsException {
        try {
            this.fixedAssetsAcceptanceService.updateFixedAssetsAcceptance(fixedAssetsAcceptance);
        } catch (Exception e) {
            message = "修改固定资产验收失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除固定资产验收")
    @DeleteMapping("/{fixedAssetsAcceptanceIds}")
    @RequiresPermissions("fixedAssetsAcceptance:delete")
    public void deleteCars(@NotBlank(message = "{required}") @PathVariable String fixedAssetsAcceptanceIds) throws FebsException {
        try {
            String[] ids = fixedAssetsAcceptanceIds.split(StringPool.COMMA);
            this.fixedAssetsAcceptanceService.deleteFixedAssetsAcceptance(ids);
        } catch (Exception e) {
            message = "删除固定资产验收失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
