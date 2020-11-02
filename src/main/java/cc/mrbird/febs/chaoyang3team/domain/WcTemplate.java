package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_wc_template")
public class WcTemplate implements Serializable {

    private static final long serialVersionUID = 4513357821913100836L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 模块名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 模块备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 模块下包含的公厕ids
     */
    @TableField("WC_IDS")
    private String wcIds;
    private transient String wcNums;
    private transient String wcNames;

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
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Integer deleted;

}
