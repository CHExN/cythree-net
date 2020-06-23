package cc.mrbird.febs.chaoyang3team.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公厕表导入模板
 * @author CHExN
 */
@Data
@Excel("公厕信息表模板")
public class WcImport implements Serializable {

    private static final long serialVersionUID = -1974697350148511217L;

    @ExcelField(value = "公厕名称", required = true)
    private String wcName;

    @ExcelField(value = "所在区县", readConverterExp = "朝阳区=1", writeConverterExp = "1=朝阳区", comment = "只可填写以下各项:1、朝阳区")
    private String district;

    @ExcelField(value = "管理单位", readConverterExp = "朝环三队=1", writeConverterExp = "1=朝环三队", comment = "只可填写以下各项:1、朝环三队")
    private String manageUnit;

    @ExcelField(value = "公厕编号", required = true)
    private String wcNum;

    @ExcelField(value = "经度")
    private String longitude;

    @ExcelField(value = "纬度")
    private String latitude;

    @ExcelField(value = "种类", readConverterExp = "公共厕所=1", writeConverterExp = "1=公共厕所", comment = "只可填写以下各项:1、公共厕所")
    private String wcKind;

    @ExcelField(value = "建设方式", readConverterExp = "独立式(固定)=1", writeConverterExp = "1=独立式(固定)", comment = "只可填写以下各项:1、独立式(固定)")
    private String buildingMethod;

    @ExcelField(value = "建筑类型", readConverterExp = "无=0,生态=1", writeConverterExp = "0=无,1=生态", comment = "只可填写以下各项:1、无; 2、生态")
    private String buildingType;

    @ExcelField(value = "有无管理间")
    private String isManageRoom;

    @ExcelField(value = "类别", readConverterExp = "一类=1,二类=2,三类=3", writeConverterExp = "1=一类,2=二类,3=三类", comment = "只可填写以下各项:1、一类; 2、二类; 3、三类")
    private String wcSort;

    @ExcelField(value = "冲洗方式")
    private String flushingMethod;

    @ExcelField(value = "指标状况")
    private String indicatorStatus;

    @ExcelField(value = "所在街乡")
    private String streetTown;

    @ExcelField(value = "五环内外")
    private String is5thRing;

    @ExcelField(value = "投入使用日期", dateFormat = "yyyy-MM-dd", comment = "请以yyyy-MM-dd的格式填写日期")
    private Date useDate;

    @ExcelField(value = "接管日期", dateFormat = "yyyy-MM-dd", comment = "请以yyyy-MM-dd的格式填写日期")
    private Date replaceDate;

    @ExcelField(value = "初建投资(万元)")
    private String initialMoney;

    @ExcelField(value = "建筑面积(平方米)")
    private String wcArea;

    @ExcelField(value = "有无产权")
    private String isPropertyRight;

    @ExcelField(value = "洗手盆数量")
    private String sink;

    @ExcelField(value = "男坑位数")
    private String pitMale;

    @ExcelField(value = "女坑位数")
    private String pitFemale;

    @ExcelField(value = "无性别坑位数")
    private String pitSexless;

    @ExcelField(value = "小便器类型")
    private String urinalsType;

    @ExcelField(value = "小便器数量")
    private String urinalsNum;

    @ExcelField(value = "有无无障碍设施")
    private String isAccessibility;

    @ExcelField(value = "导向牌数")
    private String indicatorNum;

    @ExcelField(value = "有无残疾间")
    private String isDisabledRoom;

    @ExcelField(value = "粪便处理方式")
    private String fecalMethod;

    @ExcelField(value = "小便器长度")
    private String urinalsLen;

    @ExcelField(value = "开放时间")
    private String openHour;

    @ExcelField(value = "作业方式")
    private String assignmentStyle;

    @ExcelField(value = "有无标志牌")
    private String isIndicator;

    @ExcelField(value = "所属分队")
    private String wcOwn;

    @ExcelField(value = "管理形式", readConverterExp = "自管=0,委托=1", writeConverterExp = "0=自管,1=委托", comment = "只可填写以下各项:1、自管; 2、委托")
    private String manageType;

    @ExcelField(value = "保洁人数")
    private String cleanNum;

    @ExcelField(value = "公厕详细地址")
    private String wcAddress;

    @ExcelField(value = "公厕现在的状态")
    private String wcNowStatus;
}
