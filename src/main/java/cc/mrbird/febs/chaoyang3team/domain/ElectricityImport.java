package cc.mrbird.febs.chaoyang3team.domain;

import cc.mrbird.febs.common.validator.NumericValidator;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电费表导入模板
 * @author CHExN
 */
@Data
@Excel("电费信息表模板")
public class ElectricityImport implements Serializable {

    private static final long serialVersionUID = -8561474513219102032L;

    @ExcelField(value = "电表编号", required = true, comment = "请填写正确的电表编号，否则必定导入失败")
    private String electricityNum;

    @ExcelField(value = "实际用量", required = true, validator = NumericValidator.class, comment = "请填写浮点数或者整数")
    private BigDecimal actualAmount;

    @ExcelField(value = "单价(元/度)", required = true, validator = NumericValidator.class, comment = "请填写浮点数或者整数")
    private BigDecimal unitPrice;

    @ExcelField(value = "金额合计", required = true, validator = NumericValidator.class, comment = "请填写浮点数或者整数")
    private BigDecimal totalAmount;

    @ExcelField(value = "登记日期", required = true, dateFormat = "yyyy/MM/dd", comment = "请以yyyy/MM/dd的格式填写登记日期")
    private Date createDate;

    @ExcelField(value = "缴费日期", required = true, dateFormat = "yyyy/MM/dd", comment = "请以yyyy/MM/dd的格式填写登记日期")
    private Date recDate;

    @ExcelField(value = "购电方式", required = true)
    private String type;

}
