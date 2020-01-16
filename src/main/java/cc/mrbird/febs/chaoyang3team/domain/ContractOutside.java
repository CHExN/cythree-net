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
@TableName("cy_contract_outside")
@Excel("编外人员合同信息")
public class ContractOutside implements Serializable {

    private static final long serialVersionUID = -7772118415640995630L;

    /**
     * 编外人员合同信息表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ExcelField(value = "姓名")
    private transient String name;

    @TableField("ID_NUM")
    @ExcelField(value = "身份证号")
    private String idNum;

    @TableField("REMARK_ON_FIXED_PERIOD")
    @ExcelField(value = "无固定期备注")
    private String remarkOnFixedPeriod;

    @TableField("CONTRACT_LABOR_START")
    @ExcelField(value = "劳动合同起始日期")
    private String contractLaborStart;

    @TableField("CONTRACT_LABOR_END")
    @ExcelField(value = "劳动合同结束日期")
    private String contractLaborEnd;

    @TableField("JOB_AGREEMENT_START")
    @ExcelField(value = "岗位协议起始日期")
    private String jobAgreementStart;

    @TableField("JOB_AGREEMENT_END")
    @ExcelField(value = "岗位协议结束日期")
    private String jobAgreementEnd;

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

}
