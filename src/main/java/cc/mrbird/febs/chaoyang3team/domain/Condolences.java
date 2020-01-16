package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 职工慰问登记
 * @author CHExN
 */
@Data
@TableName("cy_condolences")
public class Condolences implements Serializable {

    private static final long serialVersionUID = -7252716734243248216L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 职工姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 性别
     */
    @TableField("SEXX")
    private String sexx;

    /**
     * 年龄
     */
    @TableField("AGE")
    private Integer age;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;
    private transient String deptName;

    /**
     * 地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 联系电话
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 情况说明 （疾病名称、病发时间、是否手术等）
     */
    @TableField("TEXT")
    private String text;

    /**
     * 插入日期
     */
    @TableField("DATE")
    private LocalDate date;

    /**
     * 审核状态 1待审核 2审核通过 3审核未通过
     */
    @TableField("STATUS")
    private String status;

    /**
     * 申请的账号
     */
    @TableField("USERNAME")
    private String username;

}
