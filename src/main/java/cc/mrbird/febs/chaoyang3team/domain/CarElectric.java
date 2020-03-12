package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author CHExN
 */
@Data
@TableName("cy_car_electric")
public class CarElectric implements Serializable {

    private static final long serialVersionUID = -4204973126784141200L;

    /**
     * 电动车主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 车辆类型
     */
    @TableField("CAR_TYPE")
    private String carType;

    /**
     * 车辆品牌
     */
    @TableField("CAR_BRANDS")
    private String carBrands;

    /**
     * 钢架号
     */
    @TableField("STEEL_FRAME_NUMBER")
    private String steelFrameNumber;

    /**
     * 电机号
     */
    @TableField("MOTOR_NUMBER")
    private String motorNumber;

    /**
     * 车牌号
     */
    @TableField("CAR_NUMBER")
    private String carNumber;

    /**
     * 使用人
     */
    @TableField("USER")
    private String user;

    /**
     * 使用部门
     */
    @TableField("USE_DEPT_NAME")
    private String useDeptName;

    /**
     * 车辆配发日期
     */
    @TableField("CAR_ALLOTMENT_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate carAllotmentDate;

    /**
     * 电池数量
     */
    @TableField("BATTERY_NUMBER")
    private String batteryNumber;

    /**
     * 电池一更换日期
     */
    @TableField("BATTERY_REPLACEMENT_DATE1")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate batteryReplacementDate1;

    /**
     * 电池二更换日期
     */
    @TableField("BATTERY_REPLACEMENT_DATE2")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate batteryReplacementDate2;

    /**
     * 是否临牌 1是 2否
     */
    @TableField("IF_LICENSE")
    private String ifLicense;

    /**
     * 存储地点
     */
    @TableField("STORAGE_LOCATION")
    private String storageLocation;

    /**
     * 状态 1正常 2损坏
     */
    @TableField("STATUS")
    private String status;

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

}
