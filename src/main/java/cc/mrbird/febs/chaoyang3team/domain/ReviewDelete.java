package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_review_delete")
public class ReviewDelete implements Serializable {

    private static final long serialVersionUID = 6549709259125829725L;

    /**
     * 审核修改表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 主要信息
     */
    @TableField("INFO")
    private String info;

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

    /**
     * 表主键
     */
    @TableField("TABLE_ID")
    private Long tableId;

    /**
     * 状态 0待审核 1已通过 2未通过 3已完成
     */
    @TableField("PROCESS")
    private String process;

    @TableField("DEPT_NAME")
    private String deptName;

    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 用户名称
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 审核类型 0删除 1修改
     */
    @TableField("TYPE")
    private String type;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
