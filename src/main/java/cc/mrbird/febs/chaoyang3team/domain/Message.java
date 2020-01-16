package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 通知消息
 *
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("t_message")
public class Message implements Serializable {


    private static final long serialVersionUID = 9221858962969325609L;
    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField("DATETIME")
    private String datetime;

    /**
     * 消息体
     */
    @TableField("MESSAGE")
    private String message;

    /**
     * 寄信人
     */
    @TableField("SENDER")
    private String sender;

    /**
     * 寄信人部门
     */
    @TableField("SENDER_DEPT")
    private String senderDept;

    /**
     * 收信人
     */
    @TableField("ADDRESSEE")
    private String addressee;

    /**
     * 已读未读 1未读2已读
     */
    @TableField("STATUS")
    private String status;

}
