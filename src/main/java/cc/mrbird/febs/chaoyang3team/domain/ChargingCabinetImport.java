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

    @ExcelField(value = "责任人")
    private String user;

    @ExcelField(value = "使用部门")
    private String useDeptName;

    @ExcelField(value = "充电端口数")
    private String chargingSectionsNumber;

    @ExcelField(value = "能否充电", required = true, readConverterExp = "能=1,否=2", writeConverterExp = "1=能,2=否", comment = "只可填写以下各项:[1、能; 2、否]")
    private String ifCharge;

    @ExcelField(value = "摆放地点")
    private String place;

    @ExcelField(value = "备注")
    private String remark;

}
