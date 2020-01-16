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
@TableName("p_wc_electricity")
public class WcElectricity implements Serializable {

    private static final long serialVersionUID = 5827004965565125600L;

    /**
     * 水费id
     */
    @TableField("WC_ID")
    private Long wcId;

    /**
     * 电费记录id
     */
    @TableField("ELECTRICITY_ID")
    private Long electricityId;

}
