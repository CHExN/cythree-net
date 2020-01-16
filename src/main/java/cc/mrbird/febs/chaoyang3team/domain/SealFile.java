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
@TableName("p_seal_file")
public class SealFile implements Serializable {

    private static final long serialVersionUID = 4281846576018698180L;

    /**
     * 印章申请ID
     */
    @TableField("SEAL_ID")
    private Long sealId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
