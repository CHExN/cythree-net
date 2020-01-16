package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("p_application_plan")
public class ApplicationPlan implements Serializable {

    private static final long serialVersionUID = -2985088425805789358L;

    /**
     * 采购申请单id
     */
    @TableField("APPLICATION_ID")
    private Long applicationId;

    /**
     * 采购计划单id
     */
    @TableField("PLAN_ID")
    private Long planId;

}
