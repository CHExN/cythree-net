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
@TableName("cy_car_electric")
@Excel("电动车明细表")
public class CarElectric implements Serializable {

    private static final long serialVersionUID = -4204973126784141200L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("CAR_TYPE")
    @ExcelField(value = "车辆类型")
    private String carType;

    @TableField("CAR_BRANDS")
    @ExcelField(value = "车辆品牌")
    private String carBrands;

    @TableField("STEEL_FRAME_NUMBER")
    @ExcelField(value = "钢架号")
    private String steelFrameNumber;

    @TableField("MOTOR_NUMBER")
    @ExcelField(value = "电机号")
    private String motorNumber;

    @TableField("CAR_NUMBER")
    @ExcelField(value = "车牌号")
    private String carNumber;

    @TableField("USER")
    @ExcelField(value = "使用人")
    private String user;

    @TableField("USE_DEPT_NAME")
    @ExcelField(value = "使用部门")
    private String useDeptName;

    @TableField("CAR_ALLOTMENT_DATE")
    @ExcelField(value = "车辆配发日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate carAllotmentDate;

    @TableField("BATTERY_NUMBER")
    @ExcelField(value = "电池数量")
    private String batteryNumber;

    @TableField("BATTERY_REPLACEMENT_DATE1")
    @ExcelField(value = "电池一更换日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate batteryReplacementDate1;

    @TableField("BATTERY_REPLACEMENT_DATE2")
    @ExcelField(value = "电池二更换日期")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate batteryReplacementDate2;

    @TableField("IF_LICENSE")
    @ExcelField(value = "是否临牌", readConverterExp = "是=1,否=2", writeConverterExp = "1=是,2=否", comment = "只可填写以下各项:[1、是; 2、否]")
    private String ifLicense;

    @TableField("STORAGE_LOCATION")
    @ExcelField(value = "存储地点")
    private String storageLocation;

    @TableField("STATUS")
    @ExcelField(value = "状态", required = true, readConverterExp = "正常=1,损坏=2", writeConverterExp = "1=正常,2=损坏", comment = "只可填写以下各项:[1、正常; 2、损坏]")
    private String status;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 是否是三轮 1是 2否
     */
    @TableField("IF_THREE")
    private Integer ifThree;

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


    // 车辆配发日期开始日期
    private transient String carAllotmentDateFrom;
    // 车辆配发日期结束日期
    private transient String carAllotmentDateTo;
    // 电池1更换日期开始日期
    private transient String batteryReplacementDate1From;
    // 电池1更换日期结束日期
    private transient String batteryReplacementDate1To;
    // 电池2更换日期开始日期
    private transient String batteryReplacementDate2From;
    // 电池2更换日期结束日期
    private transient String batteryReplacementDate2To;

}
