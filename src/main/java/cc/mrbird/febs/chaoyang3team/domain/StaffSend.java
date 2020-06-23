package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CHExN
 */
@Data
@TableName("cy_staff_send")
@Excel("劳务派遣人员信息")
public class StaffSend implements Serializable {

    private static final long serialVersionUID = 8822337335325327567L;

    /**
     * 编外人员信息表主键
     */
    @TableId(value = "STAFF_ID", type = IdType.AUTO)
    private Long staffId;

    @TableField("SORT_NUM")
    @ExcelField(value = "序号")
    private String sortNum;

    @TableField("NAME")
    @ExcelField(value = "姓名")
    private String name;

    @TableField("GENDER")
    @ExcelField(value = "性别", writeConverterExp = "0=女,1=男")
    private String gender;

    @TableField("BIRTHPLACE")
    @ExcelField(value = "籍贯")
    private String birthplace;

    /**
     * 学历 内容选择有：小学、初中、高中、专科、本科、硕士、博士
     */
    @TableField("CULTURE")
    @ExcelField(value = "学历")
    private String culture;

    @TableField("ID_NUM")
    @ExcelField(value = "身份证号码")
    private String idNum;

    @TableField("PHONE_CELL")
    @ExcelField(value = "手机联系电话")
    private String phoneCell;

    @TableField("BIRTH_DATE")
    @ExcelField(value = "出生年月")
    private String birthDate;

    @ExcelField(value = "年龄")
    private transient Integer age;

    @TableField("ADD_DATE")
    @ExcelField(value = "增加日期")
    private String addDate;

    @TableField("TRANSFER_DATE")
    @ExcelField(value = "调入环卫或报到日期")
    private String transferDate;

    @TableField("BANK_CARD_NUMBER")
    @ExcelField(value = "银行卡号")
    private String bankCardNumber;

    /**
     * 获取所有项
     */
    @TableField("BANK_CARD_ATTRIBUTION")
    @ExcelField(value = "银行卡归属")
    private String bankCardAttribution;

    /**
     * 获取所有项
     */
    @TableField("COMPANY")
    @ExcelField(value = "公司")
    private String company;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

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
     * 在职或非在职 0在职 1非在职
     */
    @TableField("IS_LEAVE")
    private String isLeave;

    /**
     * 离职日期
     */
    @TableField("LEAVE_DATE")
    private String leaveDate;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @TableLogic
    @TableField("DELETED")
    private Boolean deleted;

    private transient String mini;
    private transient String max;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String reduceTimeFrom;
    private transient String reduceTimeTo;

}
