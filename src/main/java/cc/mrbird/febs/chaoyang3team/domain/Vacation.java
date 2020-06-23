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
@TableName("cy_vacation")
public class Vacation implements Serializable {

    private static final long serialVersionUID = 6214821502825557312L;

    /**
     * 休假表主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 编内还是编外 0编内 1编外归属 2编外分队 4归属人员
     */
    @TableField("INS_OUT")
    private String insOut;

    /**
     * 人员id
     */
    @TableField("STAFF_ID")
    private Long staffId;
    private transient String sortNum;
    private transient String name;
    private transient String[] names;
    private transient String idNum;
    private transient String isLeave;
    /**
     * 参工日期
     */
    private transient String workDate;

    /**
     * 是否为假期 0是 1不是 (病假，事假，年假是假期，离职，入编等不是假期)
     */
    @TableField("IS_VACATION")
    private String isVacation;

    /**
     * 休假类型 (病假，事假，年假，离职，入编等)
     */
    @TableField("TYPE")
    private String type;

    /**
     * 天
     */
    @TableField("DAY")
    private Integer day;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 休假日期
     */
    @TableField("DATE")
    private String date;

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
     * 已休天数
     */
    private transient Integer alreadyDays;

    /**
     * 以下1~12月
     */
    private transient String month1;
    private transient String month1Remark;

    private transient String month2;
    private transient String month2Remark;

    private transient String month3;
    private transient String month3Remark;

    private transient String month4;
    private transient String month4Remark;

    private transient String month5;
    private transient String month5Remark;

    private transient String month6;
    private transient String month6Remark;

    private transient String month7;
    private transient String month7Remark;

    private transient String month8;
    private transient String month8Remark;

    private transient String month9;
    private transient String month9Remark;

    private transient String month10;
    private transient String month10Remark;

    private transient String month11;
    private transient String month11Remark;

    private transient String month12;
    private transient String month12Remark;

}
