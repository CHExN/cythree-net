package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_meeting_room")
@Excel("申请预约会议室信息汇总")
public class MeetingRoom implements Serializable {

    private static final long serialVersionUID = 3523449649085097781L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("DATE_FROM")
    @ExcelField(value = "申请日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @TableField("TIME_FROM")
    @ExcelField(value = "开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeFrom;

    @TableField("TIME_TO")
    @ExcelField(value = "结束时间")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeTo;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DEPT_NAME")
    @ExcelField(value = "部门名称")
    private String deptName;

    /**
     * 创建日期
     */
    @TableField("DATETIME")
    private String dateTime;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
