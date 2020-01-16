package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 食堂用品类别管理
 *
 * @author CHExN
 */
@Data
@TableName("cy_canteen")
public class Canteen implements Serializable {

    private static final long serialVersionUID = -4421146169249294357L;

    /**
     * 食堂用品类别管理表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 类别名称
     */
    @TableField("NAME")
    private String name;

}
