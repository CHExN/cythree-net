package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_charging_cabinet")
@Excel("充电柜明细表")
public class ChargingCabinet implements Serializable {

    private static final long serialVersionUID = 8113995834508726634L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("ASSET_NAME")
    @ExcelField(value = "资产名称")
    private String assetName;

    @TableField("BRAND_MODEL")
    @ExcelField(value = "品牌型号")
    private String brandModel;

    @TableField("ALLOTMENT_DATE")
    @ExcelField(value = "配发日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate allotmentDate;

    @TableField("INSIDE_OR_OUTSIDE")
    @ExcelField(value = "编制类别", readConverterExp = "编内=0,编外=1", writeConverterExp = "0=编内,1=编外", comment = "只可填写以下各项:[1、编内; 2、编外]")
    private String insideOrOutside;

    @TableField("ID_NUM")
    @ExcelField(value = "身份证号", comment = "请填写正确的员工身份证号，否则必定导入失败")
    private String idNum;

    @ExcelField(value = "是否在职", readConverterExp = "在职=0,非在职=1", writeConverterExp = "0=在职,1=非在职", comment = "只可填写以下各项:[1、在职; 2、非在职]")
    private transient String isLeave;

    @ExcelField(value = "责任人")
    private transient String user;

    @ExcelField(value = "使用部门")
    private transient String useDeptName;

    @TableField("CHARGING_SECTIONS_NUMBER")
    @ExcelField(value = "充电端口数")
    private String chargingSectionsNumber;

    @TableField("IF_CHARGE")
    @ExcelField(value = "能否充电", readConverterExp = "能=1,否=2", writeConverterExp = "1=能,2=否", comment = "只可填写以下各项:[1、能; 2、否]")
    private String ifCharge;

    @TableField("PLACE")
    @ExcelField(value = "摆放地点")
    private String place;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Integer deleted;

    // 配发日期开始日期
    private transient String allotmentDateFrom;
    // 配发日期结束日期
    private transient String allotmentDateTo;

}
