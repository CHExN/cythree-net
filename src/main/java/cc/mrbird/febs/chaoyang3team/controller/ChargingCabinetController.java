package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.ChargingCabinet;
import cc.mrbird.febs.chaoyang3team.domain.ChargingCabinetImport;
import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.ChargingCabinetService;
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
@RequestMapping("chargingCabinet")
public class ChargingCabinetController extends BaseController {

    private String message;

    @Autowired
    private ChargingCabinetService chargingCabinetService;
    @Autowired
    private StaffInsideService staffInsideService;
    @Autowired
    private StaffOutsideService staffOutsideService;

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

    @PostMapping("excel")
    @RequiresPermissions("chargingCabinet:export")
    public void export(QueryRequest request, ChargingCabinet chargingCabinet, HttpServletResponse response) throws FebsException {
        try {
            List<ChargingCabinet> chargingCabinets = this.chargingCabinetService.findChargingCabinetDetail(request, chargingCabinet).getRecords();
            ExcelKit.$Export(ChargingCabinet.class, response).downXlsx(chargingCabinets, false);
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
        List<ChargingCabinetImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            ChargingCabinetImport chargingCabinet = new ChargingCabinetImport();
            chargingCabinet.setAssetName("充电柜" + (i + 1));
            list.add(chargingCabinet);
        });
        // 构建模板
        ExcelKit.$Export(ChargingCabinetImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入充电柜明细Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("chargingCabinet:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file) throws FebsException {
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
            final List<ChargingCabinet> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            LocalDateTime now = LocalDateTime.now();
            ExcelKit.$Import(ChargingCabinetImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<ChargingCabinetImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, ChargingCabinetImport entity) {
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
                        ChargingCabinet chargingCabinet = new ChargingCabinet();
                        chargingCabinet.setAssetName(entity.getAssetName().equals("$EMPTY_CELL$") ? "" : entity.getAssetName());
                        chargingCabinet.setBrandModel(entity.getBrandModel().equals("$EMPTY_CELL$") ? "" : entity.getBrandModel());
                        chargingCabinet.setAllotmentDate(entity.getAllotmentDate().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getAllotmentDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        chargingCabinet.setInsideOrOutside(entity.getInsideOrOutside().equals("$EMPTY_CELL$") ? "" : entity.getInsideOrOutside());
                        chargingCabinet.setIdNum(entity.getIdNum().equals("$EMPTY_CELL$") ? "" : entity.getIdNum());
                        chargingCabinet.setChargingSectionsNumber(entity.getChargingSectionsNumber().equals("$EMPTY_CELL$") ? "" : entity.getChargingSectionsNumber());
                        chargingCabinet.setIfCharge(entity.getIfCharge().equals("$EMPTY_CELL$") ? "" : entity.getIfCharge());
                        chargingCabinet.setPlace(entity.getPlace().equals("$EMPTY_CELL$") ? "" : entity.getPlace());
                        chargingCabinet.setRemark(entity.getRemark().equals("$EMPTY_CELL$") ? "" : entity.getRemark());
                        chargingCabinet.setCreateTime(now);
                        data.add(chargingCabinet);
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
                this.chargingCabinetService.batchInsertChargingCabinet(data);
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
