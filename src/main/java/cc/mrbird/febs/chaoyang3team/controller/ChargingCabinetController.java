package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.ChargingCabinet;
import cc.mrbird.febs.chaoyang3team.service.ChargingCabinetService;
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
@RequestMapping("chargingCabinet")
public class ChargingCabinetController extends BaseController {

    private String message;

    @Autowired
    private ChargingCabinetService chargingCabinetService;

    @GetMapping
    @RequiresPermissions("chargingCabinet:view")
    public Map<String, Object> ChargingCabinetList(QueryRequest request, ChargingCabinet chargingCabinet) {
        return getDataTable(this.chargingCabinetService.findChargingCabinetDetail(request, chargingCabinet));
    }

    @Log("新增充电柜")
    @PostMapping
    @RequiresPermissions("chargingCabinet:add")
    public void addChargingCabinet(@Valid ChargingCabinet chargingCabinet) throws FebsException {
        try {
            this.chargingCabinetService.createChargingCabinet(chargingCabinet);
        } catch (Exception e) {
            message = "新增充电柜失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改充电柜")
    @PutMapping
    @RequiresPermissions("chargingCabinet:update")
    public void updateChargingCabinet(@Valid ChargingCabinet chargingCabinet) throws FebsException {
        try {
            this.chargingCabinetService.updateChargingCabinet(chargingCabinet);
        } catch (Exception e) {
            message = "修改充电柜失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除充电柜")
    @DeleteMapping("/{chargingCabinetIds}")
    @RequiresPermissions("chargingCabinet:delete")
    public void deleteChargingCabinets(@NotBlank(message = "{required}") @PathVariable String chargingCabinetIds) throws FebsException {
        try {
            String[] ids = chargingCabinetIds.split(StringPool.COMMA);
            this.chargingCabinetService.deleteChargingCabinet(ids);
        } catch (Exception e) {
            message = "删除充电柜失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
