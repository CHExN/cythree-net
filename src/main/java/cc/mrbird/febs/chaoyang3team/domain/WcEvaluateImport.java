package cc.mrbird.febs.chaoyang3team.domain;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Excel("公厕评价表")
public class WcEvaluateImport implements Serializable {

    private static final long serialVersionUID = 229395228359545475L;

    @ExcelField(value = "公厕名称")
    private String wcName;
    @ExcelField(value = "公厕编号")
    private transient String wcNum;
    @ExcelField(value = "所在街乡")
    private transient String streetTown;
    @ExcelField(value = "所属分队")
    private transient String wcOwn;
    @ExcelField(value = "类别")
    private transient String wcSortToDict;

    @ExcelField(value = "评价标签")
    private String tag;

    @ExcelField(value = "总体评分")
    private Integer wholeStar;

    @ExcelField(value = "评价时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "微信昵称", writeConverterExp = "0=未知,1=男性,2=女性")
    private String nickName;

    @ExcelField(value = "性别")
    private String gender;

}
