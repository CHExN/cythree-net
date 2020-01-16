package cc.mrbird.febs.chaoyang3team.domain;


import cc.mrbird.febs.common.validator.NumericValidator;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@Excel("工资表")
public class WageImport implements Serializable {

    private static final long serialVersionUID = 3526020032109213405L;

    @ExcelField(value = "编制类别", required = true, readConverterExp = "编内=0,编外=1", writeConverterExp = "0=编内,1=编外")
    private Integer insideOrOutside;

    @ExcelField(value = "证照号码", required = true, comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String staffIdCard;

    @ExcelField(value = "岗位工资", required = true, validator = NumericValidator.class)
    private String currentIncome;

    @ExcelField(value = "补发薪级", validator = NumericValidator.class)
    private String reissueSalaryScale;

    @ExcelField(value = "薪级工资", validator = NumericValidator.class)
    private String salarySalary;

    @ExcelField(value = "岗位津贴", validator = NumericValidator.class)
    private String postAllowance;

    @ExcelField(value = "财政负担", validator = NumericValidator.class)
    private String financialBurdenPerformancePay;

    @ExcelField(value = "加班费", validator = NumericValidator.class)
    private String overtimePay;

    @ExcelField(value = "环卫危害岗位补助", validator = NumericValidator.class)
    private String environmentalSanitationDutyAllowance;

    @ExcelField(value = "住房补贴", validator = NumericValidator.class)
    private String housingSubsidy;

    @ExcelField(value = "独生子女费", validator = NumericValidator.class)
    private String onlyChildFee;

    @ExcelField(value = "临时性补贴", validator = NumericValidator.class)
    private String temporarySubsidy;

    @ExcelField(value = "岗位绩效", validator = NumericValidator.class)
    private String jobPerformance;

    @ExcelField(value = "过节费", validator = NumericValidator.class)
    private String holidayCosts;

    @ExcelField(value = "年休假工资报酬", validator = NumericValidator.class)
    private String annualLeavePay;

    @ExcelField(value = "综合补助", validator = NumericValidator.class)
    private String comprehensiveSubsidy;

    @ExcelField(value = "应发工资", validator = NumericValidator.class)
    private String payable;

    @ExcelField(value = "住房公积金", validator = NumericValidator.class)
    private String housingFund;

    /**
     * Basic pension insurance premium 基本养老保险费
     */
    @ExcelField(value = "养老保险", validator = NumericValidator.class)
    private String basicPensionIp;

    /**
     * Unemployment insurance premium 失业保险费
     */
    @ExcelField(value = "失业保险", validator = NumericValidator.class)
    private String unemploymentIp;

    /**
     * Basic medical insurance premium 基本医疗保险费
     */
    @ExcelField(value = "医疗保险", validator = NumericValidator.class)
    private String basicMedicalIp;

    @ExcelField(value = "医疗互助", validator = NumericValidator.class)
    private String medicalMutualAid;

    /**
     * Corporate (career) annuity 企业(职业)年金
     */
    @ExcelField(value = "职业年金", validator = NumericValidator.class)
    private String corporateAnnuity;

    @ExcelField(value = "扣税", validator = NumericValidator.class)
    private String taxDeduction;

    @ExcelField(value = "实发工资", validator = NumericValidator.class)
    private String realWage;
}
