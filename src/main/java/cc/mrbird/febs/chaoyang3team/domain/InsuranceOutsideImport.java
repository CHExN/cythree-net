package cc.mrbird.febs.chaoyang3team.domain;

import cc.mrbird.febs.common.validator.NumericValidator;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * 编外保险模板
 * @author CHExN
 */
@Data
@Excel("编外保险模板")
public class InsuranceOutsideImport implements Serializable {

    private static final long serialVersionUID = 1275623674028930526L;

    @ExcelField(value = "身份证号", required = true)
    private String idNum;

    @ExcelField(value = "养老保险基数", required = true, validator = NumericValidator.class)
    private String pensionInsuranceBase;

    @ExcelField(value = "工伤保险基数", required = true, validator = NumericValidator.class)
    private String workInjuryInsuranceBase;

    @ExcelField(value = "医疗保险基数", required = true, validator = NumericValidator.class)
    private String medicalInsuranceBase;

    @ExcelField(value = "户口地址")
    private String accountAddress;

    @ExcelField(value = "户口地址邮编")
    private String accountPostalCode;

    @ExcelField(value = "现居住地址")
    private String currentAddress;

    @ExcelField(value = "现居住地址邮编")
    private String currentPostalCode;

    @ExcelField(value = "银行卡号")
    private String bankCardNumber;

    @ExcelField(value = "社保医院")
    private String socialSecurityHospital;

    @ExcelField(value = "新参或转入", readConverterExp = "新参=0,转入=1", writeConverterExp = "0=新参,1=转入", comment = "只可填写以下各项:1、新参; 2、转入")
    private String newTransfer;

}
