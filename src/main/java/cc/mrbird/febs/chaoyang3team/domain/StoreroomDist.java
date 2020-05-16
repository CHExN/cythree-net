package cc.mrbird.febs.chaoyang3team.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author CHExN
 */
@Data
@Excel("物资分配表")
public class StoreroomDist implements Serializable {

    private static final long serialVersionUID = -5507137532413811747L;

    @ExcelField(value = "物品编号")
    private Long id;

    @ExcelField("物品名称")
    private String name;

    @ExcelField("型号")
    private String type;

    @ExcelField("剩余库存")
    private BigDecimal storeroomCount;

    @ExcelField("物品单位")
    private String unit;

    @ExcelField("单价")
    private String money;

    @ExcelField("物资类别")
    private String typeApplicationToDict;

    @ExcelField("部门")
    private String toDeptName;

    @ExcelField("备注")
    private String remark;

}
