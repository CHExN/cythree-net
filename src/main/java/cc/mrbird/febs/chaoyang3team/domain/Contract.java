package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author CHExN
 */
@Data
@TableName("cy_contract")
public class Contract implements Serializable {

    private static final long serialVersionUID = 4488900940381395859L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 填制日期(发起时间)
     */
    @TableField("CREATE_DATE")
    private String createDate;

    /**
     * 申请部门的id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 申请部门名称
     */
    @TableField("DEPT_NAME")
    private String deptName;

    /**
     * 发起方账号
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 合同名称
     */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /**
     * 合同对方(合同伙伴)
     */
    @TableField("CONTRACT_PARTNER")
    private String contractPartner;

    /**
     * 合同约定事项是否经过招标 1是 0否
     */
    @TableField("IS_TENDER")
    private String isTender;

    /**
     * 合同编号
     */
    @TableField("CONTRACT_NUM")
    private String contractNum;

    /**
     * 合同金额(元)
     */
    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    /**
     * 处理状态 1算是审核通过 -1是不通过 0是正在审核
     */
    @TableField("PROCESS")
    private Integer process;

    /**
     * 联合审核的处理步骤
     */
    /*@TableField("STEP_JOIN")
    private Integer stepJoin;*/

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
     * 财务审核是否通过 1通过 0审核
     */
    @TableField("IS_FINANCE")
    private Integer isFinance;

    /**
     * 法务与关联部门是否审核通过(发起部门联系) 1通过 0审核
     */
    @TableField("IS_INITIATE")
    private Integer isInitiate;

    /**
     * 文件id
     */
    @TableField("FILE_ID")
    private Long fileId;

    /**
     * 是联合审核 还是 逐一审核 1联合审核 0逐一审核
     */
    /*@TableField("IS_JOIN")
    private String isJoin;*/

    /**
     * 合同承办部门意见
     */
    @TableField("OPINION_START")
    private String opinionStart;

    /**
     * 法务审核意见
     */
    @TableField("OPINION_LEGAL")
    private String opinionLegal;

    /**
     * 财务审核意见
     */
    @TableField("OPINION_FINANCIAL")
    private String opinionFinancial;

    /**
     * 关联部门意见
     */
    @TableField("OPINION_ASS")
    private String opinionAss;

    /**
     * 合同管理部门意见(办公室)
     */
    @TableField("OPINION_OFFICE")
    private String opinionOffice;

    /**
     * 合同承办部门主管领导意见(副队长 or 工会队长)
     */
    @TableField("OPINION_HEAD")
    private String opinionHead;

    /**
     * 合同管理部门主管领导意见(副队长)
     */
    @TableField("OPINION_VICE_CAPTAIN")
    private String opinionViceCaptain;

    /**
     * 单位负责人意见
     */
    @TableField("OPINION_CAPTAIN")
    private String opinionCaptain;

    /**
     * 其他情况说明(合同承办部门)
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 合同承办部门意见时间
     */
    @TableField("DATE_START")
    private String dateStart;

    /**
     * 法务审核意见时间
     */
    @TableField("DATE_LEGAL")
    private String dateLegal;

    /**
     * 财务审核意见时间
     */
    @TableField("DATE_FINANCIAL")
    private String dateFinancial;

    /**
     * 关联部门意见时间
     */
    @TableField("DATE_ASS")
    private String dateAss;

    /**
     * 合同管理部门意见时间(办公室)
     */
    @TableField("DATE_OFFICE")
    private String dateOffice;

    /**
     * 合同承办部门主管领导意见时间(副队长 or 工会队长)
     */
    @TableField("DATE_HEAD")
    private String dateHead;

    /**
     * 合同管理部门主管领导意见时间(副队长)
     */
    @TableField("DATE_VICE_CAPTAIN")
    private String dateViceCaptain;

    /**
     * 单位负责人意见时间
     */
    @TableField("DATE_CAPTAIN")
    private String dateCaptain;

    /**
     * 文件地址
     */
    private transient String address;

}
