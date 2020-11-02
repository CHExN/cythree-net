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
@TableName("p_wc_storeroom")
public class WcStoreroom implements Serializable {

    private static final long serialVersionUID = -2203884755984912254L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 公厕id
     */
    @TableField("WC_ID")
    private Long wcId;

    /**
     * 库房id（出库）
     */
    @TableField("STOREROOM_ID")
    private Long storeroomId;
    private transient String storeroomName;
    private transient String storeroomIds;
    private transient String[] storeroomIdArr;

    /**
     * 分配数量
     */
    @TableField("AMOUNT")
    private BigDecimal amount;
    /**
     * 此storeroomId所剩余的分配数量，用于在删除分配记录是，顺带查询
     */
    private transient BigDecimal amountDist;

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
     * 日
     */
    @TableField("DAY")
    private String day;

    /**
     * 操作用户
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 公厕所属(分队)
     */
    @TableField("WC_OWN")
    private String wcOwn;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;


    private transient String yearForm;
    private transient String yearTo;
    private transient String monthForm;
    private transient String monthTo;
    private transient String wcName;
    private transient String wcNum;
    private transient String name;
    private transient String[] names;
    private transient String typeApplication;
    private transient BigDecimal money;
    private transient String unit;
    private transient String type;
    private transient String createTimeFrom;
    private transient String createTimeTo;


}
