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
@TableName("p_inside_file")
public class InsideFile implements Serializable {

    private static final long serialVersionUID = 4800043715982517634L;

    /**
     * 编内ID
     */
    @TableField("INSIDE_ID")
    private Long insideId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
