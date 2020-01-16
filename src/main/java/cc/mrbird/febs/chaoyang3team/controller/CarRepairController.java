package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.CarRepair;
import cc.mrbird.febs.chaoyang3team.service.CarRepairService;
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

import javax.servlet.ServletRequest;
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
@RequestMapping("repair")
public class CarRepairController extends BaseController {

    private String message;

    @Autowired
    private CarRepairService carRepairService;

    @GetMapping
    @RequiresPermissions("carRepair:view")
    public Map<String, Object> RepairList(QueryRequest request, CarRepair carRepair) {
        return getDataTable(this.carRepairService.findRepairDetail(request, carRepair));
    }

    @Log("新增车辆报修申请")
    @PostMapping
    @RequiresPermissions("carRepair:add")
    public void addRepair(@Valid CarRepair carRepair, ServletRequest servletRequest) throws FebsException {
        try {
            this.carRepairService.createRepair(carRepair, servletRequest);
        } catch (Exception e) {
            message = "新增车辆报修申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改车辆报修申请")
    @PutMapping
    @RequiresPermissions("carRepair:update")
    public void updateRepair(@Valid CarRepair carRepair) throws FebsException {
        try {
            this.carRepairService.updateRepair(carRepair);
        } catch (Exception e) {
            message = "修改车辆报修申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除车辆报修申请")
    @DeleteMapping("/{repairIds}")
    @RequiresPermissions("carRepair:delete")
    public void deleteRepairs(@NotBlank(message = "{required}") @PathVariable String repairIds) throws FebsException {
        try {
            String[] ids = repairIds.split(StringPool.COMMA);
            this.carRepairService.deleteRepair(ids);
        } catch (Exception e) {
            message = "删除车辆报修申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("carRepair:export")
    public void export(QueryRequest request, CarRepair carRepair, HttpServletResponse response) throws FebsException {
        try {
            List<CarRepair> carRepairs = this.carRepairService.findRepairDetail(request, carRepair).getRecords();
            ExcelKit.$Export(CarRepair.class, response).downXlsx(carRepairs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
