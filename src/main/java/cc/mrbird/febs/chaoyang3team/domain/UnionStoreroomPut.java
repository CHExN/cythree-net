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
@TableName("p_union_storeroom_put")
public class UnionStoreroomPut implements Serializable {

    private static final long serialVersionUID = 2086808231783369684L;

    /**
     * 入库记录ID
     */
    @TableField("PUT_ID")
    private Long putId;

    /**
     * 库房物品ID
     */
    @TableField("STOREROOM_ID")
    private Long storeroomId;

    public UnionStoreroomPut() {
    }

}
