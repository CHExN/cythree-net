package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author CHExN
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WageRemark extends Model<WageRemark> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
            @TableId("ID")
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
     * 月
     */
        @TableField("MONTH")
    private String month;

    /**
     * 这个月的备注，每个月只有一条
     */
        @TableField("REMARK")
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
