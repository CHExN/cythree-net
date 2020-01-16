package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@TableName("cy_letter")
public class Letter implements Serializable {

    private static final long serialVersionUID = 7523667570948354691L;

    /**
     * 主键
     */
    @TableId(value = "LETTER_ID", type = IdType.AUTO)
    private Long letterId;

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
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 介绍信人
     */
    @TableField("LETTER_USER")
    private String letterUser;

    /**
     * 创建日期
     */
    @TableField("DATETIME")
    private String dateTime;

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

    @TableField("USERNAME")
    private String username;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
