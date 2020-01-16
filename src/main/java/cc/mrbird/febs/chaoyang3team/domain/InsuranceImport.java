package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@Excel("保险表")
public class InsuranceImport implements Serializable {

    private static final long serialVersionUID = -2352911278874444552L;

    @ExcelField(value = "编制类别", required = true, readConverterExp = "编内=0,编外=1", writeConverterExp = "0=编内,1=编外")
    private int insideOrOutside;

    @ExcelField(value = "身份证号", required = true, comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String idNum;

    /**
     * 养老保险 个人
     */
    @TableField("PENSION_INSURANCE_INDIVIDUAL")
    @ExcelField("养老保险(个人)")
    private String pensionInsuranceIndividual;

    /**
     * 失业保险 个人 (城镇户口的扣个人失业，农村户口的不扣)
     */
    @TableField("UNEMPLOYMENT_INSURANCE_INDIVIDUAL")
    @ExcelField("失业保险(个人)")
    private String unemploymentInsuranceIndividual;

    /**
     * 医疗保险 个人
     */
    @TableField("MEDICAL_INSURANCE_INDIVIDUAL")
    @ExcelField("医疗保险(个人)")
    private String medicalInsuranceIndividual;

    /**
     * 医疗互助 个人
     */
    @TableField("MEDICAL_MUTUAL_AID_INDIVIDUAL")
    @ExcelField(value = "医疗互助", required = true)
    private String medicalMutualAidIndividual;

    /**
     * 职业年金 个人 (编外无)
     */
    @TableField("CORPORATE_ANNUITY_INDIVIDUAL")
    @ExcelField("职业年金(个人)")
    private String corporateAnnuityIndividual;

    /**
     * 养老保险 单位
     */
    @TableField("PENSION_INSURANCE_UNIT")
    @ExcelField("养老保险(单位)")
    private String pensionInsuranceUnit;

    /**
     * 失业保险 单位
     */
    @TableField("UNEMPLOYMENT_INSURANCE_UNIT")
    @ExcelField("失业保险(单位)")
    private String unemploymentInsuranceUnit;

    /**
     * 工伤保险 单位
     */
    @TableField("WORK_INJURY_INSURANCE_UNIT")
    @ExcelField("工伤保险(单位)")
    private String workInjuryInsuranceUnit;

    /**
     * 生育保险 单位
     */
    @TableField("MATERNITY_INSURANCE_UNIT")
    @ExcelField("生育保险(单位)")
    private String maternityInsuranceUnit;

    /**
     * 医疗保险 单位
     */
    @TableField("MEDICAL_INSURANCE_UNIT")
    @ExcelField("医疗保险(单位)")
    private String medicalInsuranceUnit;

    /**
     * 医疗互助 单位
     */
    @TableField("MEDICAL_MUTUAL_AID_UNIT")
    @ExcelField("医疗互助(单位)")
    private String medicalMutualAidUnit;

    /**
     * 公疗补充 单位 (编外无)
     */
    @TableField("PUBLIC_THERAPY_SUPPLEMENT_UNIT")
    @ExcelField("公疗补充(单位)")
    private String publicTherapySupplementUnit;

    /**
     * 职业年金 单位 (编外无)
     */
    @TableField("CORPORATE_ANNUITY_UNIT")
    @ExcelField("职业年金(单位)")
    private String corporateAnnuityUnit;

}
