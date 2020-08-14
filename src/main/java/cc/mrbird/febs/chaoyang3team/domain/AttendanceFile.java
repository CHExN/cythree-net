package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("p_attendance_file")
public class AttendanceFile implements Serializable {

    private static final long serialVersionUID = -2846404186677800036L;

    @TableField("YEAR")
    private String year;

    @TableField("MONTH")
    private String month;

    @TableField("FILE_ID")
    private Long fileId;

}
