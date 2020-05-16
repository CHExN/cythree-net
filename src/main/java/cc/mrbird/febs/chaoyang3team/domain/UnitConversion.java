package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author CHExN
 */
@Data
@TableName("cy_unit_conversion")
public class UnitConversion implements Serializable {

    private static final long serialVersionUID = 6634884308378648275L;

    @TableId("STOREROOM_ID")
    private Long storeroomId;

    /**
     * 转换后的单位
     */
    @TableField("UNIT")
    private String unit;

    /**
     * 原先1个数量可以转换为？个的数量
     */
    @TableField("AMOUNT_CONVERSION")
    private BigDecimal amountConversion;

    /**
     * 转换后的总库存
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 剩余分配的数量
     */
    @TableField("AMOUNT_DIST")
    private BigDecimal amountDist;

    private transient BigDecimal amountOriginal;
}
