package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@TableName("cy_car_record")
@Excel("朝阳3队车辆维修保养记录表")
public class CarRecord implements Serializable {

    private static final long serialVersionUID = 439994075538075276L;

    /**
     * 车辆维修保养表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 车辆id
     */
    @TableField("CAR_ID")
    private Long carId;

    @TableField("DATE")
    @ExcelField(value = "记录时间")
    private String date;

    @ExcelField(value = "车牌号")
    private transient String carNum;

    /**
     * 车辆颜色
     */
    private transient String color;

    @TableField("CAUSE")
    @ExcelField(value = "送检事由")
    private String cause;

    @ExcelField(value = "送检前/后车辆状况")
    private String typeName;

    @TableField("REPORTER")
    @ExcelField(value = "维修方")
    private String reporter;

    @TableField("DIRVER")
    @ExcelField(value = "驾驶员")
    private String dirver;

    /**
     * 送检前/后车辆状况 1仪表 2刹车 3转向 4灯光 5外观 6轮胎 7其他
     */
    @TableField("TYPE")
    private String type;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
