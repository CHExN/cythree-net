package cc.mrbird.febs.chaoyang3team.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

@Data
@Excel("充电柜明细表")
public class ChargingCabinetImport implements Serializable {

    private static final long serialVersionUID = 1239618382946924159L;

    @ExcelField(value = "资产名称")
    private String assetName;

    @ExcelField(value = "品牌型号")
    private String brandModel;

    @ExcelField(value = "配发日期")
    private String allotmentDate;

    @ExcelField(value = "编制类别", readConverterExp = "编内=0,编外=1", writeConverterExp = "0=编内,1=编外", comment = "只可填写以下各项:[1、编内; 2、编外]")
    private String insideOrOutside;

    @ExcelField(value = "身份证号", comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String idNum;

    @ExcelField(value = "充电端口数")
    private String chargingSectionsNumber;

    @ExcelField(value = "能否充电", readConverterExp = "能=1,否=2", writeConverterExp = "1=能,2=否", comment = "只可填写以下各项:[1、能; 2、否]")
    private String ifCharge;

    @ExcelField(value = "摆放地点")
    private String place;

    @ExcelField(value = "备注")
    private String remark;

}
