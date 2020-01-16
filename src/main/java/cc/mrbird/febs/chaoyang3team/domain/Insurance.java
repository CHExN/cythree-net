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
@TableName("cy_insurance")
@Excel("保险表")
public class Insurance implements Serializable {

    private static final long serialVersionUID = -1517879087199475142L;

    /**
     * 保险表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ExcelField("序号")
    private transient String sortNum;

    @TableField("INSIDE_OR_OUTSIDE")
    @ExcelField(value = "编制类别", writeConverterExp = "0=编内,1=编外")
    private String insideOrOutside;

    /**
     * 保险人员表主键 (注意，此项不是本表主键)
     */
    @TableField("INSURANCE_ID")
    private Long insuranceId;
    private transient Long staffId;
    @ExcelField("姓名")
    private transient String name;
    @ExcelField("身份证号")
    private transient String idNum;
    @ExcelField("养老保险基数")
    private transient String pensionInsuranceBase;
    @ExcelField("工伤保险基数")
    private transient String workInjuryInsuranceBase;
    @ExcelField("医疗保险基数")
    private transient String medicalInsuranceBase;

    /**
     * 养老保险 个人
     */
    @TableField("PENSION_INSURANCE_INDIVIDUAL")
    @ExcelField("养老保险")
    private BigDecimal pensionInsuranceIndividual;

    /**
     * 失业保险 个人 (城镇户口的扣个人失业，农村户口的不扣)
     */
    @TableField("UNEMPLOYMENT_INSURANCE_INDIVIDUAL")
    @ExcelField("失业保险")
    private BigDecimal unemploymentInsuranceIndividual;

    /**
     * 医疗保险 个人
     */
    @TableField("MEDICAL_INSURANCE_INDIVIDUAL")
    @ExcelField("医疗保险")
    private BigDecimal medicalInsuranceIndividual;

    /**
     * 医疗互助 个人
     */
    @TableField("MEDICAL_MUTUAL_AID_INDIVIDUAL")
    @ExcelField("医疗互助")
    private BigDecimal medicalMutualAidIndividual;

    /**
     * 职业年金 个人 (编外无)
     */
    @TableField("CORPORATE_ANNUITY_INDIVIDUAL")
    @ExcelField("职业年金")
    private BigDecimal corporateAnnuityIndividual;

    /**
     * 养老保险 单位
     */
    @TableField("PENSION_INSURANCE_UNIT")
    @ExcelField("养老保险")
    private BigDecimal pensionInsuranceUnit;

    /**
     * 失业保险 单位
     */
    @TableField("UNEMPLOYMENT_INSURANCE_UNIT")
    @ExcelField("失业保险")
    private BigDecimal unemploymentInsuranceUnit;

    /**
     * 工伤保险 单位
     */
    @TableField("WORK_INJURY_INSURANCE_UNIT")
    @ExcelField("工伤保险")
    private BigDecimal workInjuryInsuranceUnit;

    /**
     * 生育保险 单位
     */
    @TableField("MATERNITY_INSURANCE_UNIT")
    @ExcelField("生育保险")
    private BigDecimal maternityInsuranceUnit;

    /**
     * 医疗保险 单位
     */
    @TableField("MEDICAL_INSURANCE_UNIT")
    @ExcelField("医疗保险")
    private BigDecimal medicalInsuranceUnit;

    /**
     * 医疗互助 单位
     */
    @TableField("MEDICAL_MUTUAL_AID_UNIT")
    @ExcelField("医疗互助")
    private BigDecimal medicalMutualAidUnit;

    /**
     * 公疗补充 单位 (编外无)
     */
    @TableField("PUBLIC_THERAPY_SUPPLEMENT_UNIT")
    @ExcelField("公疗补充")
    private BigDecimal publicTherapySupplementUnit;

    /**
     * 职业年金 单位 (编外无)
     */
    @TableField("CORPORATE_ANNUITY_UNIT")
    @ExcelField("职业年金")
    private BigDecimal corporateAnnuityUnit;

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
    private String year;

    /**
     * 月
     */
    @TableField("MONTH")
    private String month;

    private transient String yearForm;
    private transient String yearTo;
    private transient String monthForm;
    private transient String monthTo;

}
