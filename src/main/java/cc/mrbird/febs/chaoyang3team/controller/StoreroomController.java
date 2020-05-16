package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.*;
import cc.mrbird.febs.chaoyang3team.service.StoreroomService;
import cc.mrbird.febs.chaoyang3team.service.UnitConversionService;
import cc.mrbird.febs.chaoyang3team.service.WcService;
import cc.mrbird.febs.chaoyang3team.service.WcStoreroomService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
@RequestMapping("storeroom")
public class StoreroomController extends BaseController {

    private String message;

    @Autowired
    private StoreroomService storeroomService;
    @Autowired
    private WcService wcService;
    @Autowired
    private WcStoreroomService wcStoreroomService;
    @Autowired
    private UnitConversionService unitConversionService;

    @GetMapping
    @RequiresPermissions("storeroom:view")
    public Map<String, Object> storeroomList(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        return getDataTable(this.storeroomService.findStoreroomsDetail(request, storeroom, servletRequest));
    }

    @GetMapping("itemDetails")
    @RequiresPermissions("storeroomItemDetails:view")
    public Map<String, Object> findStoreroomsItemDetails(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        return getDataTable(this.storeroomService.findStoreroomsItemDetails(request, storeroom, servletRequest));
    }

    @GetMapping("storeroomOutItem")
    @RequiresPermissions("storeroomItemDetails:view")
    public List<Storeroom> getStoreroomOutItem(Storeroom storeroom) {
        return storeroomService.getStoreroomOutItemByParentIdAndId(storeroom);
    }

    @GetMapping("storeroomDist")
    @RequiresPermissions("storeroomDist:view")
    public Map<String, Object> storeroomDistList(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        return getDataTable(this.storeroomService.getStoreroomsDist(request, storeroom, servletRequest));
    }

    // 这个不用了
    @GetMapping("officeSuppliesByDate")
    @RequiresPermissions("officeSupplies:export")
    public List<Storeroom> getOfficeSuppliesByDate(String date) {
        return storeroomService.getOfficeSuppliesByDate(date);
    }

    @GetMapping("canteen")
    @RequiresPermissions("canteen:export")
    public List<Storeroom> getCanteenByDate(String date, String dateRangeFrom, String dateRangeTo) {
        return storeroomService.getCanteenByDate(date, dateRangeFrom, dateRangeTo);
    }

    @GetMapping("canteenSupplier")
    @RequiresPermissions("canteen:export")
    public Map<String, List<Storeroom>> getCanteenBySupplierClassification(String dateRangeFrom, String dateRangeTo, String day) {
        return storeroomService.getCanteenBySupplierClassification(dateRangeFrom, dateRangeTo, day);
    }

