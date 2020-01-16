package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.domain.Wage;
import cc.mrbird.febs.chaoyang3team.domain.WageImport;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
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
        IPage<Wage> wageDetail = this.wageService.findWageDetail(request, wage);
        try {
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
            wage.setInsideOrOutside(0);
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
            String dateArr[] = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            BigDecimal zero = BigDecimal.valueOf(0);
            ExcelKit.$Import(WageImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<WageImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, WageImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = null;
                    StaffInside staffInside = null;
                    if (entity.getInsideOrOutside() == 0) {
                        staffInside = staffInsideService.getStaffIdByIdNum(entity.getStaffIdCard());
                    } else {
                        staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getStaffIdCard());
                    }
                    if (staffInside == null && staffOutside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getStaffIdCard(),
                                "证照号码",
                                "查询不到此编" + (entity.getInsideOrOutside() == 1 ? "内" : "外") + "人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        Wage wage = new Wage();
                        wage.setInsideOrOutside(entity.getInsideOrOutside() + "");
                        wage.setStaffId(staffInside == null ? staffOutside.getStaffId() : staffInside.getStaffId());
                        wage.setStaffName(staffInside == null ? staffOutside.getName() : staffInside.getName());
                        wage.setStaffIdCard(entity.getStaffIdCard());
                        wage.setCurrentIncome(entity.getCurrentIncome().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCurrentIncome()));
                        wage.setReissueSalaryScale(entity.getReissueSalaryScale().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getReissueSalaryScale()));
                        wage.setSalarySalary(entity.getSalarySalary().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getSalarySalary()));
                        wage.setPostAllowance(entity.getPostAllowance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getPostAllowance()));
                        wage.setFinancialBurdenPerformancePay(entity.getFinancialBurdenPerformancePay().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getFinancialBurdenPerformancePay()));
                        wage.setOvertimePay(entity.getOvertimePay().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getOvertimePay()));
                        wage.setEnvironmentalSanitationDutyAllowance(entity.getEnvironmentalSanitationDutyAllowance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getEnvironmentalSanitationDutyAllowance()));
                        wage.setHousingSubsidy(entity.getHousingSubsidy().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getHousingSubsidy()));
                        wage.setOnlyChildFee(entity.getOnlyChildFee().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getOnlyChildFee()));
                        wage.setTemporarySubsidy(entity.getTemporarySubsidy().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getTemporarySubsidy()));
                        wage.setJobPerformance(entity.getJobPerformance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getJobPerformance()));
                        wage.setHolidayCosts(entity.getHolidayCosts().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getHolidayCosts()));
                        wage.setAnnualLeavePay(entity.getAnnualLeavePay().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getAnnualLeavePay()));
                        wage.setComprehensiveSubsidy(entity.getComprehensiveSubsidy().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getComprehensiveSubsidy()));
                        wage.setPayable(entity.getPayable().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getPayable()));
                        wage.setHousingFund(entity.getHousingFund().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getHousingFund()));
                        wage.setBasicPensionIp(entity.getBasicPensionIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getBasicPensionIp()));
                        wage.setUnemploymentIp(entity.getUnemploymentIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getUnemploymentIp()));
                        wage.setBasicMedicalIp(entity.getBasicMedicalIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getBasicMedicalIp()));
                        wage.setMedicalMutualAid(entity.getMedicalMutualAid().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getMedicalMutualAid()));
                        wage.setCorporateAnnuity(entity.getCorporateAnnuity().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCorporateAnnuity()));
                        wage.setTaxDeduction(entity.getTaxDeduction().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getTaxDeduction()));
                        wage.setRealWage(entity.getRealWage().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getRealWage()));
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
