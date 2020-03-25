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
@TableName("cy_fire_extinguisher")
@Excel("灭火器明细表")
public class FireExtinguisher implements Serializable {

    private static final long serialVersionUID = -1730542505392846534L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("ASSET_NAME")
    @ExcelField(value = "资产名称")
    private String assetName;

    @TableField("BRAND_MODEL")
    @ExcelField(value = "品牌型号")
    private String brandModel;

    @TableField("ALLOTMENT_DATE1")
    @ExcelField(value = "配发日期1")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate allotmentDate1;

    @TableField("ALLOTMENT_DATE2")
    @ExcelField(value = "配发日期2")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate allotmentDate2;

    @TableField("USER")
    @ExcelField(value = "责任人")
    private String user;

    @TableField("TEST_DATE1")
    @ExcelField(value = "检测日期1")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate testDate1;

    @TableField("TEST_DATE2")
    @ExcelField(value = "检测日期2")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate testDate2;

    @TableField("WC_NUM")
    @ExcelField(value = "公厕编号")
    private String wcNum;
    private transient String wcName;

    @TableField("SPECIFICATION1")
    @ExcelField(value = "规格1")
    private String specification1;

    @TableField("SPECIFICATION2")
    @ExcelField(value = "规格2")
    private String specification2;

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

    // 配发日期1开始日期
    private transient String allotmentDate1From;
    // 配发日期1结束日期
    private transient String allotmentDate1To;
    // 配发日期2开始日期
    private transient String allotmentDate2From;
    // 配发日期2结束日期
    private transient String allotmentDate2To;
    // 检测日期1开始日期
    private transient String testDate1From;
    // 检测日期1结束日期
    private transient String testDate1To;
    // 检测日期2开始日期
    private transient String testDate2From;
    // 检测日期2结束日期
    private transient String testDate2To;

}
