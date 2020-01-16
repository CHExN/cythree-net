package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物品价格管理
 *
 * @author CHExN
 */
@Data
@TableName("cy_price")
public class Price implements Serializable {

    private static final long serialVersionUID = -919347965968157056L;

    /**
     * 物品价格管理表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 物品名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 单价
     */
    @TableField("MONEY")
    private BigDecimal money;
}
