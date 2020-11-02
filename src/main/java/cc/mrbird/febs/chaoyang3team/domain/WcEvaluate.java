package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_wc_evaluate")
public class WcEvaluate implements Serializable {

    private static final long serialVersionUID = 7360448960818022868L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 音频文件主键
     */
    @TableField("VOICE_ID")
    private Long voiceId;
    private transient String voiceAddress;

    /**
     * 照片文件主键
     */
    @TableField("PHOTO_ID")
    private Long photoId;
    private transient String photoAddress;

    /**
     * 总体评分
     */
    @TableField("WHOLE_STAR")
    private Integer wholeStar;

    /**
     * 公厕id
     */
    @TableField("WC_ID")
    private Long wcId;
    private transient String wcName;
    private transient String wcNum;
    private transient String streetTown;
    private transient String wcOwn;
    private transient String wcSort;

    /**
     * 评价内容集/投诉内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 是否为投诉内容 0投诉 1评价
     */
    @TableField("IS_COMPLAINT")
    private String isComplaint;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;


    /**
     * 电话
     */
    @TableField("PHONE")
    private String phone;
    private transient String encryptedData;
    private transient String iv;
    private transient String sessionKey;


    /**
     * 处理状态 1已解决 2未解决 0待解决
     */
    @TableField("PROCESS")
    private String process;
    private transient String processBefore;

    /**
     * 解决时间
     */
    @TableField("PROCESS_TIME")
    private LocalDateTime processTime;
    private transient String processTimeFrom;
    private transient String processTimeTo;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 微信昵称
     */
    @TableField("NIKCNAME")
    private String nickName;

    /**
     * 微信性别 0未知 1男性 2女性
     */
    @TableField("GENDER")
    private String gender;

    /**
     * 微信头像url
     */
    private transient String avatarUrl;
    @TableField("AVATAR_ID")
    private Long avatarId;

    /**
     * 经度
     */
    @TableField("LONGITUDE")
    private String longitude;

    /**
     * 纬度
     */
    @TableField("LATITUDE")
    private String latitude;

}
