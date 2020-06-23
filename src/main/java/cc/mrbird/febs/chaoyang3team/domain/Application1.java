package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author CHExN
 */
@Data
@TableName("cy_application")
public class Application1 implements Serializable {

    private static final long serialVersionUID = 865442155653309926L;

    /**
     * 采购申请单id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 物资类别 1保洁物品 2劳保物品 4维修用品 5固定资产 6工会用品 7技安用品 8其他
     */
    @TableField("TYPE_APPLICATION")
    private String typeApplication;
    private transient String typeApplicationAuthority;

    /**
     * 编号
     */
    @TableField("NUM")
    private String num;

    /**
     * 申请的账号
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 申请部门的id
     */
    @TableField("DEPT_ID")
    private Long deptId;
    private transient String deptName;

    /**
     * 申请部门负责人
     */
    @TableField("APP_DEPT")
    private String appDept;

    /**
     * 分管采购工作经办人（经办人）
     */
    @TableField("HANDLE")
    private String handle;

    /**
     * 分管采购工作负责人（采购部门负责人）
     */
    @TableField("PUR_DEPT")
    private String purDept;

    /**
     * 采购说明（采购用途、采购数量、功能要求等）
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 预计金额
     */
    @TableField("MONEY")
    private BigDecimal money;

    /**
     * 采购计划 1内 2外
     */
    @TableField("IS_IN")
    private String isIn;

    /**
     * 处理状态 1算是审核通过 -1是不通过 0是正在审核 2是入库完成
     */
    @TableField("PROCESS")
    private Integer process;

    /**
     * 当前处理步骤
     */
    @TableField("STEP")
    private Integer step;

    /**
     * 记录审核流程的账号名称，如有多人，以逗号分隔
     */
    @TableField("REVIEW")
    private String review;

    /**
     * 申请时日期
     */
    @TableField("CREATE_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createDate;

    /**
     * 是否是固定资产申请 0不是 1是
     */
    @TableField("IS_FIXED_ASSETS")
    private String isFixedAssets;


    private transient String planList;
    private transient String createTimeFrom;
    private transient String createTimeTo;

}
