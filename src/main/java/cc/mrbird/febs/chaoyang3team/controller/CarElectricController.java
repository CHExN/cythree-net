package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.CarElectric;
import cc.mrbird.febs.chaoyang3team.domain.CarElectricImport;
import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.CarElectricService;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
    @Autowired
    private StaffInsideService staffInsideService;
    @Autowired
    private StaffOutsideService staffOutsideService;

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

    @PostMapping("excel")
    @RequiresPermissions("carElectric:export")
    public void export(QueryRequest request, CarElectric carElectric, HttpServletResponse response) throws FebsException {
        try {
            List<CarElectric> carElectrics = this.carElectricService.findCarElectricDetail(request, carElectric).getRecords();
            ExcelKit.$Export(CarElectric.class, response).downXlsx(carElectrics, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 生成 Excel导入模板
     */
    @PostMapping("template")
    public void generateImportTemplate(HttpServletResponse response) {
        // 构建数据
        List<CarElectricImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            CarElectricImport carElectric = new CarElectricImport();
            carElectric.setCarType("电动车" + (i + 1));
            list.add(carElectric);
        });
        // 构建模板
        ExcelKit.$Export(CarElectricImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入电动车明细Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("carElectric:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, Integer ifThree) throws FebsException {
        try {
            if (file.isEmpty()) {
                throw new FebsException("导入数据为空");
            }
            String filename = file.getOriginalFilename();
            if (!StringUtils.endsWith(filename, ".xlsx")) {
                throw new FebsException("只支持.xlsx类型文件导入");
            }
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<CarElectric> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            LocalDateTime now = LocalDateTime.now();
            ExcelKit.$Import(CarElectricImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<CarElectricImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, CarElectricImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = null;
                    StaffInside staffInside = null;
                    if (entity.getIdNum().equals("$EMPTY_CELL$") || entity.getIdNum().equals("")) {
                        staffInside = new StaffInside();
                    }
                    if (entity.getInsideOrOutside().equals("0")) {
                        staffInside = staffInsideService.getStaffIdByIdNum(entity.getIdNum().trim());
                    } else if(entity.getInsideOrOutside().equals("1")) {
                        staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getIdNum().trim());
                    }
                    if ((staffInside == null && staffOutside == null)) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getIdNum().trim(),
                                "身份证号",
                                "查询不到此编" + (entity.getInsideOrOutside().equals("1") ? "外" : "内") + "人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        CarElectric carElectric = new CarElectric();
                        carElectric.setCarType(entity.getCarType().equals("$EMPTY_CELL$") ? "" : entity.getCarType());
                        carElectric.setCarBrands(entity.getCarBrands().equals("$EMPTY_CELL$") ? "" : entity.getCarBrands());
                        carElectric.setSteelFrameNumber(entity.getSteelFrameNumber().equals("$EMPTY_CELL$") ? "" : entity.getSteelFrameNumber());
                        carElectric.setMotorNumber(entity.getMotorNumber().equals("$EMPTY_CELL$") ? "" : entity.getMotorNumber());
                        carElectric.setCarNumber(entity.getCarNumber().equals("$EMPTY_CELL$") ? "" : entity.getCarNumber());
                        carElectric.setInsideOrOutside(entity.getInsideOrOutside().equals("$EMPTY_CELL$") ? "" : entity.getInsideOrOutside());
                        carElectric.setIdNum(entity.getIdNum().equals("$EMPTY_CELL$") ? "" : entity.getIdNum());
                        carElectric.setCarAllotmentDate(entity.getCarAllotmentDate().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getCarAllotmentDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        carElectric.setBatteryNumber(entity.getBatteryNumber().equals("$EMPTY_CELL$") ? "" : entity.getBatteryNumber());
                        carElectric.setBatteryReplacementDate1(entity.getBatteryReplacementDate1().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getBatteryReplacementDate1(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        carElectric.setBatteryReplacementDate2(entity.getBatteryReplacementDate2().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getBatteryReplacementDate2(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        carElectric.setIfLicense(entity.getIfLicense().equals("$EMPTY_CELL$") ? null : entity.getIfLicense());
                        carElectric.setStorageLocation(entity.getStorageLocation().equals("$EMPTY_CELL$") ? "" : entity.getStorageLocation());
                        carElectric.setStatus(entity.getStatus());
                        carElectric.setCreateTime(now);
                        carElectric.setIfThree(ifThree);
                        data.add(carElectric);
                    }
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.carElectricService.batchInsertCarElectric(data);
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(message);
        }
    }

}
