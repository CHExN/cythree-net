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
@Excel("编内个税表")
public class TaxInsideImport implements Serializable {

    private static final long serialVersionUID = 3391613183231011926L;

    @ExcelField(value = "证照类型", required = true, readConverterExp = "居民身份证=1,港澳居民来往内地通行证=2,港澳居民居住证=3,台湾居民来往大陆通行证=4,台湾居民居住证=5,中国护照=6,外国护照=7,外国人永久居留身份证=8,外国人工作许可证（A类）=9,外国人工作许可证（B类）=10,外国人工作许可证（C类）=11,其他个人证件=12")
    private String idCardType;

    @ExcelField(value = "证照号码", required = true, comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String staffIdCard;

    @ExcelField(value = "本期收入", required = true, validator = NumericValidator.class)
    private String currentIncome;

    @ExcelField(value = "本期免税收入", validator = NumericValidator.class)
    private String currentTaxFreeIncome;

    /**
     * Basic pension insurance premium
     */
    @ExcelField(value = "基本养老保险费", validator = NumericValidator.class)
    private String basicPensionIp;

    /**
     * Basic medical insurance premium
     */
    @ExcelField(value = "基本医疗保险费", validator = NumericValidator.class)
    private String basicMedicalIp;

    /**
     * Unemployment insurance premium
     */
    @ExcelField(value = "失业保险费", validator = NumericValidator.class)
    private String unemploymentIp;

    @ExcelField(value = "住房公积金", validator = NumericValidator.class)
    private String housingFund;

    /**
     * Cumulative child education
     */
    @ExcelField(value = "累计子女教育", validator = NumericValidator.class)
    private String cumulativeChildE;

    /**
     * Cumulative continuing education
     */
    @ExcelField(value = "累计继续教育", validator = NumericValidator.class)
    private String cumulativeContinuingE;

    @ExcelField(value = "累计住房贷款利息", validator = NumericValidator.class)
    private String cumulativeHomeLoanInterest;

    @ExcelField(value = "累计住房租金", validator = NumericValidator.class)
    private String cumulativeHousingRent;

    @ExcelField(value = "累计赡养老人", validator = NumericValidator.class)
    private String cumulativeElderlySupport;

    /**
     * Corporate (career) annuity
     */
    @ExcelField(value = "企业(职业)年金", validator = NumericValidator.class)
    private String corporateAnnuity;

    @ExcelField(value = "商业健康保险", validator = NumericValidator.class)
    private String commercialHealthInsurance;

    @ExcelField(value = "税延养老保险", validator = NumericValidator.class)
    private String taxExtensionPensionInsurance;

    @ExcelField(value = "其他", validator = NumericValidator.class)
    private String other;

    @ExcelField(value = "准予扣除的捐赠额", validator = NumericValidator.class)
    private String allowanceForDeduction;

    @ExcelField(value = "减免税额", validator = NumericValidator.class)
    private String taxDeduction;

    @ExcelField(value = "备注")
    private String remark;

}
