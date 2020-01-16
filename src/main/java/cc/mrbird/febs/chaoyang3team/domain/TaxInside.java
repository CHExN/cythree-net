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
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_tax_inside")
@Excel("编内个税表")
public class TaxInside implements Serializable {

    private static final long serialVersionUID = -6810172853649827404L;

    /**
     * 编内个税表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ExcelField(value = "序号")
    private transient Integer sortNum;

    /**
     * 人员id
     */
    @TableField("STAFF_ID")
    private Long staffId;

    @ExcelField(value = "姓名")
    private transient String staffName;

    @TableField("ID_CARD_TYPE")
    @ExcelField(value = "证照类型", writeConverterExp = "1=居民身份证,2=港澳居民来往内地通行证,3=港澳居民居住证,4=台湾居民来往大陆通行证,5=台湾居民居住证,6=中国护照,7=外国护照,8=外国人永久居留身份证,9=外国人工作许可证（A类）,10=外国人工作许可证（B类）,11=外国人工作许可证（C类）,12=其他个人证件")
    private String idCardType;

    @ExcelField(value = "证照号码")
    private transient String staffIdCard;

    @TableField("CURRENT_INCOME")
    private BigDecimal currentIncome;
    @ExcelField(value = "本期收入")
    private transient BigDecimal currentIncomeSum;

    @TableField("CURRENT_TAX_FREE_INCOME")
    private BigDecimal currentTaxFreeIncome;
    @ExcelField(value = "本期免税收入")
    private transient BigDecimal currentTaxFreeIncomeSum;

    /**
     * Basic pension insurance premium
     */
    @TableField("BASIC_PENSION_IP")
    private BigDecimal basicPensionIp;
    @ExcelField(value = "基本养老保险费")
    private transient BigDecimal basicPensionIpSum;

    /**
     * Basic medical insurance premium
     */
    @TableField("BASIC_MEDICAL_IP")
    private BigDecimal basicMedicalIp;
    @ExcelField(value = "基本医疗保险费")
    private transient BigDecimal basicMedicalIpSum;

    /**
     * Unemployment insurance premium
     */
    @TableField("UNEMPLOYMENT_IP")
    private BigDecimal unemploymentIp;
    @ExcelField(value = "失业保险费")
    private transient BigDecimal unemploymentIpSum;

    @TableField("HOUSING_FUND")
    private BigDecimal housingFund;
    @ExcelField(value = "住房公积金")
    private transient BigDecimal housingFundSum;

    /**
     * Cumulative child education
     */
    @TableField("CUMULATIVE_CHILD_E")
    private BigDecimal cumulativeChildE;
    @ExcelField(value = "累计子女教育")
    private transient BigDecimal cumulativeChildESum;

    /**
     * Cumulative continuing education
     */
    @TableField("CUMULATIVE_CONTINUING_E")
    private BigDecimal cumulativeContinuingE;
    @ExcelField(value = "累计继续教育")
    private transient BigDecimal cumulativeContinuingESum;

    @TableField("CUMULATIVE_HOME_LOAN_INTEREST")
    private BigDecimal cumulativeHomeLoanInterest;
    @ExcelField(value = "累计住房贷款利息")
    private transient BigDecimal cumulativeHomeLoanInterestSum;

    @TableField("CUMULATIVE_HOUSING_RENT")
    private BigDecimal cumulativeHousingRent;
    @ExcelField(value = "累计住房租金")
    private transient BigDecimal cumulativeHousingRentSum;

    @TableField("CUMULATIVE_ELDERLY_SUPPORT")
    private BigDecimal cumulativeElderlySupport;
    @ExcelField(value = "累计赡养老人")
    private transient BigDecimal cumulativeElderlySupportSum;

    /**
     * Corporate (career) annuity
     */
    @TableField("CORPORATE_ANNUITY")
    private BigDecimal corporateAnnuity;
    @ExcelField(value = "企业(职业)年金")
    private transient BigDecimal corporateAnnuitySum;

    @TableField("COMMERCIAL_HEALTH_INSURANCE")
    private BigDecimal commercialHealthInsurance;
    @ExcelField(value = "商业健康保险")
    private transient BigDecimal commercialHealthInsuranceSum;

    @TableField("TAX_EXTENSION_PENSION_INSURANCE")
    private BigDecimal taxExtensionPensionInsurance;
    @ExcelField(value = "税延养老保险")
    private transient BigDecimal taxExtensionPensionInsuranceSum;

    @TableField("OTHER")
    private BigDecimal other;
    @ExcelField(value = "其他")
    private transient BigDecimal otherSum;

    @TableField("ALLOWANCE_FOR_DEDUCTION")
    private BigDecimal allowanceForDeduction;
    @ExcelField(value = "准予扣除的捐赠额")
    private transient BigDecimal allowanceForDeductionSum;

    @TableField("TAX_DEDUCTION")
    private BigDecimal taxDeduction;
    @ExcelField(value = "减免税额")
    private transient BigDecimal taxDeductionSum;

    @TableField("FINAL_WAGE")
    private BigDecimal finalWage;
    @ExcelField(value = "最终工资")
    private transient BigDecimal finalWageSum;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

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

    private transient String yearForm;
    private transient String monthForm;
    private transient String yearTo;
    private transient String monthTo;

}
