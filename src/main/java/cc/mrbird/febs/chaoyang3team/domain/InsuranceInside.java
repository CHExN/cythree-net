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
@TableName("cy_insurance_inside")
@Excel("编内保险表")
public class InsuranceInside implements Serializable {

    private static final long serialVersionUID = 6782122300686237382L;

    /**
     * 编内保险人员信息表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 编内人员表主键
     */
    @TableField("STAFF_ID")
    private Long staffId;

    @ExcelField(value = "序号")
    private transient String sortNum;

    @ExcelField(value = "姓名")
    private transient String name;

    @ExcelField(value = "身份证号")
    private transient String idNum;

    @ExcelField(value = "性别", writeConverterExp = "0=女,1=男")
    private transient String gender;

    /**
     * 民族
     */
    private transient String clan;

    @ExcelField(value = "户籍性质", writeConverterExp = "0=外地农业,1=外地非农业,2=本地农业,3=本地非农业")
    private transient String householdRegistrationType;

    /**
     * 报到日期
     */
    private transient String transferDate;

    /**
     * 手机联系电话
     */
    private transient String phoneCell;

    @ExcelField(value = "在职或非在职", writeConverterExp = "0=在职,1=非在职")
    private transient String isLeave;

    @TableField("ACCOUNT_ADDRESS")
    @ExcelField(value = "户口地址")
    private String accountAddress;

    @TableField("ACCOUNT_POSTAL_CODE")
    @ExcelField(value = "户口地址邮编")
    private String accountPostalCode;

    @TableField("CURRENT_ADDRESS")
    @ExcelField(value = "现居住地址")
    private String currentAddress;

    @TableField("CURRENT_POSTAL_CODE")
    @ExcelField(value = "现居住地址邮编")
    private String currentPostalCode;

    @TableField("BANK_CARD_NUMBER")
    @ExcelField(value = "银行卡号")
    private String bankCardNumber;

    @TableField("SOCIAL_SECURITY_HOSPITAL")
    @ExcelField(value = "社保医院")
    private String socialSecurityHospital;

    @TableField("NEW_TRANSFER")
    @ExcelField(value = "新参或转入", writeConverterExp = "0=新参,1=转入")
    private String newTransfer;

    @TableField("PENSION_INSURANCE_BASE")
    @ExcelField(value = "养老保险基数")
    private BigDecimal pensionInsuranceBase;

    @TableField("WORK_INJURY_INSURANCE_BASE")
    @ExcelField(value = "工伤保险基数")
    private BigDecimal workInjuryInsuranceBase;

    @TableField("MEDICAL_INSURANCE_BASE")
    @ExcelField(value = "医疗保险基数")
    private BigDecimal medicalInsuranceBase;

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

}
