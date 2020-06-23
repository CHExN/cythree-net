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
@Excel("编外工资表")
public class WageOutsideImport implements Serializable {

    private static final long serialVersionUID = -2845813665597512798L;
    
    @ExcelField(value = "姓名", comment = "此项不用必须填写，只是作为参照使用")
    private String staffName;

    @ExcelField(value = "证照号码", required = true, comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String staffIdCard;

    @ExcelField(value = "基本工资", validator = NumericValidator.class)
    private String currentIncome;

    @ExcelField(value = "岗位津贴", validator = NumericValidator.class)
    private String postAllowance;

    @ExcelField(value = "环卫津贴", validator = NumericValidator.class)
    private String sanitationAllowance;

    @ExcelField(value = "危岗补助", validator = NumericValidator.class)
    private String dangerousSubsidy;

    @ExcelField(value = "绩效奖金", validator = NumericValidator.class)
    private String performanceBonus;

    @ExcelField(value = "加班费", validator = NumericValidator.class)
    private String overtimePay;

    @ExcelField(value = "过节费", validator = NumericValidator.class)
    private String holidayCosts;

    @ExcelField(value = "应发工资")
    private String payable;

    @ExcelField(value = "养老保险", validator = NumericValidator.class)
    private String basicPensionIp;

    @ExcelField(value = "失业保险", validator = NumericValidator.class)
    private String unemploymentIp;

    @ExcelField(value = "医疗保险", validator = NumericValidator.class)
    private String basicMedicalIp;

    @ExcelField(value = "税金", validator = NumericValidator.class)
    private String taxDeduction;

    @ExcelField(value = "会费", validator = NumericValidator.class)
    private String membership;

    @ExcelField(value = "住房公积金", validator = NumericValidator.class)
    private String housingFund;

    @ExcelField(value = "病事假", validator = NumericValidator.class)
    private String sickLeave;

    @ExcelField(value = "实发工资")
    private String realWage;

    @ExcelField(value = "空列01")
    private String emptyColumn01;
    @ExcelField(value = "空列02")
    private String emptyColumn02;
    @ExcelField(value = "空列03")
    private String emptyColumn03;
    @ExcelField(value = "空列04")
    private String emptyColumn04;
    @ExcelField(value = "空列05")
    private String emptyColumn05;

    @ExcelField(value = "空列06")
    private String emptyColumn06;
    @ExcelField(value = "空列07")
    private String emptyColumn07;
    @ExcelField(value = "空列08")
    private String emptyColumn08;
    @ExcelField(value = "空列09")
    private String emptyColumn09;
    @ExcelField(value = "空列10")
    private String emptyColumn10;

}
