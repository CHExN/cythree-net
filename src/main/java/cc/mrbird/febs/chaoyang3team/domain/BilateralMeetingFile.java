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
@TableName("p_bilateral_meeting_file")
public class BilateralMeetingFile implements Serializable {

    private static final long serialVersionUID = -3289986740238371404L;

    /**
     * 上会议题ID
     */
    @TableField("BILATERAL_MEETING_ID")
    private Long bilateralMeetingId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
