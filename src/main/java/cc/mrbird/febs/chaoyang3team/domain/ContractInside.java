package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@TableName("cy_contract_inside")
@Excel("编内人员合同信息")
public class ContractInside implements Serializable {

    private static final long serialVersionUID = 6677499621687552162L;

    /**
     * 编内人员合同信息表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ExcelField(value = "序号")
    private transient String sortNum;

    @ExcelField(value = "姓名")
    private transient String name;

    @TableField("ID_NUM")
    @ExcelField(value = "身份证号")
    private String idNum;

    // 废弃
//    /**
//     * 档案编号 内容选择有：编内合同制、编内固定工
//     */
//    @TableField("FILE_NUM")
//    @ExcelField(value = "档案编号")
//    private String fileNum;

    // 废弃
//    @TableField("BAD_NUM")
//    @ExcelField(value = "胸牌号码")
//    private String badNum;

    /**
     * 无固定期 0有1无
     */
    @TableField("IS_FIXED_PERIOD")
    private String isFixedPeriod;

    @TableField("REMARK_RENEW")
    @ExcelField(value = "续签备注")
    private String remarkRenew;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;


    @TableField("CONTRACT_PERIOD")
    @ExcelField(value = "合同期")
    private String contractPeriod;

    @TableField("CONTRACT_PERIOD_DATE")
    @ExcelField(value = "合同期日期")
    private String contractPeriodDate;

    @TableField("JOB_AGREEMENT")
    @ExcelField(value = "职位协议")
    private String jobAgreement;

    @TableField("JOB_AGREEMENT_DATE")
    @ExcelField(value = "职位协议日期")
    private String jobAgreementDate;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Integer deleted;

    private transient Long staffId;
    private transient String isLeave;

    // 合同开始的开始时间
    private transient String contractPeriodFromFrom;
    // 合同开始的结束时间
    private transient String contractPeriodFromTo;
    // 合同结束的开始时间
    private transient String contractPeriodToFrom;
    // 合同结束的结束时间
    private transient String contractPeriodToTo;

    // 职位协议开始的开始时间
    private transient String jobAgreementFromFrom;
    // 职位协议开始的结束时间
    private transient String jobAgreementFromTo;
    // 职位协议结束的开始时间
    private transient String jobAgreementToFrom;
    // 职位协议结束的结束时间
    private transient String jobAgreementToTo;
}
