package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.CarElectric;
import cc.mrbird.febs.chaoyang3team.service.CarElectricService;
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
@RequestMapping("carElectric")
public class CarElectricController extends BaseController {

    private String message;

    @Autowired
    private CarElectricService carElectricService;

    @GetMapping
    @RequiresPermissions("carElectric:view")
    public Map<String, Object> CarElectricList(QueryRequest request, CarElectric carElectric) {
        return getDataTable(this.carElectricService.findCarElectricDetail(request, carElectric));
    }

    @Log("新增电动车")
    @PostMapping
    @RequiresPermissions("carElectric:add")
    public void addCarElectric(@Valid CarElectric carElectric) throws FebsException {
        try {
            this.carElectricService.createCarElectric(carElectric);
        } catch (Exception e) {
            message = "新增电动车失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改电动车")
    @PutMapping
    @RequiresPermissions("carElectric:update")
    public void updateCarElectric(@Valid CarElectric carElectric) throws FebsException {
        try {
            this.carElectricService.updateCarElectric(carElectric);
        } catch (Exception e) {
            message = "修改电动车失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除电动车")
    @DeleteMapping("/{carElectricIds}")
    @RequiresPermissions("carElectric:delete")
    public void deleteCarElectrics(@NotBlank(message = "{required}") @PathVariable String carElectricIds) throws FebsException {
        try {
            String[] ids = carElectricIds.split(StringPool.COMMA);
            this.carElectricService.deleteCarElectric(ids);
        } catch (Exception e) {
            message = "删除电动车失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
