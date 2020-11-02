package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_wc_inspection")
public class WcInspection implements Serializable {

    private static final long serialVersionUID = -4285944599645086953L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    private transient Long wcTemplateId;

    /**
     * 部门主键
     */
    @TableField("DEPT_ID")
    private Long deptId;
    private transient String deptName;

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
     * 巡检日期
     */
    @TableField("INSPECTION_DATE")
    private LocalDateTime inspectionDate;
    private transient String inspectionDateString;

    /**
     * 巡检公厕主键
     */
    @TableField("WC_ID")
    private Long wcId;
    private transient String wcName;
    private transient String wcNum;
    private transient String streetTown;
    private transient String wcSort;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Integer deleted;

}
