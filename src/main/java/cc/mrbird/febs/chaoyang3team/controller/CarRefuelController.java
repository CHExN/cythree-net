package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.CarRefuel;
import cc.mrbird.febs.chaoyang3team.service.CarRefuelService;
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
@RequestMapping("carRefuel")
public class CarRefuelController extends BaseController {

    private String message;

    @Autowired
    private CarRefuelService carRefuelService;

    @GetMapping
    @RequiresPermissions("carRefuel:view")
    public Map<String, Object> CarRefuelList(QueryRequest request, CarRefuel carRefuel) {
        return getDataTable(this.carRefuelService.findCarRefuel(request, carRefuel));
    }

    @GetMapping("carRefuelDetail")
    @RequiresPermissions("carRefuel:view")
    public List<CarRefuel> CarRefuelDetail(CarRefuel carRefuel) {
        return this.carRefuelService.findCarRefuelDetail(carRefuel);
    }

    @Log("新增车辆加油信息")
    @PostMapping
    @RequiresPermissions("carRefuel:add")
    public void addCarRefuel(@Valid String carRefuels) throws FebsException {
        try {
            this.carRefuelService.createCarRefuel(carRefuels);
        } catch (Exception e) {
            message = "新增车辆加油信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改车辆加油信息")
    @PutMapping
    @RequiresPermissions("carRefuel:update")
    public void updateCarRefuel(@Valid String carRefuels) throws FebsException {
        try {
            this.carRefuelService.updateCarRefuel(carRefuels);
        } catch (Exception e) {
            message = "修改车辆加油信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除车辆加油信息")
    @DeleteMapping("/{carRefuelDates}")
    @RequiresPermissions("carRefuel:delete")
    public void deleteCars(@NotBlank(message = "{required}") @PathVariable String carRefuelDates) throws FebsException {
        try {
            String[] dates = carRefuelDates.split(StringPool.COMMA);
            this.carRefuelService.deleteCarRefuel(dates);
        } catch (Exception e) {
            message = "删除车辆加油信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
