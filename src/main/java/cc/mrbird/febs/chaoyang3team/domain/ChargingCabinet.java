package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author CHExN
 */
@Data
@TableName("cy_charging_cabinet")
public class ChargingCabinet implements Serializable {

    private static final long serialVersionUID = 8113995834508726634L;

    /**
     * 充电柜主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 资产名称
     */
    @TableField("ASSET_NAME")
    private String assetName;

    /**
     * 品牌型号
     */
    @TableField("BRAND_MODEL")
    private String brandModel;

    /**
     * 配发日期
     */
    @TableField("ALLOTMENT_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate allotmentDate;

    /**
     * 责任人
     */
    @TableField("USER")
    private String user;

    /**
     * 使用部门
     */
    @TableField("USE_DEPT_NAME")
    private String useDeptName;

    /**
     * 充电端口数
     */
    @TableField("CHARGING_SECTIONS_NUMBER")
    private String chargingSectionsNumber;

    /**
     * 能否充电 1能 2否
     */
    @TableField("IF_CHARGE")
    private String ifCharge;

    /**
     * 摆放地点
     */
    @TableField("PLACE")
    private String place;

    /**
     * 备注
     */
    @TableField("REMARK")
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

}
