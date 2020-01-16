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
@TableName("cy_car")
@Excel("朝阳3队车辆信息表")
public class Car implements Serializable {

    private static final long serialVersionUID = 136516731815098961L;

    /**
     * 主键
     */
    @TableId(value = "CAR_ID", type = IdType.AUTO)
    private Long carId;

    @TableField("CAR_NUM")
    @ExcelField(value = "车牌号")
    private String carNum;

    @TableField("CAR_TYPE")
    @ExcelField(value = "车辆品牌")
    private String carType;

    @TableField("COLOR")
    @ExcelField(value = "车牌颜色", writeConverterExp = "1=黄,2=蓝")
    private String color;

    @TableField("CAR_DISPLACEMENT")
    @ExcelField(value = "汽车排量")
    private String carDisplacement;

    @TableField("CAR_KIND")
    @ExcelField(value = "车辆种类")
    private String carKind;

    @TableField("DATE")
    @ExcelField(value = "车辆日期")
    private String date;

    @TableField("CAR_USE")
    @ExcelField(value = "车辆用途")
    private String carUse;

    @TableField("CAR_USE_UNIT")
    @ExcelField(value = "车辆使用单位")
    private String carUseUnit;

    @TableField("CAR_UNIT")
    @ExcelField(value = "车辆所属单位")
    private String carUnit;

    @TableField("CAR_NUM_OLD")
    @ExcelField(value = "原始牌照")
    private String carNumOld;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    @TableField("STATUS")
    @ExcelField(value = "车辆状态", writeConverterExp = "1=行驶,2=停驶")
    private String status;

    /**
     * 加油卡卡号
     */
    @TableField("CARD_NUM")
    private String cardNum;

}
