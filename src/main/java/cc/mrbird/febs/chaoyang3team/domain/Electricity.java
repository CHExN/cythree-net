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
import java.util.Date;

/**
 * @author CHExN
 */
@Data
@TableName("cy_electricity")
@Excel("电费信息表")
public class Electricity implements Serializable {

    private static final long serialVersionUID = 8946070493203700033L;

    /**
     * 主键
     */
    @TableId(value = "ELECTRICITY_ID", type = IdType.AUTO)
    private Long electricityId;

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
    @ExcelField(value = "单价(元/度)")
    private BigDecimal unitPrice;

    @TableField("TOTAL_AMOUNT")
    @ExcelField(value = "金额合计")
    private BigDecimal totalAmount;

    @TableField("REC_DATE")
    @ExcelField(value = "缴费日期")
    private Date recDate;

    @TableField("TYPE")
    @ExcelField(value = "购电方式")
    private String type;

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
