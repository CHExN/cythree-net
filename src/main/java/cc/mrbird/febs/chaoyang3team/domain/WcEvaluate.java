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
     * 环境评分
     */
    @TableField("ENVIRONMENT_STAR")
    private Integer environmentStar;

    /**
     * 保洁评分
     */
    @TableField("CLEAN_STAR")
    private Integer cleanStar;

    /**
     * 设施评分
     */
    @TableField("INSTALLATION_STAR")
    private Integer installationStar;

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

    /**
     * 评价/投诉 内容
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
     * 电话
     */
    @TableField("PHONE")
    private String phone;

}
