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
@TableName("p_outside_file")
public class OutsideFile implements Serializable {

    private static final long serialVersionUID = 935868018148057905L;

    /**
     * 编外ID
     */
    @TableField("OUTSIDE_ID")
    private Long outsideId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
