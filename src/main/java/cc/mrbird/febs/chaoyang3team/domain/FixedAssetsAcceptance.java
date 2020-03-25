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
@TableName("cy_fixed_assets_acceptance")
public class FixedAssetsAcceptance implements Serializable {

    private static final long serialVersionUID = 4534047796546873080L;

    /**
     * 固定资产验收表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 合同编号
     */
    @TableField("NUM")
    private String num;

    /**
     * 项目负责人
     */
    @TableField("MANAGER")
    private String manager;

    /**
     * 合同金额
     */
    @TableField("MONEY")
    private BigDecimal money;

    /**
     * 实际金额
     */
    @TableField("MONEY_TRUE")
    private BigDecimal moneyTrue;

    /**
     * 是否有与合同不符的情况 0否 1是
     */
    @TableField("IS_MATCH")
    private String isMatch;

    /**
     * 资产适用性能是否达到要求 0否 1是
     */
    @TableField("IS_APPLICABILITY")
    private String isApplicability;

    /**
     * 资产技术指标是否与合同相符 0否 1是
     */
    @TableField("IS_INDICATORS")
    private String isIndicators;

    /**
     * 资产配件是否与采购要求相符 0否 1是
     */
    @TableField("IS_ACCESSORIES")
    private String isAccessories;

    /**
     * 资产是否全新完好 0否 1是
     */
    @TableField("IS_NEW")
    private String isNew;

    /**
     * 技术文档是否齐全 0否 1是
     */
    @TableField("IS_COMPLETE")
    private String isComplete;

    /**
     * 资产在安装调试、试用过程中的情况
     */
    @TableField("REMARK_O")
    private String remarkO;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 资产存放地点
     */
    @TableField("LOCATION")
    private String location;

    /**
     * 安装完成日期
     */
    @TableField("INSTALL_COMPLETE_DATE")
    private String installCompleteDate;

    /**
     * 验收日期
     */
    @TableField("ACCEPTANCE_DATE")
    private String acceptanceDate;

    /**
     * 验收结果
     */
    @TableField("ACCEPTANCE_RESULT")
    private String acceptanceResult;

    /**
     * 出库单id
     */
    @TableField("STOREROOM_OUT_ID")
    private String storeroomOutId;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String storeroomOutNum;
}
