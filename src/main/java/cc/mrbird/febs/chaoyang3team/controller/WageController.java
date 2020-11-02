package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.Wage;
import cc.mrbird.febs.chaoyang3team.domain.WageImport;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
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
    /*@Autowired
    private WageRemarkService wageRemarkService;*/

    @GetMapping("oneInfo")
    @RequiresPermissions("wage:view")
    public List<Wage> getWageInfoList(Wage wage) {
        return this.wageService.getWageInfoList(wage);
    }

    @GetMapping
    @RequiresPermissions("wage:view")
    public Map<String, Object> wageList(QueryRequest request, Wage wage) {
        return getDataTable(this.wageService.findWageDetail(request, wage));
    }

    @Log("新增编内工资信息")
    @PostMapping
    @RequiresPermissions("wage:add")
    public void addWage(@Valid Wage wage) throws FebsException {
        try {
            this.wageService.createWage(wage);
        } catch (Exception e) {
            message = "新增编内工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编内工资信息")
    @PutMapping
    @RequiresPermissions("wage:update")
    public void updateWage(@Valid Wage wage) throws FebsException {
        try {
            this.wageService.updateWage(wage);
        } catch (Exception e) {
            message = "修改编内工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编内工资信息")
    @DeleteMapping("/{wageIds}")
    @RequiresPermissions("wage:delete")
    public void deleteWages(@NotBlank(message = "{required}") @PathVariable String wageIds) throws FebsException {
        try {
            String[] ids = wageIds.split(StringPool.COMMA);
            this.wageService.deleteWage(ids);
        } catch (Exception e) {
            message = "删除编内工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除本月全部编内工资信息")
    @DeleteMapping("deleteAll")
    @RequiresPermissions("wage:delete")
    public void deleteAllWages(String year, String month) throws FebsException {
        try {
            this.wageService.deleteAllWage(year, month);
        } catch (Exception e) {
            message = "删除本月全部编内工资信息失败";
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
            wage.setPayable("不用填写，自动计算");
            wage.setHousingFund("0");
            wage.setBasicPensionIp("0");
            wage.setUnemploymentIp("0");
            wage.setBasicMedicalIp("0");
            wage.setMedicalMutualAid("0");
            wage.setCorporateAnnuity("0");
            wage.setTaxDeduction("0");
            wage.setRealWage("不用填写，自动计算");
            wage.setEmptyColumn01("0");
            wage.setEmptyColumn02("0");
            wage.setEmptyColumn03("0");
            wage.setEmptyColumn04("0");
            wage.setEmptyColumn05("0");
            wage.setEmptyColumn06("0");
            wage.setEmptyColumn07("0");
            wage.setEmptyColumn08("0");
            wage.setEmptyColumn09("0");
            wage.setEmptyColumn10("0");
            list.add(wage);
        });
        // 构建模板
        ExcelKit.$Export(WageImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入编内工资信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("wage:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, String date) throws FebsException {
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
            String[] dateArr = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            BigDecimal zero = BigDecimal.valueOf(0);
            ExcelKit.$Import(WageImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<WageImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, WageImport entity) {
                    // 数据校验成功时，加入集合
                    StaffInside staffInside = staffInsideService.getStaffIdByIdNum(entity.getStaffIdCard().trim());
                    if (staffInside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getStaffIdCard().trim(),
                                "证照号码",
                                "查询不到 [" + entity.getStaffName() + "] 这个编内人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        Wage wage = new Wage();
                        wage.setStaffId(staffInside.getStaffId());
                        wage.setStaffName(staffInside.getName());
                        wage.setStaffIdCard(entity.getStaffIdCard().trim());
                        wage.setCurrentIncome(StringUtils.isBlank(entity.getCurrentIncome()) || entity.getCurrentIncome().equals("$EMPTY_CELL$") || entity.getCurrentIncome().equals("") ? zero : new BigDecimal(entity.getCurrentIncome()));
                        wage.setReissueSalaryScale(StringUtils.isBlank(entity.getReissueSalaryScale()) || entity.getReissueSalaryScale().equals("$EMPTY_CELL$") || entity.getReissueSalaryScale().equals("") ? zero : new BigDecimal(entity.getReissueSalaryScale()));
                        wage.setSalarySalary(StringUtils.isBlank(entity.getSalarySalary()) || entity.getSalarySalary().equals("$EMPTY_CELL$") || entity.getSalarySalary().equals("") ? zero : new BigDecimal(entity.getSalarySalary()));
                        wage.setPostAllowance(StringUtils.isBlank(entity.getPostAllowance()) || entity.getPostAllowance().equals("$EMPTY_CELL$") || entity.getPostAllowance().equals("") ? zero : new BigDecimal(entity.getPostAllowance()));
                        wage.setFinancialBurdenPerformancePay(StringUtils.isBlank(entity.getFinancialBurdenPerformancePay()) || entity.getFinancialBurdenPerformancePay().equals("$EMPTY_CELL$") || entity.getFinancialBurdenPerformancePay().equals("") ? zero : new BigDecimal(entity.getFinancialBurdenPerformancePay()));
                        wage.setOvertimePay(StringUtils.isBlank(entity.getOvertimePay()) || entity.getOvertimePay().equals("$EMPTY_CELL$") || entity.getOvertimePay().equals("") ? zero : new BigDecimal(entity.getOvertimePay()));
                        wage.setEnvironmentalSanitationDutyAllowance(StringUtils.isBlank(entity.getEnvironmentalSanitationDutyAllowance()) || entity.getEnvironmentalSanitationDutyAllowance().equals("$EMPTY_CELL$") || entity.getEnvironmentalSanitationDutyAllowance().equals("") ? zero : new BigDecimal(entity.getEnvironmentalSanitationDutyAllowance()));
                        wage.setHousingSubsidy(StringUtils.isBlank(entity.getHousingSubsidy()) || entity.getHousingSubsidy().equals("$EMPTY_CELL$") || entity.getHousingSubsidy().equals("") ? zero : new BigDecimal(entity.getHousingSubsidy()));
                        wage.setOnlyChildFee(StringUtils.isBlank(entity.getOnlyChildFee()) || entity.getOnlyChildFee().equals("$EMPTY_CELL$") || entity.getOnlyChildFee().equals("") ? zero : new BigDecimal(entity.getOnlyChildFee()));
                        wage.setTemporarySubsidy(StringUtils.isBlank(entity.getTemporarySubsidy()) || entity.getTemporarySubsidy().equals("$EMPTY_CELL$") || entity.getTemporarySubsidy().equals("") ? zero : new BigDecimal(entity.getTemporarySubsidy()));
                        wage.setJobPerformance(StringUtils.isBlank(entity.getJobPerformance()) || entity.getJobPerformance().equals("$EMPTY_CELL$") || entity.getJobPerformance().equals("") ? zero : new BigDecimal(entity.getJobPerformance()));
                        wage.setHolidayCosts(StringUtils.isBlank(entity.getHolidayCosts()) || entity.getHolidayCosts().equals("$EMPTY_CELL$") || entity.getHolidayCosts().equals("") ? zero : new BigDecimal(entity.getHolidayCosts()));
                        wage.setAnnualLeavePay(StringUtils.isBlank(entity.getAnnualLeavePay()) || entity.getAnnualLeavePay().equals("$EMPTY_CELL$") || entity.getAnnualLeavePay().equals("") ? zero : new BigDecimal(entity.getAnnualLeavePay()));
                        wage.setComprehensiveSubsidy(StringUtils.isBlank(entity.getComprehensiveSubsidy()) || entity.getComprehensiveSubsidy().equals("$EMPTY_CELL$") || entity.getComprehensiveSubsidy().equals("") ? zero : new BigDecimal(entity.getComprehensiveSubsidy()));
                        wage.setEmptyColumn01(StringUtils.isBlank(entity.getEmptyColumn01()) || entity.getEmptyColumn01().equals("$EMPTY_CELL$") || entity.getEmptyColumn01().equals("") ? zero : new BigDecimal(entity.getEmptyColumn01()));
                        wage.setEmptyColumn02(StringUtils.isBlank(entity.getEmptyColumn02()) || entity.getEmptyColumn02().equals("$EMPTY_CELL$") || entity.getEmptyColumn02().equals("") ? zero : new BigDecimal(entity.getEmptyColumn02()));
                        wage.setEmptyColumn03(StringUtils.isBlank(entity.getEmptyColumn03()) || entity.getEmptyColumn03().equals("$EMPTY_CELL$") || entity.getEmptyColumn03().equals("") ? zero : new BigDecimal(entity.getEmptyColumn03()));
                        wage.setEmptyColumn04(StringUtils.isBlank(entity.getEmptyColumn04()) || entity.getEmptyColumn04().equals("$EMPTY_CELL$") || entity.getEmptyColumn04().equals("") ? zero : new BigDecimal(entity.getEmptyColumn04()));
                        wage.setEmptyColumn05(StringUtils.isBlank(entity.getEmptyColumn05()) || entity.getEmptyColumn05().equals("$EMPTY_CELL$") || entity.getEmptyColumn05().equals("") ? zero : new BigDecimal(entity.getEmptyColumn05()));
                        // 应发工资
                        // wage.setPayable(entity.getPayable().equals("$EMPTY_CELL$") || entity.getPayable().equals("") ? zero : new BigDecimal(entity.getPayable()));
                        wage.setPayable(wage.getCurrentIncome()
                                .add(wage.getReissueSalaryScale())
                                .add(wage.getSalarySalary())
                                .add(wage.getPostAllowance())
                                .add(wage.getFinancialBurdenPerformancePay())
                                .add(wage.getOvertimePay())
                                .add(wage.getEnvironmentalSanitationDutyAllowance())
                                .add(wage.getHousingSubsidy())
                                .add(wage.getOnlyChildFee())
                                .add(wage.getTemporarySubsidy())
                                .add(wage.getJobPerformance())
                                .add(wage.getHolidayCosts())
                                .add(wage.getAnnualLeavePay())
                                .add(wage.getComprehensiveSubsidy())
                                .add(wage.getEmptyColumn01())
                                .add(wage.getEmptyColumn02())
                                .add(wage.getEmptyColumn03())
                                .add(wage.getEmptyColumn04())
                                .add(wage.getEmptyColumn05())
                        );
                        System.out.println(staffInside.getName());
                        System.out.println("应发工资");
                        System.out.println(wage.getPayable());

                        wage.setHousingFund(StringUtils.isBlank(entity.getHousingFund()) || entity.getHousingFund().equals("$EMPTY_CELL$") || entity.getHousingFund().equals("") ? zero : new BigDecimal(entity.getHousingFund()));
                        wage.setBasicPensionIp(StringUtils.isBlank(entity.getBasicPensionIp()) || entity.getBasicPensionIp().equals("$EMPTY_CELL$") || entity.getBasicPensionIp().equals("") ? zero : new BigDecimal(entity.getBasicPensionIp()));
                        wage.setUnemploymentIp(StringUtils.isBlank(entity.getUnemploymentIp()) || entity.getUnemploymentIp().equals("$EMPTY_CELL$") || entity.getUnemploymentIp().equals("") ? zero : new BigDecimal(entity.getUnemploymentIp()));
                        wage.setBasicMedicalIp(StringUtils.isBlank(entity.getBasicMedicalIp()) || entity.getBasicMedicalIp().equals("$EMPTY_CELL$") || entity.getBasicMedicalIp().equals("") ? zero : new BigDecimal(entity.getBasicMedicalIp()));
                        wage.setMedicalMutualAid(StringUtils.isBlank(entity.getMedicalMutualAid()) || entity.getMedicalMutualAid().equals("$EMPTY_CELL$") || entity.getMedicalMutualAid().equals("") ? zero : new BigDecimal(entity.getMedicalMutualAid()));
                        wage.setCorporateAnnuity(StringUtils.isBlank(entity.getCorporateAnnuity()) || entity.getCorporateAnnuity().equals("$EMPTY_CELL$") || entity.getCorporateAnnuity().equals("") ? zero : new BigDecimal(entity.getCorporateAnnuity()));
                        wage.setTaxDeduction(StringUtils.isBlank(entity.getTaxDeduction()) || entity.getTaxDeduction().equals("$EMPTY_CELL$") || entity.getTaxDeduction().equals("") ? zero : new BigDecimal(entity.getTaxDeduction()));
                        wage.setEmptyColumn06(StringUtils.isBlank(entity.getEmptyColumn06()) || entity.getEmptyColumn06().equals("$EMPTY_CELL$") || entity.getEmptyColumn06().equals("") ? zero : new BigDecimal(entity.getEmptyColumn06()));
                        wage.setEmptyColumn07(StringUtils.isBlank(entity.getEmptyColumn07()) || entity.getEmptyColumn07().equals("$EMPTY_CELL$") || entity.getEmptyColumn07().equals("") ? zero : new BigDecimal(entity.getEmptyColumn07()));
                        wage.setEmptyColumn08(StringUtils.isBlank(entity.getEmptyColumn08()) || entity.getEmptyColumn08().equals("$EMPTY_CELL$") || entity.getEmptyColumn08().equals("") ? zero : new BigDecimal(entity.getEmptyColumn08()));
                        wage.setEmptyColumn09(StringUtils.isBlank(entity.getEmptyColumn09()) || entity.getEmptyColumn09().equals("$EMPTY_CELL$") || entity.getEmptyColumn09().equals("") ? zero : new BigDecimal(entity.getEmptyColumn09()));
                        wage.setEmptyColumn10(StringUtils.isBlank(entity.getEmptyColumn10()) || entity.getEmptyColumn10().equals("$EMPTY_CELL$") || entity.getEmptyColumn10().equals("") ? zero : new BigDecimal(entity.getEmptyColumn10()));
                        // 实发工资
                        // wage.setRealWage(entity.getRealWage().equals("$EMPTY_CELL$") || entity.getRealWage().equals("") ? zero : new BigDecimal(entity.getRealWage()));
                        wage.setRealWage(wage.getPayable()
                                .subtract(wage.getHousingFund())
                                .subtract(wage.getBasicPensionIp())
                                .subtract(wage.getUnemploymentIp())
                                .subtract(wage.getBasicMedicalIp())
                                .subtract(wage.getMedicalMutualAid())
                                .subtract(wage.getCorporateAnnuity())
                                .subtract(wage.getTaxDeduction())
                                .subtract(wage.getEmptyColumn06())
                                .subtract(wage.getEmptyColumn07())
                                .subtract(wage.getEmptyColumn08())
                                .subtract(wage.getEmptyColumn09())
                                .subtract(wage.getEmptyColumn10())
                        );
                        System.out.println("实发工资");
                        System.out.println(wage.getRealWage());
                        System.out.println(wage);

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
                // 将合法的记录批量插入
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
