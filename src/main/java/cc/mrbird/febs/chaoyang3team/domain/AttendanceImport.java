package cc.mrbird.febs.chaoyang3team.domain;

import cc.mrbird.febs.common.validator.NumberValidator;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@Excel("考勤表")
public class AttendanceImport implements Serializable {

    private static final long serialVersionUID = -6648447358130741841L;

    @ExcelField(value = "编外人员身份证号", required = true, comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String staffIdCard;

    @ExcelField(value = "日勤", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer dayWork;

    @ExcelField(value = "夜勤", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer nightWork;

    @ExcelField(value = "双加", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer doublePlus;

    @ExcelField(value = "节加", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer holidayPlus;

    @ExcelField(value = "1.5倍小时加班", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer hour15;

    @ExcelField(value = "2倍小时加班", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer hour20;

    @ExcelField(value = "公休", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer publicHoliday;

    @ExcelField(value = "工龄假", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer workingLeave;

    @ExcelField(value = "病假", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer sickLeave;

    @ExcelField(value = "事假", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer thingLeave;

    @ExcelField(value = "婚假", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer marriageLeave;

    @ExcelField(value = "丧假", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer funeralLeave;

    @ExcelField(value = "工伤假", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer workInjury;

    @ExcelField(value = "迟到早退折合天数", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer lateAndLeaveEarly;

    @ExcelField(value = "平时值班", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer usuallyDuty;

    @ExcelField(value = "周末值班", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer weekendDuty;

    @ExcelField(value = "节日值班", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer holidayWatch;

    @ExcelField(value = "扣分", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer deduction;

    @ExcelField(value = "总计(出勤天数)", required = true, validator = NumberValidator.class, comment = "请填写整数")
    private Integer totalAttendanceDays;

    @ExcelField(value = "备注")
    private String remark;
}
