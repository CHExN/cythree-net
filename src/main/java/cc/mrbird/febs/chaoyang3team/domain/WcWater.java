package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("p_wc_water")
public class WcWater implements Serializable {

    private static final long serialVersionUID = 788667012341123295L;

    /**
     * 公厕id
     */
    @TableField("WC_ID")
    private Long wcId;

    /**
     * 水费记录id
     */
    @TableField("WATER_ID")
    private Long waterId;

}
