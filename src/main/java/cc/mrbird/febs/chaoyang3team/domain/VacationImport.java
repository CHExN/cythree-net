package cc.mrbird.febs.chaoyang3team.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端InsideAnnualLeave页面用来接收数据的格式 与 导出模板
 * @author CHExN
 */
@Data
public class VacationImport implements Serializable {

    private static final long serialVersionUID = 2156117631180795547L;

    /**
     * 人员id
     */
    private Long staffId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 参工日期
     */
    private String workDate;

    /**
     * 已休天数
     */
    private Integer alreadyDays;

    /**
     * 以下1~12月
     */
    private String january;

    private String february;

    private String march;

    private String april;

    private String may;

    private String june;

    private String july;

    private String august;

    private String september;

    private String october;

    private String november;

    private String december;

}
