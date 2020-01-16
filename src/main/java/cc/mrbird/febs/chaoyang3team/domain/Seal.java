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
@TableName("cy_seal")
@Excel("申请印章使用汇总")
public class Seal implements Serializable {

    private static final long serialVersionUID = 8966253041609080865L;

    /**
     * 主键
     */
    @TableId(value = "SEAL_ID", type = IdType.AUTO)
    private Long sealId;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DEPT_NAME")
    @ExcelField(value = "部门名称")
    private String deptName;

    @TableField("REMARK")
    @ExcelField(value = "印章使用事由")
    private String remark;

    @TableField("SEAL_USER")
    @ExcelField(value = "印章使用人")
    private String sealUser;

    /**
     * 创建日期
     */
    @TableField("DATETIME")
    private String dateTime;

    @TableField("AMOUNT")
    @ExcelField(value = "份数")
    private Integer amount;

    /**
     * 处理状态 1算是审核通过 -1是不通过 0是正在审核
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

    @TableField("USERNAME")
    @ExcelField(value = "申请人")
    private String username;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