    @PostMapping("distExcel")
    @RequiresPermissions("wcStoreroom:view")
    public void export(QueryRequest request, Storeroom storeroom, HttpServletResponse response, ServletRequest servletRequest) throws FebsException {
        try {
            List<Storeroom> storerooms = this.storeroomService.getStoreroomsDist(request, storeroom, servletRequest).getRecords();
            ExcelKit.$Export(StoreroomDist.class, response).downXlsx(storerooms, false);
        } catch (Exception e) {
            message = "导出分配物资Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 导出 distWc
     */
    @PostMapping("distWc")
    public void exportDistWc(QueryRequest request, Wc wc, HttpServletResponse response) throws FebsException {
        try {
            List<Wc> wcs = this.wcService.findWcDetailExcel(request, wc).getRecords();
            ExcelKit.$Export(DistWc.class, response).downXlsx(wcs, false);
        } catch (Exception e) {
            message = "导出公厕Excel失败";
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
        List<StoreroomDistImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            StoreroomDistImport storeroomDistImport = new StoreroomDistImport();
            storeroomDistImport.setYear("2020");
            storeroomDistImport.setMonth("04");
            storeroomDistImport.setDay("01");
            storeroomDistImport.setAmount(BigDecimal.valueOf(10));
            storeroomDistImport.setStoreroomId(6300 + i + "");
            storeroomDistImport.setStoreroomName("表头未带[*]为非必填");
            storeroomDistImport.setWcName("表头未带[*]为非必填");
            storeroomDistImport.setWcNum("10510" + (20000000 + i));
            list.add(storeroomDistImport);
        });
        // 构建模板
        ExcelKit.$Export(StoreroomDistImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入公厕信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("wcStoreroom:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, ServletRequest servletRequest) throws FebsException {
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
            final List<WcStoreroom> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            final Map<String, Wc> wcNums = new HashMap<>();
            final Map<String, Storeroom> storeroomIds = new HashMap<>();
            // 获取当前操作用户的账号名称
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));


            ExcelKit.$Import(StoreroomDistImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<StoreroomDistImport>() {
                @Override
                public void onSuccess(int sheetIndex, int rowIndex, StoreroomDistImport entity) {

                    /*
                     判断是否有此公厕编号
                     */
                    String wcNum = entity.getWcNum().trim();
                    wcNum = wcNum.substring(0, 13);
                    Wc wc;
                    if (wcNums.isEmpty() || !wcNums.containsKey(wcNum)) {
                        wc = wcService.getWcByWcNum(wcNum);
                        wcNums.put(wcNum, wc);
                    } else {
                        wc = wcNums.get(wcNum);
                    }
                    if (wc == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                3,
                                entity.getWcNum(),
                                "公厕编号",
                                "查询不到编号为「" + entity.getWcNum() + "」的这个公厕"));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }


                    /*
                     判断是否有此物品
                     */
                    String storeroomId = entity.getStoreroomId().trim();
                    Storeroom storeroom;
                    if (storeroomIds.isEmpty() || !storeroomIds.containsKey(storeroomId)) {
                        storeroom = storeroomService.getStoreroomById(storeroomId);
                        storeroomIds.put(storeroomId, storeroom);
                    } else {
                        storeroom = storeroomIds.get(storeroomId);
                    }
                    if (storeroom == null) {
                        System.out.println("============================   storeroom为null   =============================");
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                5,
                                storeroomId,
                                "物品编号",
                                "查询不到编号为「" + storeroomId + "」的这个物品"));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }


                    /*
                      判断数量是否有超出
                      */
                    UnitConversion unitConversion = unitConversionService.findById(storeroomId);
                    if (unitConversion == null) {
                        BigDecimal amountDistBefore = storeroom.getAmountDist();
                        storeroom.setAmountDist(amountDistBefore.subtract(entity.getAmount()));
                        System.out.println("比较" + storeroom.getAmountDist() + " ? " + BigDecimal.ZERO);
                        int compare = storeroom.getAmountDist().compareTo(BigDecimal.ZERO);
                        System.out.println("结果 > " + compare);
                        if (compare == -1) {
                            storeroom.setAmountDist(amountDistBefore);
                            List<ExcelErrorField> errorFields = new ArrayList<>();
                            errorFields.add(new ExcelErrorField(
                                    7,
                                    entity.getAmount() + "",
                                    "分配数量",
                                    "「" + entity.getAmount() + "」分配数量已超出「" + entity.getStoreroomName()
                                            + "」这个物品剩余的库存（库存剩余：" + amountDistBefore + "）"));
                            onError(sheetIndex, rowIndex, errorFields);
                            return;
                        } else if (compare == 0) {
                            storeroom.setStatus("1");
                        }
                        storeroomService.updateStoreroom(storeroom);
                    } else {
                        BigDecimal amountDistBefore = unitConversion.getAmountDist();
                        unitConversion.setAmountDist(amountDistBefore.subtract(entity.getAmount()));
                        System.out.println("比较" + unitConversion.getAmountDist() + " ? " + BigDecimal.ZERO);
                        int compare = unitConversion.getAmountDist().compareTo(BigDecimal.ZERO);
                        System.out.println("结果 > " + compare);
                        if (compare == -1) {
                            storeroom.setAmountDist(amountDistBefore);
                            List<ExcelErrorField> errorFields = new ArrayList<>();
                            errorFields.add(new ExcelErrorField(
                                    7,
                                    entity.getAmount() + "",
                                    "分配数量",
                                    "「" + entity.getAmount() + "」分配数量已超出「" + entity.getStoreroomName()
                                            + "」这个物品剩余的库存（库存剩余：" + amountDistBefore + "）"));
                            onError(sheetIndex, rowIndex, errorFields);
                            return;
                        } else if (compare == 0) {
                            storeroom.setStatus("1");
                            storeroomService.updateStoreroom(storeroom);
                        }
                        unitConversionService.saveOrUpdateUnitConversion(unitConversion);
                    }


                    /*
                    判断日期格式是否正确
                     */
                    String year = entity.getYear().trim();
                    String month = entity.getMonth().trim();
                    String day = entity.getDay().trim();
                    if (year.length() != 4) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                year,
                                "年",
                                "“年”日期格式不对"));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }
                    if (month.length() != 1 && month.length() != 2) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                1,
                                month,
                                "月",
                                "“月”日期格式不对"));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }
                    if (month.length() == 1) month = "0" + month;
                    if (day.length() != 1 && day.length() != 2) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                2,
                                day,
                                "日",
                                "“日”日期格式不对"));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }
                    if (day.length() == 1) day = "0" + day;


                    /*
                    数据校验成功时，加入集合
                     */
                    WcStoreroom wcStoreroom = new WcStoreroom();
                    wcStoreroom.setYear(year);
                    wcStoreroom.setMonth(month);
                    wcStoreroom.setUsername(username);
                    wcStoreroom.setWcOwn(wc.getWcOwn());
                    wcStoreroom.setDay(day);
                    wcStoreroom.setWcId(wc.getWcId());
                    wcStoreroom.setAmount(entity.getAmount());
                    wcStoreroom.setStoreroomId(Long.valueOf(storeroomId));

                    data.add(wcStoreroom);

                    wcStoreroom.setWcName(wc.getWcName());
                    wcStoreroom.setWcNum(wc.getWcNum());
                    wcStoreroom.setStoreroomName(storeroom.getName());
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.wcStoreroomService.batchInsertWcStoreroom(data);
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
