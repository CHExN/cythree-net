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
@TableName("cy_contract_inside")
@Excel("编内人员合同信息")
public class ContractInside implements Serializable {

    private static final long serialVersionUID = 6677499621687552162L;

    /**
     * 编内人员合同信息表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private transient String name;

    /**
     * 档案编号 内容选择有：编内合同制、编内固定工
     */
    @TableField("FILE_NUM")
    @ExcelField(value = "档案编号")
    private String fileNum;

    /**
     * 胸牌号码
     */
    @TableField("BAD_NUM")
    @ExcelField(value = "胸牌号码")
    private String badNum;

    /**
     * 无固定期 0有1无
     */
    @TableField("IS_FIXED_PERIOD")
    private String isFixedPeriod;

    /**
     * 劳动合同起始日期
     */
    @TableField("CONTRACT_LABOR_START")
    @ExcelField(value = "劳动合同起始日期")
    private String contractLaborStart;

    /**
     * 劳动合同结束日期
     */
    @TableField("CONTRACT_LABOR_END")
    @ExcelField(value = "劳动合同结束日期")
    private String contractLaborEnd;

    /**
     * 岗位协议起始日期
     */
    @TableField("JOB_AGREEMENT_START")
    @ExcelField(value = "岗位协议起始日期")
    private String jobAgreementStart;

    /**
     * 岗位协议结束日期
     */
    @TableField("JOB_AGREEMENT_END")
    @ExcelField(value = "岗位协议结束日期")
    private String jobAgreementEnd;

    /**
     * 续签备注
     */
    @TableField("REMARK_RENEW")
    @ExcelField(value = "续签备注")
    private String remarkRenew;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 人员id
     */
    @TableField("STAFF_ID")
    private Long staffId;
}
