package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author CHExN
 */
@Data
@TableName("cy_wc")
@Excel("公厕信息表")
public class Wc implements Serializable {

    private static final long serialVersionUID = -4884347372476598458L;

    @TableId(value = "WC_ID", type = IdType.AUTO)
    private Long wcId;

    @TableField(condition = "%s LIKE CONCAT('%%',#{%s},'%%')")
    @ExcelField(value = "公厕名称")
    private String wcName;

    @NotBlank(message = "{required}")
    private String district;
    @ExcelField(value = "所在区县")
    private transient String districtToDict;

    @NotBlank(message = "{required}")
    private String manageUnit;
    @ExcelField(value = "管理单位")
    private transient String manageUnitToDict;

    @ExcelField(value = "公厕编号")
    private String wcNum;

    @ExcelField(value = "经度")
    private String longitude;

    @ExcelField(value = "纬度")
    private String latitude;

    @NotBlank(message = "{required}")
    private String wcKind;
    @ExcelField(value = "种类")
    private transient String wcKindToDict;

    @NotBlank(message = "{required}")
    private String buildingMethod;
    @ExcelField(value = "建设方式")
    private transient String buildingMethodToDict;

    @NotBlank(message = "{required}")
    private String buildingType;
    @ExcelField(value = "建筑类型")
    private transient String buildingTypeToDict;

    @ExcelField(value = "有无管理间")
    private String isManageRoom;

    @NotBlank(message = "{required}")
    private String wcSort;
    @ExcelField(value = "类别")
    private transient String wcSortToDict;

    @ExcelField(value = "冲洗方式")
    private String flushingMethod;

    @ExcelField(value = "指标状况")
    private String indicatorStatus;

    @ExcelField(value = "所在街乡")
    private String streetTown;

    @ExcelField(value = "五环内外")
    private String is5thRing;

    @ExcelField(value = "投入使用日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate useDate;

    @ExcelField(value = "接管日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // @DatetimeFormat是将String转换成Date，一般前台给后台传值时用
    private LocalDate replaceDate;

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

    @NotBlank(message = "{required}")
    private String manageType;
    @ExcelField(value = "管理形式")
    private transient String manageTypeToDict;

    @ExcelField(value = "保洁人数")
    private String cleanNum;

    @ExcelField(value = "公厕详细地址")
    private String wcAddress;

    @ExcelField(value = "公厕现在的状态")
    private String wcNowStatus;

    @ExcelField(value = "水表编号")
    private String waterNum;

    @ExcelField(value = "电表编号")
    private String electricityNum;

    @ExcelField(value = "缴费号")
    private String paymentNum;


    private transient String a;
    private transient BigDecimal b;
    private transient BigDecimal c;
    private transient BigDecimal d;
    private transient BigDecimal e;
    private transient BigDecimal f;
    private transient BigDecimal g;
    private transient BigDecimal h;
    private transient BigDecimal i;
    private transient BigDecimal j;
    private transient String k;
    private transient String l;
    private transient String m;
    private transient String n;
    private transient String o;
    private transient String p;

}
