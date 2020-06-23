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
import org.apache.commons.lang3.SerializationUtils;
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
import java.time.LocalDateTime;
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
            final Map<String, List<Storeroom>> storeroomNames = new HashMap<>();
            final Map<String, UnitConversion> unitConversions = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();
            // 获取当前操作用户的账号名称
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));


            ExcelKit.$Import(StoreroomDistImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<StoreroomDistImport>() {
                @Override
                public void onSuccess(int sheetIndex, int rowIndex, StoreroomDistImport entity) {



                    /*
                    判断要分配的数量如果为空，或者为0，就直接跳到下一条
                     */
                    if (entity.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                        System.out.println("等于0");
                        return;
                    }


                    // 创建要insert的分配记录，并设置导入用户和当前时间
                    WcStoreroom wcStoreroom = new WcStoreroom();
                    wcStoreroom.setUsername(username);
                    wcStoreroom.setCreateTime(now);



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
                    // 分配记录设置年月日
                    wcStoreroom.setYear(year);
                    wcStoreroom.setMonth(month);
                    wcStoreroom.setDay(day);




                    /*
                     判断是否有此公厕编号
                     */
                    String wcNum = entity.getWcNum().trim();
                    // 如果公厕编号超过13位，就把后面的截掉
                    if (wcNum.length() > 13) {
                        wcNum = wcNum.substring(0, 13);
                    }
                    Wc wc;
                    if (wcNums.isEmpty() || !wcNums.containsKey(wcNum)) {
                        wc = wcService.getWcByWcNum(wcNum, false);
                        wcNums.put(wcNum, wc);
                    } else {
                        wc = wcNums.get(wcNum);
                    }
                    if (wc == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        StringBuilder message = new StringBuilder();
                        message.append("查询不到 “公厕编号” 为「").append(wcNum).append("」");
                        if (!(entity.getWcName().equals("") || entity.getWcName().equals("$EMPTY_CELL$"))) {
                            message.append("、“公厕名称”为「").append(entity.getWcName()).append("」");
                        }
                        message.append("的这个公厕，请检查编号是否填写有误");
                        errorFields.add(new ExcelErrorField(
                                3,
                                wcNum,
                                "公厕编号",
                                message.toString()));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }
                    // 设置公厕相关信息
                    wcStoreroom.setWcOwn(wc.getWcOwn());
                    wcStoreroom.setWcId(wc.getWcId());
                    wcStoreroom.setWcName(wc.getWcName());
                    wcStoreroom.setWcNum(wc.getWcNum());



                    /*
                     判断是否有此物品
                     */
                    String storeroomId = entity.getStoreroomId().trim();
                    String storeroomName = entity.getStoreroomName().trim();
                    Storeroom storeroom = null;
                    String message;
                    boolean isId = true;
                    List<Storeroom> storeroomsByName = new ArrayList<>();

                    // 如果物品id不为空
                    if (!(storeroomId.equals("$EMPTY_CELL$") || storeroomId.isEmpty())) {
                        // 现物品id没有存在已查询<id,物品>集里
                        if (!storeroomIds.containsKey(storeroomId)) {
                            // 判断如果物品id不为空就查询，如果物品名称不为空就查询
                            storeroom = storeroomService.getStoreroomById(storeroomId);
                            storeroomIds.put(storeroomId, storeroom);
                        } else {
                            storeroom = storeroomIds.get(storeroomId);
                        }
                        message = "查询不到 “物品编号” 为「" + storeroomId + "」的这个物品";
                        // 如果物品名称不为空
                    } else if (!(storeroomName.equals("$EMPTY_CELL$") || storeroomName.isEmpty())) {
                        isId = false;
                        // 现物品id没有存在已查询<name,物品>集里
                        if (!storeroomNames.containsKey(storeroomName)) {
                            storeroomsByName = storeroomService.getStoreroomsByName(storeroomName);
                            if (!storeroomsByName.isEmpty()) {
                                storeroom = storeroomsByName.get(0);
                            }
                            storeroomNames.put(storeroomName, storeroomsByName);
                        } else {
                            storeroomsByName = storeroomNames.get(storeroomName);
                            if (!storeroomsByName.isEmpty()) {
                                storeroom = storeroomsByName.get(0);
                            }
                        }
                        message = "查询不到 “物品名称” 为「" + storeroomName + "」的这个物品";
                    } else {
                        message = "请填写 “物品编号” 或者 “物品名称” 其中任意一项";
                    }

                    if (storeroom == null) {
                        System.out.println("============================   storeroom为null   =============================");
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                !isId ? 6 : 5,
                                !isId ? storeroomName : storeroomId,
                                !isId ? "物品名称" : "物品编号",
                                message));
                        onError(sheetIndex, rowIndex, errorFields);
                        return;
                    }



                    /*
                      判断数量是否有超出，并导入
                      */
                    if (isId) { // 以下是填写了物品编号的导入操作
                        UnitConversion unitConversion;
                        if (!unitConversions.containsKey(storeroom.getId().toString())) {
                            // 判断如果物品id不为空就查询，如果物品名称不为空就查询
                            unitConversion = unitConversionService.findById(storeroom.getId().toString());
                            unitConversions.put(storeroom.getId().toString(), unitConversion);
                        } else {
                            unitConversion = unitConversions.get(storeroom.getId().toString());
                        }
                        // UnitConversion unitConversion = unitConversionService.findById(storeroom.getId().toString());
                        if (unitConversion != null) { // 此物资有单位转换
                            // 获取此物资剩余分配的数量
                            BigDecimal amountDistBefore = unitConversion.getAmountDist();
                            // 把剩余分配数量 - 要分配的数量 赋值给剩余分配的数量
                            unitConversion.setAmountDist(amountDistBefore.subtract(entity.getAmount()));
                            int compare = unitConversion.getAmountDist().compareTo(BigDecimal.ZERO);
                            if (compare < 0) { // compare == -1
                                List<ExcelErrorField> errorFields = new ArrayList<>();
                                StringBuilder message1 = new StringBuilder();
                                message1.append("分配数量「").append(entity.getAmount()).append("」已超出 ");
                                if (!(entity.getStoreroomId().equals("") || entity.getStoreroomId().equals("$EMPTY_CELL$"))) {
                                    message1.append("“物品编号”为「").append(entity.getStoreroomName()).append("」");
                                }
                                if (!(entity.getStoreroomName().equals("") || entity.getStoreroomName().equals("$EMPTY_CELL$"))) {
                                    message1.append("“物品名称”为「").append(entity.getStoreroomName()).append("」");
                                }
                                message1.append("的这个物品剩余库存，（库存剩余：").append(amountDistBefore).append("）");
                                errorFields.add(new ExcelErrorField(
                                        7,
                                        entity.getAmount().toString(),
                                        "分配数量",
                                        message1.toString()));
                                onError(sheetIndex, rowIndex, errorFields);
                                return;
                            } else if (compare == 0) {
                                storeroom.setStatus("1");
                                storeroomService.updateStoreroom(storeroom);
                            }
                            // 运行到此代码段的情况必定为 compare == 0 || compare == 1
                            unitConversionService.saveOrUpdateUnitConversion(unitConversion);
                        } else { // 此物资没有单位转换
                            // 获取此物资剩余分配的数量
                            BigDecimal amountDistBefore = storeroom.getAmountDist();
                            // 把剩余分配数量 - 要分配的数量 赋值给剩余分配的数量
                            storeroom.setAmountDist(amountDistBefore.subtract(entity.getAmount()));
                            int compare = storeroom.getAmountDist().compareTo(BigDecimal.ZERO);
                            if (compare < 0) { // compare == -1
                                List<ExcelErrorField> errorFields = new ArrayList<>();
                                StringBuilder message1 = new StringBuilder();
                                message1.append("分配数量「").append(entity.getAmount()).append("」已超出 ");
                                if (!(entity.getStoreroomId().equals("") || entity.getStoreroomId().equals("$EMPTY_CELL$"))) {
                                    message1.append("“物品编号”为「").append(entity.getStoreroomName()).append("」");
                                }
                                if (!(entity.getStoreroomName().equals("") || entity.getStoreroomName().equals("$EMPTY_CELL$"))) {
                                    message1.append("“物品名称”为「").append(entity.getStoreroomName()).append("」");
                                }
                                message1.append("的这个物品剩余库存，（库存剩余：").append(amountDistBefore).append("）");
                                errorFields.add(new ExcelErrorField(
                                        7,
                                        entity.getAmount().toString(),
                                        "分配数量",
                                        message1.toString()));
                                onError(sheetIndex, rowIndex, errorFields);
                                return;
                            } else if (compare == 0) {
                                storeroom.setStatus("1");
                            }
                            // 运行到此代码段的情况必定为 compare == 0 || compare == 1
                            storeroomService.updateStoreroom(storeroom);
                        }

                        // 数据校验成功时，把相关数据加入集合
                        wcStoreroom.setAmount(entity.getAmount());
                        wcStoreroom.setStoreroomId(storeroom.getId());
                        wcStoreroom.setStoreroomName(storeroom.getName());
                        data.add(wcStoreroom);


                    } else { // 以下是只填写了物品名称的导入操作
                        System.out.println("isId为false");
                        // 初始化同名物品的所有剩余数量
                        BigDecimal amount = BigDecimal.ZERO;
                        // 循环完后，amount就是这个物品名称的所有数量之和
                        for (Storeroom value : storeroomsByName) {
                            // 查询是否有单位转换
                            UnitConversion unitConversion;
                            if (!unitConversions.containsKey(value.getId().toString())) {
                                // 判断如果物品id不为空就查询，如果物品名称不为空就查询
                                unitConversion = unitConversionService.findById(value.getId().toString());
                                unitConversions.put(value.getId().toString(), unitConversion);
                            } else {
                                unitConversion = unitConversions.get(value.getId().toString());
                            }
                            // UnitConversion unitConversion = unitConversionService.findById(value.getId().toString());
                            if (unitConversion != null) {
                                // 把查询到的单位转换信息set到自身里，这样下次循环的时候就不用再全都查一遍了
                                value.setUnitConversion(unitConversion);
                                // 把剩余库存数量累加到amount上
                                amount = amount.add(unitConversion.getAmountDist());
                            } else {
                                // 把剩余库存数量累加到amount上
                                amount = amount.add(value.getAmountDist());
                            }
                        }
                        // 把总共剩余分配的数量 - 要分配的数量 赋值给amountDist
                        BigDecimal amountDist = amount.subtract(entity.getAmount());
                        int compare = amountDist.compareTo(BigDecimal.ZERO);
                        if (compare < 0) { // compare == -1
                            List<ExcelErrorField> errorFields = new ArrayList<>();
                            errorFields.add(new ExcelErrorField(
                                    7,
                                    entity.getAmount().toString(),
                                    "分配数量",
                                    "「" + entity.getAmount() + "」分配数量已超出「" + entity.getStoreroomName()
                                            + "」这个物品剩余的库存（库存剩余：" + amount + "）"));
                            onError(sheetIndex, rowIndex, errorFields);
                            // return;
                        } else { // compare == 0 || compare == 1
                            // 到目前代码段可得知：
                            // 总共剩余分配的数量 >= 要分配的数量；
                            // 总共剩余分配的数量为amount；

                            // 一直循环到要分配的数量为0
                            for (; !(entity.getAmount().compareTo(BigDecimal.ZERO) == 0); ) {
                                Storeroom value = storeroomsByName.get(0); // 把上面那次循环存的unitConversion掏出来
                                UnitConversion unitConversion = value.getUnitConversion();
                                if (unitConversion != null) { // 以下是有单位转换的
                                    // 此物品剩余分配的数量
                                    BigDecimal amountDistBefore = unitConversion.getAmountDist();
                                    // 要分配的总数量
                                    BigDecimal amountBefore = entity.getAmount();
                                    entity.setAmount(amountDistBefore.subtract(amountBefore));
                                    int isBigger = amountDistBefore.compareTo(amountBefore);

                                    if (isBigger == 0) { // 此物品剩余分配的数量 = 要分配的总数量

                                        System.out.println(amountDistBefore + " = " + amountBefore);

                                        // 设置要分配的总数量为0
                                        entity.setAmount(BigDecimal.ZERO);
                                        // 更新分配状态为已分配
                                        value.setStatus("1");
                                        storeroomService.updateStoreroom(value);
                                        // 删除此条物品，因为剩余库存数量已经分配完了
                                        storeroomsByName.remove(0);
                                        // 更新单位转换剩余数量为0
                                        unitConversion.setAmountDist(BigDecimal.ZERO);
                                        unitConversionService.saveOrUpdateUnitConversion(unitConversion);
                                        // 设置分配数量为 要分配的总数量
                                        wcStoreroom.setAmount(amountBefore);
                                        System.out.println("设置分配数量为" + amountBefore);


                                    } else if (isBigger > 0) { // 此物品剩余分配的数量 > 要分配的总数量  （isBigger == 1）

                                        System.out.println(amountDistBefore + " > " + amountBefore);

                                        // 设置要分配的总数量为0
                                        entity.setAmount(BigDecimal.ZERO);
                                        // 更新单位转换剩余数量 为 此物品剩余分配的数量 - 要分配的总数量
                                        unitConversion.setAmountDist(amountDistBefore.subtract(amountBefore));
                                        unitConversionService.saveOrUpdateUnitConversion(unitConversion);
                                        // 设置分配数量为 要分配的总数量
                                        wcStoreroom.setAmount(amountBefore);
                                        System.out.println("设置分配数量为" + amountBefore);

                                        //value.setUnitConversion(unitConversion);
                                        //storeroomsByName.set(0, value);


                                    } else { // 此物品剩余分配的数量 < 要分配的总数量 (isBigger == -1)

                                        System.out.println(amountDistBefore + " < " + amountBefore);

                                        // 设置要分配的总数量为 要分配的总数量 - 此物品剩余分配的数量
                                        entity.setAmount(amountBefore.subtract(amountDistBefore));
                                        // 更新分配状态为已分配
                                        value.setStatus("1");
                                        storeroomService.updateStoreroom(value);
                                        // 删除此条物品，因为剩余库存数量已经分配完了
                                        storeroomsByName.remove(0);
                                        // 更新单位转换剩余数量为0
                                        unitConversion.setAmountDist(BigDecimal.ZERO);
                                        unitConversionService.saveOrUpdateUnitConversion(unitConversion);
                                        // 设置分配数量为 此物品剩余分配的数量
                                        wcStoreroom.setAmount(amountDistBefore);
                                        System.out.println("设置分配数量为" + amountDistBefore);


                                    }

                                } else { // 以下是没单位转换的
                                    // 此物品剩余分配的数量
                                    BigDecimal amountDistBefore = value.getAmountDist();
                                    // 要分配的总数量
                                    BigDecimal amountBefore = entity.getAmount();
                                    int isBigger = amountDistBefore.compareTo(amountBefore);

                                    if (isBigger == 0) { // 此物品剩余分配的数量 = 要分配的总数量

                                        System.out.println(amountDistBefore + " = " + amountBefore);

                                        // 设置要分配的总数量为0
                                        entity.setAmount(BigDecimal.ZERO);
                                        // 更新 物资剩余数量为0，分配状态为已分配
                                        value.setAmountDist(BigDecimal.ZERO);
                                        value.setStatus("1");
                                        storeroomService.updateStoreroom(value);
                                        // 删除此条物品，因为剩余库存数量已经分配完了
                                        storeroomsByName.remove(0);
                                        // 设置分配数量为 要分配的总数量
                                        wcStoreroom.setAmount(amountBefore);
                                        System.out.println("设置分配数量为" + amountBefore);


                                    } else if (isBigger > 0) { // 此物品剩余分配的数量 > 要分配的总数量  （isBigger == 1）\

                                        System.out.println(amountDistBefore + " > " + amountBefore);

                                        // 设置要分配的总数量为0
                                        entity.setAmount(BigDecimal.ZERO);
                                        // 更新物资剩余数量 为 此物品剩余分配的数量 - 要分配的总数量
                                        value.setAmountDist(amountDistBefore.subtract(amountBefore));
                                        storeroomService.updateStoreroom(value);
                                        // 设置分配数量为 要分配的总数量
                                        wcStoreroom.setAmount(amountBefore);
                                        System.out.println("设置分配数量为" + amountBefore);

                                        //storeroomsByName.set(0, value);


                                    } else { // 此物品剩余分配的数量 < 要分配的总数量 (isBigger == -1)

                                        System.out.println(amountDistBefore + " < " + amountBefore);

                                        // 设置要分配的总数量为 要分配的总数量 - 此物品剩余分配的数量
                                        entity.setAmount(amountBefore.subtract(amountDistBefore));
                                        // 更新物资剩余数量为0
                                        value.setAmountDist(BigDecimal.ZERO);
                                        value.setStatus("1");
                                        storeroomService.updateStoreroom(value);
                                        // 删除此条物品，因为剩余库存数量已经分配完了
                                        storeroomsByName.remove(0);
                                        // 设置分配数量为 此物品剩余分配的数量
                                        wcStoreroom.setAmount(amountDistBefore);
                                        System.out.println("设置分配数量为" + amountDistBefore);


                                    }

                                }


                                // System.out.println("value.getId() = " + value.getId());
                                // 数据校验成功时，加入集合
                                WcStoreroom copyWcStoreroom = SerializationUtils.clone(wcStoreroom);
                                wcStoreroom.setStoreroomId(value.getId());
                                wcStoreroom.setStoreroomName(value.getName());
                                data.add(wcStoreroom);
                                wcStoreroom = copyWcStoreroom;
                                wcStoreroom.setWcName(wc.getWcName());
                                wcStoreroom.setWcNum(wc.getWcNum());

                            }

                        }

                    }

                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                System.out.println("将合法的记录批量入库");
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
