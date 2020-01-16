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
@TableName("p_application_file")
public class ApplicationFile implements Serializable {

    private static final long serialVersionUID = 4590387662217436270L;

    /**
     * 采购申请ID
     */
    @TableField("APPLICATION_ID")
    private Long applicationId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
