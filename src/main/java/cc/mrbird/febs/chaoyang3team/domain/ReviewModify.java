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
@TableName("cy_review_modify")
public class ReviewModify implements Serializable {

    private static final long serialVersionUID = -5505931396307902520L;

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
     * 字段名称
     */
    @TableField("FIELD_NAME")
    private String fieldName;

    /**
     * 表名
     */
    @TableField("TABLE_NAME")
    private String tableName;

    /**
     * 表的中文名(模块名)
     */
    @TableField("TABLE_CNAME")
    private String tableCname;

    /**
     * 表主键
     */
    @TableField("TABLE_ID")
    private Long tableId;

    /**
     * 状态 0待审核 1通过 2未通过 3已修改
     */
    @TableField("PROCESS")
    private String process;

    /**
     * 部门名称
     */
    @TableField("DEPT_NAME")
    private String deptName;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 用户名称
     */
    @TableField("USERNAME")
    private String username;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
