package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcImport;
import cc.mrbird.febs.chaoyang3team.service.WcFileService;
import cc.mrbird.febs.chaoyang3team.service.WcService;
import cc.mrbird.febs.chaoyang3team.service.WcStoreroomService;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wc")
public class WcController extends BaseController {

    private String message;

    @Autowired
    private WcService wcService;
    @Autowired
    private WcFileService wcFileService;
    @Autowired
    private WcStoreroomService storeroomService;

    @GetMapping("weChat/wcList")
    public List<Wc> findWcListByPosition(String longitude, String latitude, Integer radius, Integer length) {
        return wcService.findWcListByPosition(longitude, latitude, radius, length);
    }

    @GetMapping("weChat/wcInfo")
    public Wc getWcInfo(Long wcId) {
        return this.wcService.getWcAndFilesById(wcId);
    }

    @GetMapping("index/{year}")
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String year) {
        Map<String, Object> data = new HashMap<>();
        // 获取所有月份的公厕消耗
        List<Map<String, Object>> allMonthWcConsumptionByYear = wcService.findAllMonthWcConsumptionByYear(year);
        data.put("allMonthWcConsumptionByYear", allMonthWcConsumptionByYear);
        // 所有分队前7消耗量排名
        List<Map<String, Object>> allOwnWcConsumptionByYear = wcService.findAllOwnWcConsumptionByYear(year);
        data.put("allOwnWcConsumptionByYear", allOwnWcConsumptionByYear);
        return new FebsResponse().data(data);
    }

    @GetMapping("wcCostAccount")
    public List<Wc> findWcCostAccount(String year, String month) {
        return wcService.findWcCostAccount(year, month);
    }

    @GetMapping("findWcCostAccountByYear")
    public List<Wc> findWcCostAccountByYear(String year, Integer up) {
        return wcService.findWcCostAccountByYear(year, up);
    }

    @PostMapping("uploadWcPhoto")
    public FebsResponse uploadWcPhoto(@RequestParam("file") MultipartFile file, String wcId) throws FebsException {
        try {
            return this.wcService.uploadWcPhoto(file, wcId);
        } catch (Exception e) {
            message = "上传公厕图片失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("wcOwns")
    public List<String> findWcOwns() {
        return this.wcService.findWcOwns();
    }

    @GetMapping("wcFiles")
    public FebsResponse findFilesByWcId(String wcId) {
        return this.wcFileService.findFilesByWcId(wcId);
    }

    @GetMapping("wcName")
    @RequiresPermissions("wc:wcName")
    public Map<String, Object> WcNameList(QueryRequest request, Wc wc, ServletRequest servletRequest) {
//        return getDataTable(this.wcService.getWcName(request, wc, servletRequest));
        return getDataTable(this.wcService.findWcDetail(request, wc));
    }

    @GetMapping("selectOne")
    @RequiresPermissions("wc:view")
    public Wc selectOne(Long wcId) {
        return this.wcService.selectOne(wcId);
    }

    @GetMapping
    @RequiresPermissions("wc:view")
    public Map<String, Object> WcList(QueryRequest request, Wc wc) {
        return getDataTable(this.wcService.findWcDetail(request, wc));
    }

    @Log("新增公厕")
    @PostMapping
    @RequiresPermissions("wc:add")
    public void addWc(@Valid Wc wc) throws FebsException {
        try {
            this.wcService.createWc(wc);
        } catch (Exception e) {
            message = "新增公厕失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("addWcStoreroom")
    @RequiresPermissions("wcStoreroom:add")
    public void addWcStoreroom(@Valid String wcStoreroomStr, BigDecimal amountDist, ServletRequest servletRequest) throws FebsException {
        try {
            this.storeroomService.createWcStoreroom(wcStoreroomStr, amountDist, servletRequest);
        } catch (Exception e) {
            message = "新增公厕与出库记录关系失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除公厕")
    @DeleteMapping("/{wcIds}")
    @RequiresPermissions("wc:delete")
    public void deleteWc(@NotBlank(message = "{required}") @PathVariable String wcIds) throws FebsException {
        try {
            String[] ids = wcIds.split(StringPool.COMMA);
            this.wcService.deleteWc(ids);
        } catch (Exception e) {
            message = "删除公厕失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除公厕文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    public void deleteWcFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.wcService.deleteWcFile(ids);
        } catch (Exception e) {
            message = "删除公厕文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改公厕")
    @PutMapping
    @RequiresPermissions("wc:update")
    public void updateWc(@Valid Wc wc) throws FebsException {
        try {
            this.wcService.updateWc(wc);
        } catch (Exception e) {
            message = "修改公厕失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("wc:export")
    public void export(QueryRequest request, Wc wc, HttpServletResponse response) throws FebsException {
        try {
            List<Wc> wcs = this.wcService.findWcDetailExcel(request, wc).getRecords();
            ExcelKit.$Export(Wc.class, response).downXlsx(wcs, false);
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
        List<Wc> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            Wc wc = new Wc();
            wc.setWcName("公厕名称" + (i + 1));
            wc.setDistrict("1");
            wc.setManageUnit("1");
            wc.setWcNum("公厕编号" + (i + 1));
            wc.setWcKind("1");
            wc.setBuildingMethod("1");
            wc.setBuildingType("0");
            wc.setWcSort("1");
            wc.setUseDate(LocalDate.now());
            wc.setReplaceDate(LocalDate.now());
            wc.setManageType("0");
            list.add(wc);
        });
        // 构建模板
        ExcelKit.$Export(WcImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入公厕信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("wc:add")
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
            final List<Wc> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(WcImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<WcImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, WcImport entity) {
                    // 数据校验成功时，加入集合
                    Wc wc = new Wc();
                    wc.setWcName(entity.getWcName());
                    wc.setDistrict(entity.getDistrict().equals("$EMPTY_CELL$") ? "" : entity.getDistrict());
                    wc.setManageUnit(entity.getManageUnit().equals("$EMPTY_CELL$") ? "" : entity.getManageUnit());
                    wc.setWcNum(entity.getWcNum());
                    wc.setLongitude(entity.getLongitude().equals("$EMPTY_CELL$") ? "" : entity.getLongitude());
                    wc.setLatitude(entity.getLatitude().equals("$EMPTY_CELL$") ? "" : entity.getLatitude());
                    wc.setWcKind(entity.getWcKind().equals("$EMPTY_CELL$") ? "" : entity.getWcKind());
                    wc.setWcKind(entity.getWcKind().equals("$EMPTY_CELL$") ? "" : entity.getWcKind());
                    wc.setBuildingMethod(entity.getBuildingMethod().equals("$EMPTY_CELL$") ? "" : entity.getBuildingMethod());
                    wc.setBuildingType(entity.getBuildingType().equals("$EMPTY_CELL$") ? "" : entity.getBuildingType());
                    wc.setIsManageRoom(entity.getIsManageRoom().equals("$EMPTY_CELL$") ? "" : entity.getIsManageRoom());
                    wc.setWcSort(entity.getWcSort().equals("$EMPTY_CELL$") ? "" : entity.getWcSort());
                    wc.setFlushingMethod(entity.getFlushingMethod().equals("$EMPTY_CELL$") ? "" : entity.getFlushingMethod());
                    wc.setIndicatorStatus(entity.getIndicatorStatus().equals("$EMPTY_CELL$") ? "" : entity.getIndicatorStatus());
                    wc.setStreetTown(entity.getStreetTown().equals("$EMPTY_CELL$") ? "" : entity.getStreetTown());
                    wc.setIs5thRing(entity.getIs5thRing().equals("$EMPTY_CELL$") ? "" : entity.getIs5thRing());
                    wc.setUseDate(entity.getUseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    wc.setReplaceDate(entity.getReplaceDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    wc.setInitialMoney(entity.getInitialMoney().equals("$EMPTY_CELL$") ? "" : entity.getInitialMoney());
                    wc.setWcArea(entity.getWcArea().equals("$EMPTY_CELL$") ? "" : entity.getWcArea());
                    wc.setIsPropertyRight(entity.getIsPropertyRight().equals("$EMPTY_CELL$") ? "" : entity.getIsPropertyRight());
                    wc.setSink(entity.getSink().equals("$EMPTY_CELL$") ? "" : entity.getSink());
                    wc.setPitMale(entity.getPitMale().equals("$EMPTY_CELL$") ? "" : entity.getPitMale());
                    wc.setPitFemale(entity.getPitFemale().equals("$EMPTY_CELL$") ? "" : entity.getPitFemale());
                    wc.setPitSexless(entity.getPitSexless().equals("$EMPTY_CELL$") ? "" : entity.getPitSexless());
                    wc.setUrinalsType(entity.getUrinalsType().equals("$EMPTY_CELL$") ? "" : entity.getUrinalsType());
                    wc.setUrinalsNum(entity.getUrinalsNum().equals("$EMPTY_CELL$") ? "" : entity.getUrinalsNum());
                    wc.setIsAccessibility(entity.getIsAccessibility().equals("$EMPTY_CELL$") ? "" : entity.getIsAccessibility());
                    wc.setIndicatorNum(entity.getIndicatorNum().equals("$EMPTY_CELL$") ? "" : entity.getIndicatorNum());
                    wc.setIsDisabledRoom(entity.getIsDisabledRoom().equals("$EMPTY_CELL$") ? "" : entity.getIsDisabledRoom());
                    wc.setFecalMethod(entity.getFecalMethod().equals("$EMPTY_CELL$") ? "" : entity.getFecalMethod());
                    wc.setUrinalsLen(entity.getUrinalsLen().equals("$EMPTY_CELL$") ? "" : entity.getUrinalsLen());
                    wc.setOpenHour(entity.getOpenHour().equals("$EMPTY_CELL$") ? "" : entity.getOpenHour());
                    wc.setAssignmentStyle(entity.getAssignmentStyle().equals("$EMPTY_CELL$") ? "" : entity.getAssignmentStyle());
                    wc.setIsIndicator(entity.getIsIndicator().equals("$EMPTY_CELL$") ? "" : entity.getIsIndicator());
                    wc.setWcOwn(entity.getWcOwn().equals("$EMPTY_CELL$") ? "" : entity.getWcOwn());
                    wc.setManageType(entity.getManageType().equals("$EMPTY_CELL$") ? "" : entity.getManageType());
                    wc.setCleanNum(entity.getCleanNum().equals("$EMPTY_CELL$") ? "" : entity.getCleanNum());
                    wc.setWcAddress(entity.getWcAddress().equals("$EMPTY_CELL$") ? "" : entity.getWcAddress());
                    wc.setWcNowStatus(entity.getWcNowStatus().equals("$EMPTY_CELL$") ? "" : entity.getWcNowStatus());
                    wc.setWaterNum(entity.getWaterNum().equals("$EMPTY_CELL$") ? "" : entity.getWaterNum());
                    wc.setElectricityNum(entity.getElectricityNum().equals("$EMPTY_CELL$") ? "" : entity.getElectricityNum());
                    wc.setPaymentNum(entity.getPaymentNum().equals("$EMPTY_CELL$") ? "" : entity.getPaymentNum());
                    data.add(wc);
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.wcService.batchInsertWc(data);
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
