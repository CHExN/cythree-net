package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author CHExN
 */
@Data
@TableName("cy_car_refuel")
public class CarRefuel implements Serializable {

    private static final long serialVersionUID = -5658940360775022066L;

    /**
     * 主键
     */
    @TableId(value = "CAR_REFUEL_ID", type = IdType.AUTO)
    private Long carRefuelId;

    /**
     * 车辆id
     */
    @TableField("CAR_ID")
    private Long carId;

    /**
     * 录入时间
     */
    @TableField("DATE")
    private String date;

    /**
     * 金额
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 一条数据内包含的总数
     */
    private transient Long count;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String carNum;
    private transient String cardNum;
}
