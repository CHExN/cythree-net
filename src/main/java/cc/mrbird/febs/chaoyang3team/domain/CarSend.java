package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_car_send")
public class CarSend implements Serializable {

    private static final long serialVersionUID = -1617495506849814675L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 车辆id
     */
    @TableField("CAR_ID")
    private Long carId;

    /**
     * 司机
     */
    @TableField("DRIVER")
    private String driver;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 是否已派车 0未派车 1已派车
     */
    @TableField("STATUS")
    private String status;

    /**
     * 事由
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
     * 用户账号
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Integer deleted;

    // 部门名称
    private transient String deptName;
    // 车牌颜色
    private transient String color;
    // 车辆牌号
    private transient String carNum;

}
