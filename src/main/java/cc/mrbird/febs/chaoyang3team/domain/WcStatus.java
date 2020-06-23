package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_wc_status")
public class WcStatus implements Serializable {

    private static final long serialVersionUID = -1169591263356141632L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 公厕id
     */
    @TableField("WC_ID")
    private Long wcId;
    private transient String wcName;
    private transient String wcNum;
    private transient String wcSort;
    private transient String streetTown;
    private transient String wcOwn;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 关门日期
     */
    @TableField("START_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    private String startDateFrom;
    private String startDateTo;

    /**
     * 三场抽运/开门日期
     */
    @TableField("END_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String endDateFrom;
    private String endDateTo;

    /**
     * 状态 默认“井满”
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 是否通知三场 0不通知 1通知
     */
    @TableField("IS_NOTICE")
    private String isNotice;

    /**
     * 通知日期
     */
    @TableField("NOTICE_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate noticeDate;

    /**
     * 是否重点位置 0否 1是
     */
    @TableField("IS_FOCUS")
    private String isFocus;

    /**
     * 是否开门 0关门 1开门
     */
    @TableField("IS_OPEN")
    private String isOpen;

    private transient String day;
}
