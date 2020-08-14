package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_union_storeroom_put_out")
public class UnionStoreroomPutOut implements Serializable {

    private static final long serialVersionUID = -1243308216511709754L;

    /**
     * 出入库单id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 1入库 2出库
     */
    @TableField("IS_PUT")
    private String isPut;

    /**
     * 单号
     */
    @TableField("NUM")
    private String num;

    /**
     * 出/入库日期
     */
    @TableField("DATE")
    private String date;

    /**
     * 科目(出库到哪个部门的id)
     */
    @TableField("TO_DEPT_ID")
    private Long toDeptId;
    private transient String toDeptName;

    /**
     * 总价
     */
    @TableField("MONEY")
    private BigDecimal money;

    /**
     * 保管员
     */
    @TableField("STORAGE")
    private String storage;

    /**
     * 经手人
     */
    @TableField("HANDLE")
    private String handle;

    /**
     * 物资类别 1保洁物品 2劳保物品 3办公室用品 4维修用品 5固定资产 6工会用品 7技安用品 8食堂用品 9其他
     */
    @TableField("TYPE_APPLICATION")
    private String typeApplication;

    /**
     * 操作账号
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 供应商
     */
    @TableField("SUPPLIER")
    private String supplier;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String StoreroomList;

    private transient Long count;

}
