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

    @ExcelField(value = "电表编号")
    private transient String electricityNum;

    @ExcelField(value = "公厕名称")
    private transient String wcName;

    @ExcelField(value = "公厕编号")
    private transient String wcNum;

    private transient Long wcId;

    private transient String date;

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

    @TableField("CREATE_DATE")
    @ExcelField(value = "登记日期")
    private Date createDate;

    @TableField("REC_DATE")
    @ExcelField(value = "缴费日期")
    private Date recDate;

    @TableField("TYPE")
    @ExcelField(value = "购电方式")
    private String type;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
