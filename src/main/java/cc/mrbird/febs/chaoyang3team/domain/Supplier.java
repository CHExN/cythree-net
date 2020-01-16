package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@TableName("cy_supplier")
@Excel("供应商信息表")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 6544966786854040012L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 供应商名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 状态 0正常1删除
     */
    @TableField("STATUS")
    private String status;

}
