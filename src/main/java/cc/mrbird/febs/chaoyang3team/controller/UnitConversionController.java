package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.UnitConversion;
import cc.mrbird.febs.chaoyang3team.service.UnitConversionService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.exception.FebsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("unitConversion")
public class UnitConversionController extends BaseController {

    @Autowired
    private UnitConversionService unitConversionService;

    @GetMapping("/{id}")
    public UnitConversion checkUserName(@NotBlank(message = "{required}") @PathVariable String id) {
        return this.unitConversionService.findById(id);
    }

    @Log("新增/修改单位转换")
    @PostMapping
    public void saveOrUpdateUnitConversion(@Valid UnitConversion unitConversion) throws FebsException {
        try {
            this.unitConversionService.saveOrUpdateUnitConversion(unitConversion);
        } catch (Exception e) {
            String message = "新增/修改单位转换异常";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
