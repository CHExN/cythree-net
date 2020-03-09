package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_bilateral_meeting")
public class BilateralMeeting implements Serializable {

    private static final long serialVersionUID = 4934682011385971312L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @TableField("DEPT_NAME")
    private String deptName;

    /**
     * 拟上会议题
     */
    @TableField("BILATERAL_MEETING")
    private String bilateralMeeting;

    /**
     * 提议事由梗概
     */
    @TableField("PROPOSED_CAUSE_SUMMARY")
    private String proposedCauseSummary;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 上会时间
     */
    @TableField("MEETING_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingTime;

    /**
     * 处理状态 1算是审核通过 -1是不通过 0是正在审核
     */
    @TableField("PROCESS")
    private Integer process;

    /**
     * 当前处理步骤
     */
    @TableField("STEP")
    private Integer step;

    /**
     * 记录审核流程的账号名称，如有多人，以逗号分隔
     */
    @TableField("REVIEW")
    private String review;

    /**
     * 申请人
     */
    @TableField("APPLICANT")
    private String applicant;

    /**
     * 申请人账号
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 办公室意见
     */
    @TableField("OPINION_OFFICE")
    private String opinionOffice;

    /**
     * 队长意见
     */
    @TableField("OPINION_CAPTAIN")
    private String opinionCaptain;

    /**
     * 办公室时间
     */
    @TableField("DATE_OFFICE")
    private LocalDateTime dateOffice;

    /**
     * 队长时间
     */
    @TableField("DATE_CAPTAIN")
    private LocalDateTime dateCaptain;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    private transient String fileId;

}
