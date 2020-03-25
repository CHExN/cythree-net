package cc.mrbird.febs.chaoyang3team.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

@Data
@Excel("电动车明细表")
public class CarElectricImport implements Serializable {

    private static final long serialVersionUID = 2811964672748159519L;

    @ExcelField(value = "车辆类型")
    private String carType;

    @ExcelField(value = "车辆品牌")
    private String carBrands;

    @ExcelField(value = "钢架号")
    private String steelFrameNumber;

    @ExcelField(value = "电机号")
    private String motorNumber;

    @ExcelField(value = "车牌号")
    private String carNumber;

    @ExcelField(value = "编制类别", readConverterExp = "编内=0,编外=1", writeConverterExp = "0=编内,1=编外", comment = "只可填写以下各项:[1、编内; 2、编外]")
    private String insideOrOutside;

    @ExcelField(value = "身份证号", comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String idNum;

    @ExcelField(value = "车辆配发日期", comment = "请以yyyy-MM-dd的格式填写日期")
    private String carAllotmentDate;

    @ExcelField(value = "电池数量")
    private String batteryNumber;

    @ExcelField(value = "电池一更换日期", comment = "请以yyyy-MM-dd的格式填写日期")
    private String batteryReplacementDate1;

    @ExcelField(value = "电池二更换日期", comment = "请以yyyy-MM-dd的格式填写日期")
    private String batteryReplacementDate2;

    @ExcelField(value = "是否临牌", readConverterExp = "是=1,否=2", writeConverterExp = "1=是,2=否", comment = "只可填写以下各项:[1、是; 2、否]")
    private String ifLicense;

    @ExcelField(value = "存储地点")
    private String storageLocation;

    @ExcelField(value = "状态", required = true, readConverterExp = "正常=1,损坏=2", writeConverterExp = "1=正常,2=损坏", comment = "只可填写以下各项:[1、正常; 2、损坏]")
    private String status;
}
