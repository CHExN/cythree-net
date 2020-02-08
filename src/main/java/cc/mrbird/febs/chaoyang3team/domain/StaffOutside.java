package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 类型 0head 1北分队 2南分队 3保洁分队
     */
    @TableField("TYPE")
    private String type;

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

    /**
     * 姓名
     */
    @TableField("NAME")
    @ExcelField(value = "姓名")
    private String name;

    /**
     * 性别 0女 1男
     */
    @TableField("GENDER")
    @ExcelField(value = "性别", writeConverterExp = "0=女,1=男")
    private String gender;

    /**
     * 民族
     */
    @TableField("CLAN")
    @ExcelField(value = "民族")
    private String clan;

    /**
     * 籍贯
     */
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

    /**
     * 身份证号码
     */
    @TableField("ID_NUM")
    @ExcelField(value = "身份证号码")
    private String idNum;

    /**
     * 家庭住址
     */
    @TableField("ADDRESS")
    @ExcelField(value = "家庭住址")
    private String address;

    /**
     * 座机联系电话
     */
    @TableField("PHONE_LAND_LINE")
    @ExcelField(value = "座机联系电话")
    private String phoneLandLine;

    /**
     * 手机联系电话
     */
    @TableField("PHONE_CELL")
    @ExcelField(value = "手机联系电话")
    private String phoneCell;

    /**
     * 出生年月
     */
    @TableField("BIRTH_DATE")
    @ExcelField(value = "出生年月")
    private String birthDate;

//    @TableField("AGE")
//    @ExcelField(value = "年龄")
//    private Integer age;

//    @TableField("RETIREMENT_YEAR")
//    @ExcelField(value = "退休年份")
//    private String retirementYear;

    /**
     * 调入环卫或报到日期
     */
    @TableField("TRANSFER_DATE")
    @ExcelField(value = "调入环卫或报到日期")
    private String transferDate;

    /**
     * 到本场队日期
     */
    @TableField("TO_TEAM_DATE")
    @ExcelField(value = "到本场队日期")
    private String toTeamDate;

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

    /**
     * 备注
     */
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

    private transient String mini;
    private transient String max;

}
