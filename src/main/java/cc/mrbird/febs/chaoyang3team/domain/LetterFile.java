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
@TableName("p_letter_file")
public class LetterFile implements Serializable {

    private static final long serialVersionUID = -4875642877761496719L;

    /**
     * 介绍信申请ID
     */
    @TableField("LETTER_ID")
    private Long letterId;

    /**
     * 文件ID
     */
    @TableField("FILE_ID")
    private Long fileId;

}
