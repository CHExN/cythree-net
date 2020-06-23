package cc.mrbird.febs.chaoyang3team.domain;

import cc.mrbird.febs.common.validator.NumericValidator;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Excel("物资分配导入模板")
public class StoreroomDistImport implements Serializable {

    private static final long serialVersionUID = -6889515167065846608L;

    @ExcelField(value = "年", required = true)
    private String year;

    @ExcelField(value = "月", required = true)
    private String month;

    @ExcelField(value = "日", required = true)
    private String day;

    @ExcelField(value = "公厕编号", required = true)
    private String wcNum;

    @ExcelField(value = "公厕名称")
    private String wcName;

    // @ExcelField(value = "物品编号", required = true)
    // 物品编号如果为空，则去判断物品名称
    @ExcelField(value = "物品编号")
    private String storeroomId;

    @ExcelField(value = "物品名称")
    private String storeroomName;

    @ExcelField(value = "分配数量", required = true, validator = NumericValidator.class, comment = "请填写浮点数或者整数")
    private BigDecimal amount;

}
