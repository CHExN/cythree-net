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
     * 开始日期
     */
    @TableField("START_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    private String startDateFrom;
    private String startDateTo;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String endDateFrom;
    private String endDateTo;

    /**
     * 状态 默认“维修中”
     */
    @TableField("STATUS")
    private String status;


}
