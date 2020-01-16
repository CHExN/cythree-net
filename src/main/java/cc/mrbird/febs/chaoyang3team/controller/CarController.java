package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Car;
import cc.mrbird.febs.chaoyang3team.service.CarService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("car")
public class CarController extends BaseController {

    private String message;

    @Autowired
    private CarService carService;

    @GetMapping("carKind")
    public List<String> getCarKind() {
        return this.carService.getCarKind();
    }

    @GetMapping("carUse")
    public List<String> getCarUse() {
        return this.carService.getCarUse();
    }

    @GetMapping("carSimplify")
    @RequiresPermissions("car:simplifyView")
    public List<Car> carSimplifyList() {
        return this.carService.getCarSimplify();
    }

    @GetMapping
    @RequiresPermissions("car:view")
    public Map<String, Object> carList(QueryRequest request, Car car) {
        return getDataTable(this.carService.findCar(request, car));
    }

    @Log("新增车辆")
    @PostMapping
    @RequiresPermissions("car:add")
    public void addCar(@Valid Car car) throws FebsException {
        try {
            this.carService.createCar(car);
        } catch (Exception e) {
            message = "新增车辆失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改车辆")
    @PutMapping
    @RequiresPermissions("car:update")
    public void updateCar(@Valid Car car) throws FebsException {
        try {
            this.carService.updateCar(car);
        } catch (Exception e) {
            message = "修改车辆失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("批量修改车辆")
    @PutMapping("updateBatchCar")
    @RequiresPermissions("car:update")
    public void updateBatchCar(@Valid String cars) throws FebsException {
        try {
            this.carService.updateBatchCar(cars);
        } catch (Exception e) {
            message = "批量修改车辆失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除车辆")
    @DeleteMapping("/{carIds}")
    @RequiresPermissions("car:delete")
    public void deleteCars(@NotBlank(message = "{required}") @PathVariable String carIds) throws FebsException {
        try {
            String[] ids = carIds.split(StringPool.COMMA);
            this.carService.deleteCar(ids);
        } catch (Exception e) {
            message = "删除车辆失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("car:export")
    public void export(QueryRequest request, Car car, HttpServletResponse response) throws FebsException {
        try {
            List<Car> cars = this.carService.findCar(request, car).getRecords();
            ExcelKit.$Export(Car.class, response).downXlsx(cars, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
