package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.CarRecord;
import cc.mrbird.febs.chaoyang3team.service.CarRecordService;
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
@RequestMapping("carRecord")
public class CarRecordController extends BaseController {

    private String message;

    @Autowired
    private CarRecordService carRecordService;

    @GetMapping
    @RequiresPermissions("carRecord:view")
    public Map<String, Object> carRecordList(QueryRequest request, CarRecord carRecord) {
        return getDataTable(this.carRecordService.findCarRecordDetail(request, carRecord));
    }

    @Log("新增车辆维修保养")
    @PostMapping
    @RequiresPermissions("carRecord:add")
    public void addCarRecord(@Valid CarRecord carRecord) throws FebsException {
        try {
            this.carRecordService.createCarRecord(carRecord);
        } catch (Exception e) {
            message = "新增车辆维修保养失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改车辆维修保养")
    @PutMapping
    @RequiresPermissions("carRecord:update")
    public void updateCarRecord(@Valid CarRecord carRecord) throws FebsException {
        try {
            this.carRecordService.updateCarRecord(carRecord);
        } catch (Exception e) {
            message = "修改车辆维修保养失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除车辆维修保养")
    @DeleteMapping("/{carRecordIds}")
    @RequiresPermissions("carRecord:delete")
    public void deleteCarRecords(@NotBlank(message = "{required}") @PathVariable String carRecordIds) throws FebsException {
        try {
            String[] ids = carRecordIds.split(StringPool.COMMA);
            this.carRecordService.deleteCarRecord(ids);
        } catch (Exception e) {
            message = "删除车辆维修保养失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("carRecord:export")
    public void export(QueryRequest request, CarRecord carRecord, HttpServletResponse response) throws FebsException {
        try {
            List<CarRecord> carRecords = this.carRecordService.findCarRecordDetailExcel(request, carRecord).getRecords();
            ExcelKit.$Export(CarRecord.class, response).downXlsx(carRecords, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
