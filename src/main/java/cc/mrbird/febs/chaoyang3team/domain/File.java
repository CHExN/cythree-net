package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("t_file")
public class File implements Serializable {

    private static final long serialVersionUID = -4203391818318638996L;
    /**
     * 文件id
     */
    @TableId(value = "FILE_ID", type = IdType.AUTO)
    private Long fileId;

    /**
     * 文件地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 文件类型/后缀名
     */
    @TableField("TYPE")
    private String type;


}
