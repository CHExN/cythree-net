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
@TableName("p_send_file")
public class SendFile implements Serializable {

    private static final long serialVersionUID = 1792710719803879822L;

    /**
     * 编外ID
     */
    @TableField("SEND_ID")
    private Long sendId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
