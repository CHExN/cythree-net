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
@TableName("cy_staff_inside")
@Excel("编内人员信息")
public class StaffInside implements Serializable {

    private static final long serialVersionUID = -174492978968781822L;

    /**
     * 编内人员信息表主键
     */
    @TableId(value = "STAFF_ID", type = IdType.AUTO)
    private Long staffId;

    @TableField("TYPE")
    @ExcelField(value = "人员类型", writeConverterExp = "0=固定工,1=合同制")
    private String type;

    /**
     * 事由 内容选择有：在册、增加、解除、终止、退休、亡故
     */
    @TableField("CAUSE")
    @ExcelField(value = "事由")
    private String cause;

    /**
     * 旧事由，更新时判断更改的事由与旧事由是否相等，相等就一并更新事由改变事由的时间 （废弃）
     */
    private transient String lowCause;

    /**
     * 事由改变事由的时间（废弃）
     */
    @TableField("CAUSE_DATE")
    private String causeDate;

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
     * 原学历 内容选择有：小学、初中、高中、专科、本科、硕士、博士
     */
    @TableField("CULTURE_BEFORE")
    @ExcelField(value = "原学历")
    private String cultureBefore;

    /**
     * 现学历 内容选择有：小学、初中、高中、专科、本科、硕士、博士
     */
    @TableField("CULTURE_CURRENT")
    @ExcelField(value = "现学历")
    private String cultureCurrent;

    @TableField("HOUSEHOLD_REGISTRATION_TYPE")
    @ExcelField(value = "户籍性质", writeConverterExp = "0=外地农业,1=外地非农业,2=本地农业,3=本地非农业")
    private String householdRegistrationType;

    @TableField("PROFESSION")
    @ExcelField(value = "专业")
    private String profession;

    @TableField("GRADUATION_DATE")
    @ExcelField(value = "毕业日期")
    private String graduationDate;

    /**
     * 政治面貌 内容选择有：群众、团员、党员、预备党员
     */
    @TableField("POLITICAL_FACE")
    @ExcelField(value = "政治面貌")
    private String politicalFace;

    @TableField("IS_DISABILITY_CERTIFICATE")
    @ExcelField(value = "有无残疾证", writeConverterExp = "0=无,1=有")
    private String isDisabilityCertificate;

    @TableField("WORK_INJURY_CERTIFICATE_DATE")
    @ExcelField(value = "工伤证发证日期")
    private String workInjuryCertificateDate;

    @TableField("DISABILITY_IDENTIFICATION_LEVEL")
    @ExcelField(value = "伤残鉴定等级")
    private String disabilityIdentificationLevel;

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

//    @TableField("AGE")
//    @ExcelField(value = "年龄")
//    private Integer age;

//    @TableField("RETIREMENT_YEAR")
//    @ExcelField(value = "退休年份")
//    private String retirementYear;

    @TableField("WORK_DATE")
    @ExcelField(value = "参加工作日期")
    private String workDate;

    @TableField("FARMER_WORK_DATE")
    @ExcelField(value = "农转工转工日期")
    private String farmerWorkDate;

    @TableField("TRANSFER_DATE")
    @ExcelField(value = "调入环卫或报到日期")
    private String transferDate;

    @TableField("TO_TEAM_DATE")
    @ExcelField(value = "到本场队日期")
    private String toTeamDate;

    /**
     * 现任岗位职务
     */
//    @TableField("CURRENT_POSITION")
//    @ExcelField(value = "现任岗位职务")
//    private String currentPosition;

    /**
     * 岗位类别（技术专业类别） 获取所有选项
     */
//    @TableField("TECHNICAL_TYPE")
//    @ExcelField(value = "岗位类别")
//    private String technicalType;

    /**
     * 现任岗位职务 获取所有选项
     */
    @TableField("TECHNICAL_TYPE")
    @ExcelField(value = "现任岗位职务")
    private String technicalType;


    /**
     * 岗位 0管理 1技工 2普工 3专技
     */
    @TableField("POST")
    @ExcelField(value = "岗位")
    private String post;

    @TableField("POST_LEVEL")
    @ExcelField(value = "岗位级别")
    private String postLevel;

    @TableField("APPOINTMENT_DATE")
    @ExcelField(value = "聘任时间")
    private String appointmentDate;

    @TableField("TECHNICAL_LEVEL_DATE")
    @ExcelField(value = "技术等级取得日期")
    private String technicalLevelDate;

    @TableField("CERTIFICATE_NUM")
    @ExcelField(value = "证书编号")
    private String certificateNum;

    /**
     * 入职状态 获取所有选项
     */
    @TableField("ENTRY_STATUS")
    @ExcelField(value = "入职状态")
    private String entryStatus;

    /**
     * 排序序号
     */
    @TableField("SORT_NUM")
    private String sortNum;

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

    @TableField("IS_LEAVE")
    @ExcelField(value = "在职或非在职", writeConverterExp = "0=在职,1=非在职")
    private String isLeave;

    /**
     * 离职日期
     */
    @TableField(value = "LEAVE_DATE", strategy = FieldStrategy.NOT_EMPTY)
    private String leaveDate;

    /**
     * 部门id
     */
    @TableField("DEPT_ID")
    private Long deptId;

    @ExcelField(value = "部门")
    private transient String deptName;

    private transient String fileNum;

    private transient String mini;
    private transient String max;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
