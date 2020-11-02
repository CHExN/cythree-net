package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_openid_modify_record")
public class OpenidModifyRecord implements Serializable {

    private static final long serialVersionUID = 141803248749256311L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 原来的微信id
     */
    @TableField("OPEN_ID_LOW")
    private String openIdLow;

    /**
     * 新的微信id
     */
    @TableField("OPEN_ID_NEW")
    private String openIdNew;

    private transient String openId;

    /**
     * 更换时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 更换微信id的用户名称
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Integer deleted;

}
