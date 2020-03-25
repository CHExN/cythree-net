package cc.mrbird.febs.chaoyang3team.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

@Data
@Excel("灭火器明细表")
public class FireExtinguisherImport implements Serializable {

    private static final long serialVersionUID = 2505606533853216904L;

    @ExcelField(value = "资产名称")
    private String assetName;

    @ExcelField(value = "品牌型号")
    private String brandModel;

    @ExcelField(value = "配发日期1")
    private String allotmentDate1;

    @ExcelField(value = "配发日期2")
    private String allotmentDate2;

    @ExcelField(value = "责任人")
    private String user;

    @ExcelField(value = "检测日期1")
    private String testDate1;

    @ExcelField(value = "检测日期2")
    private String testDate2;

    @ExcelField(value = "公厕编号")
    private String wcNum;
    private transient String wcName;

    @ExcelField(value = "规格1")
    private String specification1;

    @ExcelField(value = "规格2")
    private String specification2;

    @ExcelField(value = "摆放地点")
    private String place;

    @ExcelField(value = "备注")
    private String remark;

}
