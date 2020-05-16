package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("cy_plan")
public class Plan implements Serializable {

    private static final long serialVersionUID = 7431533572257279533L;

    /**
     * 采购计划单id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 物品名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 型号
     */
    @TableField("TYPE")
    private String type;

    /**
     * 单位
     */
    @TableField("UNIT")
    private String unit;

    /**
     * 数量
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 计划单物品状态 1未入库 2已入库
     */
    @TableField("STATUS")
    private String status;

    private transient String deptName;

}
