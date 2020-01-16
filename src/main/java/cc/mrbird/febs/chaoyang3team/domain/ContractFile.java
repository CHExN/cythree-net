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
@TableName("p_contract_file")
public class ContractFile implements Serializable {

    private static final long serialVersionUID = 9110562261460502411L;

    /**
     * 印章申请ID
     */
    @TableField("CONTRACT_ID")
    private Long contractId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
