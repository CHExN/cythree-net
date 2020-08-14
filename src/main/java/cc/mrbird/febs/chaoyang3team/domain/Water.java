package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_water")
@Excel("水费信息表")
public class Water implements Serializable {

    private static final long serialVersionUID = -7914407910859186850L;

    /**
     * 主键
     */
    @TableId(value = "WATER_ID", type = IdType.AUTO)
    private Long waterId;

    @ExcelField(value = "公厕编号")
    private transient String wcNum;

    @ExcelField(value = "公厕名称")
    private transient String wcName;

    private transient Long wcId;

    @TableField("YEAR")
    @ExcelField(value = "年")
    private String year;

    @TableField("MONTH")
    @ExcelField(value = "月份")
    private String month;

    @TableField("ACTUAL_AMOUNT")
    @ExcelField(value = "实际用量")
    private BigDecimal actualAmount;

    @TableField("UNIT_PRICE")
    private BigDecimal unitPrice;

    @TableField("TAP_WATER_FEE")
    @ExcelField(value = "自来水费")
    private BigDecimal tapWaterFee;

    @TableField("WATER_RESOURCES_FEE")
    @ExcelField(value = "水资源费")
    private BigDecimal waterResourcesFee;

    @TableField("SEWAGE_FEE")
    @ExcelField(value = "污水")
    private BigDecimal sewageFee;

    @TableField("TOTAL_AMOUNT")
    @ExcelField(value = "金额合计")
    private BigDecimal totalAmount;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

}
