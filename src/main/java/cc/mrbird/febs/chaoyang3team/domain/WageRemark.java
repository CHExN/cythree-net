package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("cy_wage_remark")
public class WageRemark implements Serializable {

    private static final long serialVersionUID = -4913923584187456786L;

    public WageRemark() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 是编内还是编外 0编内 1编外
     */
    @TableField("INSIDE_OR_OUTSIDE")
    private String insideOrOutside;

    /**
     * 年
     */
    @TableField("YEAR")
    private String year;


    /**
     * 这个月的备注，每个月只有一条
     */
    @TableField("REMARK")
    private String remark;

    private transient Integer rowKey;

}
