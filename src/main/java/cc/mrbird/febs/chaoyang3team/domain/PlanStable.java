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
@TableName("cy_plan_stable")
public class PlanStable implements Serializable {

    private static final long serialVersionUID = 3652457338024045369L;

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
     * 备注
     */
    @TableField("REMARK")
    private String remark;

}
