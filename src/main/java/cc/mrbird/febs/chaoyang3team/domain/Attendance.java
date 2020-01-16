package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CHExN
 */
@Data
@TableName("cy_attendance")
@Excel("考勤表")
public class Attendance implements Serializable {

    private static final long serialVersionUID = -3604017152812588371L;

    /**
     * 考勤表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 提交数据的部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;
    private String deptName;

    /**
     * 编外人员信息表主键
     */
    @TableField("STAFF_ID")
    private Long staffId;
    @ExcelField(value = "姓名")
    private transient String staffName;
    private transient String staffIdCard;

    @TableField("DAY_WORK")
    @ExcelField(value = "日勤")
    private Integer dayWork;

    @TableField("NIGHT_WORK")
    @ExcelField(value = "夜勤")
    private Integer nightWork;

    @TableField("DOUBLE_PLUS")
    @ExcelField(value = "双加")
    private Integer doublePlus;

    @TableField("HOLIDAY_PLUS")
    @ExcelField(value = "节加")
    private Integer holidayPlus;

    @TableField("HOUR15")
    @ExcelField(value = "1.5倍小时加班")
    private Integer hour15;

    @TableField("HOUR20")
    @ExcelField(value = "2倍小时加班")
    private Integer hour20;

    @TableField("PUBLIC_HOLIDAY")
    @ExcelField(value = "公休")
    private Integer publicHoliday;

    @TableField("WORKING_LEAVE")
    @ExcelField(value = "工龄假")
    private Integer workingLeave;

    @TableField("SICK_LEAVE")
    @ExcelField(value = "病假")
    private Integer sickLeave;

    @TableField("THING_LEAVE")
    @ExcelField(value = "事假")
    private Integer thingLeave;

    @TableField("MARRIAGE_LEAVE")
    @ExcelField(value = "婚假")
    private Integer marriageLeave;

    @TableField("FUNERAL_LEAVE")
    @ExcelField(value = "丧假")
    private Integer funeralLeave;

    @TableField("WORK_INJURY")
    @ExcelField(value = "工伤假")
    private Integer workInjury;

    @TableField("LATE_AND_LEAVE_EARLY")
    @ExcelField(value = "迟到早退折合天数")
    private Integer lateAndLeaveEarly;

    @TableField("USUALLY_DUTY")
    @ExcelField(value = "平时值班")
    private Integer usuallyDuty;

    @TableField("WEEKEND_DUTY")
    @ExcelField(value = "周末值班")
    private Integer weekendDuty;

    @TableField("HOLIDAY_WATCH")
    @ExcelField(value = "节日值班")
    private Integer holidayWatch;

    @TableField("DEDUCTION")
    @ExcelField(value = "扣分")
    private Integer deduction;

    @TableField("TOTAL_ATTENDANCE_DAYS")
    @ExcelField(value = "总计(出勤天数)")
    private Integer totalAttendanceDays;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    @TableField("START_DATE")
    @ExcelField(value = "开始日期")
    private Date startDate;

    @TableField("END_DATE")
    @ExcelField(value = "结束日期")
    private Date endDate;

    private transient String year;
    private transient String month;

}
