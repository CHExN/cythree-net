package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author CHExN
 */
@Data
@TableName("cy_wage")
@Excel("工资表")
public class Wage implements Serializable {

    private static final long serialVersionUID = 810793371144236410L;

    /**
     * 工资表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ExcelField(value = "序号")
    private transient Integer sortNum;

    @TableField("INSIDE_OR_OUTSIDE")
    @ExcelField(value = "编制类别", required = true, readConverterExp = "编内=0,编外=1", writeConverterExp = "0=编内,1=编外")
    private String insideOrOutside;

    /**
     * 人员id
     */
    @TableField("STAFF_ID")
    private Long staffId;

    @ExcelField(value = "姓名")
    private transient String staffName;

    @ExcelField(value = "证照号码")
    private transient String staffIdCard;

    @ExcelField(value = "岗位级别")
    private transient String postLevel;

    @TableField("CURRENT_INCOME")
    private BigDecimal currentIncome;
    @ExcelField(value = "岗位工资")
    private transient BigDecimal currentIncomeSum;

    @TableField("REISSUE_SALARY_SCALE")
    private BigDecimal reissueSalaryScale;
    @ExcelField(value = "补发薪级")
    private transient BigDecimal reissueSalaryScaleSum;

    @TableField("SALARY_SALARY")
    private BigDecimal salarySalary;
    @ExcelField(value = "薪级工资")
    private transient BigDecimal salarySalarySum;

    @TableField("POST_ALLOWANCE")
    private BigDecimal postAllowance;
    @ExcelField(value = "岗位津贴")
    private transient BigDecimal postAllowanceSum;

    @TableField("JOB_PERFORMANCE")
    private BigDecimal jobPerformance;
    @ExcelField(value = "岗位绩效")
    private transient BigDecimal jobPerformanceSum;

    @TableField("FINANCIAL_BURDEN_PERFORMANCE_PAY")
    private BigDecimal financialBurdenPerformancePay;
    @ExcelField(value = "财政负担")
    private transient BigDecimal financialBurdenPerformancePaySum;

    @TableField("OVERTIME_PAY")
    private BigDecimal overtimePay;
    @ExcelField(value = "加班费")
    private transient BigDecimal overtimePaySum;

    @TableField("ENVIRONMENTAL_SANITATION_DUTY_ALLOWANCE")
    private BigDecimal environmentalSanitationDutyAllowance;
    @ExcelField(value = "环卫危害岗位补助")
    private transient BigDecimal environmentalSanitationDutyAllowanceSum;

    @TableField("HOUSING_SUBSIDY")
    private BigDecimal housingSubsidy;
    @ExcelField(value = "住房补贴")
    private transient BigDecimal housingSubsidySum;

    @TableField("ONLY_CHILD_FEE")
    private BigDecimal onlyChildFee;
    @ExcelField(value = "独生子女费")
    private transient BigDecimal onlyChildFeeSum;

    @TableField("TEMPORARY_SUBSIDY")
    private BigDecimal temporarySubsidy;
    @ExcelField(value = "临时性补贴")
    private transient BigDecimal temporarySubsidySum;

    @TableField("HOLIDAY_COSTS")
    private BigDecimal holidayCosts;
    @ExcelField(value = "过节费")
    private transient BigDecimal holidayCostsSum;

    @TableField("ANNUAL_LEAVE_PAY")
    private BigDecimal annualLeavePay;
    @ExcelField(value = "年休假工资报酬")
    private transient BigDecimal annualLeavePaySum;

    @TableField("COMPREHENSIVE_SUBSIDY")
    private BigDecimal comprehensiveSubsidy;
    @ExcelField(value = "综合补助")
    private transient BigDecimal comprehensiveSubsidySum;

    @TableField("PAYABLE")
    private BigDecimal payable;
    @ExcelField(value = "应发工资")
    private transient BigDecimal payableSum;

    @TableField("HOUSING_FUND")
    private BigDecimal housingFund;
    @ExcelField(value = "住房公积金")
    private transient BigDecimal housingFundSum;

    /**
     * Basic pension insurance premium 基本养老保险费
     */
    @TableField("BASIC_PENSION_IP")
    private BigDecimal basicPensionIp;
    @ExcelField(value = "养老保险")
    private transient BigDecimal basicPensionIpSum;

    /**
     * Unemployment insurance premium 失业保险费
     */
    @TableField("UNEMPLOYMENT_IP")
    private BigDecimal unemploymentIp;
    @ExcelField(value = "失业保险")
    private transient BigDecimal unemploymentIpSum;

    /**
     * Basic medical insurance premium 基本医疗保险费
     */
    @TableField("BASIC_MEDICAL_IP")
    private BigDecimal basicMedicalIp;
    @ExcelField(value = "医疗保险")
    private transient BigDecimal basicMedicalIpSum;

    @TableField("MEDICAL_MUTUAL_AID")
    private BigDecimal medicalMutualAid;
    @ExcelField("医疗互助")
    private transient BigDecimal medicalMutualAidSum;

    /**
     * Corporate (career) annuity 企业(职业)年金
     */
    @TableField("CORPORATE_ANNUITY")
    private BigDecimal corporateAnnuity;
    @ExcelField(value = "职业年金")
    private transient BigDecimal corporateAnnuitySum;

    @TableField("TAX_DEDUCTION")
    private BigDecimal taxDeduction;
    @ExcelField(value = "扣税")
    private transient BigDecimal taxDeductionSum;

    @TableField("REAL_WAGE")
    private BigDecimal realWage;
    @ExcelField(value = "实发工资")
    private transient BigDecimal realWageSum;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private String modifyTime;

    /**
     * 年
     */
    @TableField("YEAR")
    @ExcelField(value = "年份")
    private String year;

    /**
     * 月
     */
    @TableField("MONTH")
    @ExcelField(value = "月份")
    private String month;
    private transient String monthArr;

    private String yearForm;
    private String monthForm;
    private String yearTo;
    private String monthTo;

}
