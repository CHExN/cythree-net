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
@TableName("cy_car_repair")
@Excel("朝阳3队车辆维修申请表")
public class CarRepair implements Serializable {

    private static final long serialVersionUID = 1918278409495145610L;

    /**
     * 车辆报修申请表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 报修车辆id
     */
    @TableField("CAR_ID")
    private Long carId;

    @ExcelField(value = "车牌号")
    private transient String carNum;

    /**
     * 车辆颜色
     */
    private transient String color;

    @TableField("DATE")
    @ExcelField(value = "报修时间")
    private String date;

    @TableField("REMARK")
    @ExcelField(value = "报修故障描述")
    private String remark;

    @TableField("DIRVER")
    @ExcelField(value = "驾驶员")
    private String dirver;

    @TableField("REPORTER")
    @ExcelField(value = "报修人")
    private String reporter;

    /**
     * 报修部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DEPT_NAME")
    @ExcelField(value = "部门")
    private String deptName;

    @TableField("PROCESS")
    @ExcelField(value = "审核状态", writeConverterExp = "0=待审核,-1=不通过,1=通过")
    private Integer process;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
