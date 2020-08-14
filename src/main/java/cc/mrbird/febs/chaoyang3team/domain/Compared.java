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
@TableName("cy_compared")
public class Compared implements Serializable {

    private static final long serialVersionUID = -1611277078721379893L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 表名
     */
    @TableField("TABLE_NAME")
    private String tableName;

    /**
     * 表中文名
     */
    @TableField("TABLE_CN_NAME")
    private String tableCnName;

    /**
     * 月份 0无月份 1有月份
     */
    @TableField("MONTH")
    private String month;
    private transient String dateValue;

    /**
     * 是否分编内外 0没分 1分编内外
     */
    @TableField("INPUT")
    private String input;
    private transient String inputValue;

    /**
     * 人员类型 0编内 1编外归属 2编外分队
     */
    @TableField("STAFF_TYPE")
    private String staffType;

    /**
     * 是否是基础表 0不是 1是
     */
    @TableField("BASIS")
    private String basis;

    /**
     * 主键名
     */
    @TableField("ID_NAME")
    private String idName;

    /**
     * 是否有删除列 0没有 1有
     */
    @TableField("DELETED")
    private String deleted;

    /**
     * 是否是用身份证号进行绑定的 0不是 1是
     */
    @TableField("ID_NUM")
    private String idNum;

}
