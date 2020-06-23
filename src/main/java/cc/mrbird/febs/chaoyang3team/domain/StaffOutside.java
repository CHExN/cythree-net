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
@TableName("cy_staff_outside")
@Excel("编外人员信息")
public class StaffOutside implements Serializable {

    private static final long serialVersionUID = -3851594643320790822L;

    /**
     * 编外人员信息表主键
     */
    @TableId(value = "STAFF_ID", type = IdType.AUTO)
    private Long staffId;

    @TableField("SORT_NUM_1")
    @ExcelField(value = "总序号")
    private String sortNum1;

    @TableField("SORT_NUM_2")
    @ExcelField(value = "分队序号")
    private String sortNum2;

    /**
     * 类型 0head 1北分队 2南分队 3保洁分队 4派遣人员
     */
    @TableField("TYPE")
    private String type;

    /**
     * 事由 内容选择有：在册、增加、解除、终止、退休、亡故
     */
    @TableField("CAUSE")
    @ExcelField(value = "事由")
    private String cause;

    /**
     * 人员类型 内容选择有：临时合同工
     */
    @TableField("TEMPORARY")
    @ExcelField(value = "临时合同工")
    private String temporary;

    /**
     * 分队 获取所有选项
     */
    @TableField("TEAM")
    @ExcelField(value = "分队")
    private String team;

    @TableField("NAME")
    @ExcelField(value = "姓名")
    private String name;

    @TableField("GENDER")
    @ExcelField(value = "性别", writeConverterExp = "0=女,1=男")
    private String gender;

    @TableField("CLAN")
    @ExcelField(value = "民族")
    private String clan;

    @TableField("BIRTHPLACE")
    @ExcelField(value = "籍贯")
    private String birthplace;

    /**
     * 学历 内容选择有：小学、初中、高中、专科、本科、硕士、博士
     */
    @TableField("CULTURE")
    @ExcelField(value = "学历")
    private String culture;

    /**
     * 户籍性质 0外地农业 1外地非农业 2本地农业 3本地非农业
     */
    @TableField("HOUSEHOLD_REGISTRATION_TYPE")
    @ExcelField(value = "户籍性质", writeConverterExp = "0=外地农业,1=外地非农业,2=本地农业,3=本地非农业")
    private String householdRegistrationType;

    /**
     * 政治面貌 内容选择有：群众、团员、党员、预备党员
     */
    @TableField("POLITICAL_FACE")
    @ExcelField(value = "政治面貌")
    private String politicalFace;

    @TableField("ID_NUM")
    @ExcelField(value = "身份证号码")
    private String idNum;

    @TableField("ADDRESS")
    @ExcelField(value = "家庭住址")
    private String address;

    @TableField("PHONE_LAND_LINE")
    @ExcelField(value = "座机联系电话")
    private String phoneLandLine;

    @TableField("PHONE_CELL")
    @ExcelField(value = "手机联系电话")
    private String phoneCell;

    @TableField("BIRTH_DATE")
    @ExcelField(value = "出生年月")
    private String birthDate;

    @ExcelField(value = "年龄")
    private transient Integer age;

    @ExcelField(value = "退休年龄")
    private transient Integer retirementAge;

    @ExcelField(value = "退休日期")
    private transient String retirementDate;

    @TableField("ADD_DATE")
    @ExcelField(value = "增加日期")
    private String addDate;

    @TableField("TRANSFER_DATE")
    @ExcelField(value = "调入环卫或报到日期")
    private String transferDate;

//    @TableField("TO_TEAM_DATE")
//    @ExcelField(value = "到本场队日期")
//    private String toTeamDate;

    /**
     * 岗位（技术专业类别） 获取所有选项
     */
    @TableField("POST")
    @ExcelField(value = "岗位")
    private String post;

    /**
     * 岗位类别（技术专业类别） 获取所有选项
     */
    @TableField("TECHNICAL_TYPE")
    @ExcelField(value = "岗位类别")
    private String technicalType;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

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
    private Integer deleted;

    private transient String mini;
    private transient String max;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String reduceTimeFrom;
    private transient String reduceTimeTo;


}
