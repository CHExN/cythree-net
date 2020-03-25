package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.domain.Wage;
import cc.mrbird.febs.chaoyang3team.domain.WageImport;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.chaoyang3team.service.WageRemarkService;
import cc.mrbird.febs.chaoyang3team.service.WageService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@RequestMapping("wage")
public class WageController extends BaseController {

    private String message;

    @Autowired
    private WageService wageService;
    @Autowired
    private StaffInsideService staffInsideService;
    @Autowired
    private StaffOutsideService staffOutsideService;
    @Autowired
    private WageRemarkService wageRemarkService;

    @GetMapping("oneInfo")
    @RequiresPermissions("wage:view")
    public List<Wage> getWageInfoList(Wage wage) {
        return this.wageService.getWageInfoList(wage);
    }

    @GetMapping
    @RequiresPermissions("wage:view")
    public Map<String, Object> WageList(QueryRequest request, Wage wage) {
        return getDataTable(this.wageService.findWageDetail(request, wage));
    }

    @Log("新增工资信息")
    @PostMapping
    @RequiresPermissions("wage:add")
    public void addWage(@Valid Wage wage) throws FebsException {
        try {
            this.wageService.createWage(wage);
        } catch (Exception e) {
            message = "新增工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改工资信息")
    @PutMapping
    @RequiresPermissions("wage:update")
    public void updateWage(@Valid Wage wage) throws FebsException {
        try {
            this.wageService.updateWage(wage);
        } catch (Exception e) {
            message = "修改工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除工资信息")
    @DeleteMapping("/{wageIds}")
    @RequiresPermissions("wage:delete")
    public void deleteWages(@NotBlank(message = "{required}") @PathVariable String wageIds) throws FebsException {
        try {
            String[] ids = wageIds.split(StringPool.COMMA);
            this.wageService.deleteWage(ids);
        } catch (Exception e) {
            message = "删除工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("wage:export")
    public void export(QueryRequest request, Wage wage, HttpServletResponse response) throws FebsException {
        try {
            // 本来是想在导出的时候，列名也跟着备注来，但是最后发现没法改注解，所以就不做了。。。
            /*WageRemark wageRemark = wageRemarkService.getOneWageRemark(
                    new WageRemark(null, wage.getInsideOrOutside(), wage.getYear(), wage.getMonth(), null, null));

            if (wageRemark != null && StringUtils.isNotBlank(wageRemark.getRemark())) {
                String[] remarkSplit = wageRemark.getRemark().split(",");
                for (int i = 0; i < remarkSplit.length; ++i) {
                    // 如果是空就跳过
                    if (remarkSplit[i].equals("") || remarkSplit[i] == null) continue;
                    Class<? extends Wage> clazz = wage.getClass();
                    // 获取要修改的字段
                    Field field = clazz.getDeclaredField("emptyColumn0" + (i + 1) + "Sum");
                    field.setAccessible(true);
                    // 获取要修改字段上的ExcelField注解实例
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    // 获取 excelField 这个代理实例所持有的 InvocationHandler
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(excelField);
                    // 获取 AnnotationInvocationHandler 的 memberValues 字段
                    Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
                    // 因为这个字段事 private final 修饰，所以要打开权限
                    declaredField.setAccessible(true);
                    // 获取 memberValues
                    Map<String, Object> memberValues = (Map<String, Object>) declaredField.get(invocationHandler);
                    // 修改 value 属性值
                    memberValues.put("value", remarkSplit[i]);
                    ExcelField excelField2 = field.getAnnotation(ExcelField.class);
                    System.out.println(excelField2);
                }
            }*/

            IPage<Wage> wageDetail = this.wageService.findWageDetail(request, wage);
            if (wageDetail != null) {
                List<Wage> wages = wageDetail.getRecords();
                ExcelKit.$Export(Wage.class, response).downXlsx(wages, false);
            }
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
        List<WageImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            WageImport wage = new WageImport();
            wage.setStaffName("此项不用必须填写，只是作为参照使用");
            wage.setStaffIdCard("证照号码" + (i + 1));
            wage.setCurrentIncome("0");
            wage.setReissueSalaryScale("0");
            wage.setSalarySalary("0");
            wage.setPostAllowance("0");
            wage.setFinancialBurdenPerformancePay("0");
            wage.setOvertimePay("0");
            wage.setEnvironmentalSanitationDutyAllowance("0");
            wage.setHousingSubsidy("0");
            wage.setOnlyChildFee("0");
            wage.setTemporarySubsidy("0");
            wage.setJobPerformance("0");
            wage.setHolidayCosts("0");
            wage.setAnnualLeavePay("0");
            wage.setComprehensiveSubsidy("0");
            wage.setPayable("0");
            wage.setHousingFund("0");
            wage.setBasicPensionIp("0");
            wage.setUnemploymentIp("0");
            wage.setBasicMedicalIp("0");
            wage.setMedicalMutualAid("0");
            wage.setCorporateAnnuity("0");
            wage.setTaxDeduction("0");
            wage.setRealWage("0");
            wage.setEmptyColumn01("0");
            wage.setEmptyColumn02("0");
            wage.setEmptyColumn03("0");
            wage.setEmptyColumn04("0");
            wage.setEmptyColumn05("0");
            list.add(wage);
        });
        // 构建模板
        ExcelKit.$Export(WageImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入工资信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("wage:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, String insideOrOutside, String date) throws FebsException {
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
            final List<Wage> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            String dateArr[] = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            BigDecimal zero = BigDecimal.valueOf(0);
            ExcelKit.$Import(WageImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<WageImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, WageImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = null;
                    StaffInside staffInside = null;
                    if (insideOrOutside.equals("0")) {
                        staffInside = staffInsideService.getStaffIdByIdNum(entity.getStaffIdCard().trim());
                    } else {
                        staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getStaffIdCard().trim());
                    }
                    if (staffInside == null && staffOutside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getStaffIdCard().trim(),
                                "证照号码",
                                "查询不到 [" + entity.getStaffName() + "] 这个编" + (insideOrOutside.equals("0") ? "内" : "外") + "人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        Wage wage = new Wage();
                        wage.setInsideOrOutside(insideOrOutside.equals("0") + "");
                        wage.setStaffId(staffInside == null ? staffOutside.getStaffId() : staffInside.getStaffId());
                        wage.setStaffName(staffInside == null ? staffOutside.getName() : staffInside.getName());
                        wage.setStaffIdCard(entity.getStaffIdCard().trim());
                        wage.setCurrentIncome(entity.getCurrentIncome().equals("$EMPTY_CELL$") || entity.getCurrentIncome().equals("") ? zero : new BigDecimal(entity.getCurrentIncome()));
                        wage.setReissueSalaryScale(entity.getReissueSalaryScale().equals("$EMPTY_CELL$") || entity.getReissueSalaryScale().equals("") ? zero : new BigDecimal(entity.getReissueSalaryScale()));
                        wage.setSalarySalary(entity.getSalarySalary().equals("$EMPTY_CELL$") || entity.getSalarySalary().equals("") ? zero : new BigDecimal(entity.getSalarySalary()));
                        wage.setPostAllowance(entity.getPostAllowance().equals("$EMPTY_CELL$") || entity.getPostAllowance().equals("") ? zero : new BigDecimal(entity.getPostAllowance()));
                        wage.setFinancialBurdenPerformancePay(entity.getFinancialBurdenPerformancePay().equals("$EMPTY_CELL$") || entity.getFinancialBurdenPerformancePay().equals("") ? zero : new BigDecimal(entity.getFinancialBurdenPerformancePay()));
                        wage.setOvertimePay(entity.getOvertimePay().equals("$EMPTY_CELL$") || entity.getOvertimePay().equals("") ? zero : new BigDecimal(entity.getOvertimePay()));
                        wage.setEnvironmentalSanitationDutyAllowance(entity.getEnvironmentalSanitationDutyAllowance().equals("$EMPTY_CELL$") || entity.getEnvironmentalSanitationDutyAllowance().equals("") ? zero : new BigDecimal(entity.getEnvironmentalSanitationDutyAllowance()));
                        wage.setHousingSubsidy(entity.getHousingSubsidy().equals("$EMPTY_CELL$") || entity.getHousingSubsidy().equals("") ? zero : new BigDecimal(entity.getHousingSubsidy()));
                        wage.setOnlyChildFee(entity.getOnlyChildFee().equals("$EMPTY_CELL$") || entity.getOnlyChildFee().equals("") ? zero : new BigDecimal(entity.getOnlyChildFee()));
                        wage.setTemporarySubsidy(entity.getTemporarySubsidy().equals("$EMPTY_CELL$") || entity.getTemporarySubsidy().equals("") ? zero : new BigDecimal(entity.getTemporarySubsidy()));
                        wage.setJobPerformance(entity.getJobPerformance().equals("$EMPTY_CELL$") || entity.getJobPerformance().equals("") ? zero : new BigDecimal(entity.getJobPerformance()));
                        wage.setHolidayCosts(entity.getHolidayCosts().equals("$EMPTY_CELL$") || entity.getHolidayCosts().equals("") ? zero : new BigDecimal(entity.getHolidayCosts()));
                        wage.setAnnualLeavePay(entity.getAnnualLeavePay().equals("$EMPTY_CELL$") || entity.getAnnualLeavePay().equals("") ? zero : new BigDecimal(entity.getAnnualLeavePay()));
                        wage.setComprehensiveSubsidy(entity.getComprehensiveSubsidy().equals("$EMPTY_CELL$") || entity.getComprehensiveSubsidy().equals("") ? zero : new BigDecimal(entity.getComprehensiveSubsidy()));
                        wage.setPayable(entity.getPayable().equals("$EMPTY_CELL$") || entity.getPayable().equals("") ? zero : new BigDecimal(entity.getPayable()));
                        wage.setHousingFund(entity.getHousingFund().equals("$EMPTY_CELL$") || entity.getHousingFund().equals("") ? zero : new BigDecimal(entity.getHousingFund()));
                        wage.setBasicPensionIp(entity.getBasicPensionIp().equals("$EMPTY_CELL$") || entity.getBasicPensionIp().equals("") ? zero : new BigDecimal(entity.getBasicPensionIp()));
                        wage.setUnemploymentIp(entity.getUnemploymentIp().equals("$EMPTY_CELL$") || entity.getUnemploymentIp().equals("") ? zero : new BigDecimal(entity.getUnemploymentIp()));
                        wage.setBasicMedicalIp(entity.getBasicMedicalIp().equals("$EMPTY_CELL$") || entity.getBasicMedicalIp().equals("") ? zero : new BigDecimal(entity.getBasicMedicalIp()));
                        wage.setMedicalMutualAid(entity.getMedicalMutualAid().equals("$EMPTY_CELL$") || entity.getMedicalMutualAid().equals("") ? zero : new BigDecimal(entity.getMedicalMutualAid()));
                        wage.setCorporateAnnuity(entity.getCorporateAnnuity().equals("$EMPTY_CELL$") || entity.getCorporateAnnuity().equals("") ? zero : new BigDecimal(entity.getCorporateAnnuity()));
                        wage.setTaxDeduction(entity.getTaxDeduction().equals("$EMPTY_CELL$") || entity.getTaxDeduction().equals("") ? zero : new BigDecimal(entity.getTaxDeduction()));
                        wage.setRealWage(entity.getRealWage().equals("$EMPTY_CELL$") || entity.getRealWage().equals("") ? zero : new BigDecimal(entity.getRealWage()));
                        wage.setRealWage(entity.getEmptyColumn01().equals("$EMPTY_CELL$") || entity.getEmptyColumn01().equals("") ? zero : new BigDecimal(entity.getEmptyColumn01()));
                        wage.setRealWage(entity.getEmptyColumn02().equals("$EMPTY_CELL$") || entity.getEmptyColumn02().equals("") ? zero : new BigDecimal(entity.getEmptyColumn02()));
                        wage.setRealWage(entity.getEmptyColumn03().equals("$EMPTY_CELL$") || entity.getEmptyColumn03().equals("") ? zero : new BigDecimal(entity.getEmptyColumn03()));
                        wage.setRealWage(entity.getEmptyColumn04().equals("$EMPTY_CELL$") || entity.getEmptyColumn04().equals("") ? zero : new BigDecimal(entity.getEmptyColumn04()));
                        wage.setRealWage(entity.getEmptyColumn05().equals("$EMPTY_CELL$") || entity.getEmptyColumn05().equals("") ? zero : new BigDecimal(entity.getEmptyColumn05()));
                        wage.setCreateTime(now.toString());
                        wage.setYear(dateArr[0]);
                        wage.setMonth(dateArr[1]);
                        data.add(wage);
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
                this.wageService.batchInsertWage(data);
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
