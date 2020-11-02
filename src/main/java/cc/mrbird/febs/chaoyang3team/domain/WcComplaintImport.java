package cc.mrbird.febs.chaoyang3team.domain;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Excel("公厕投诉表")
public class WcComplaintImport  implements Serializable {

    private static final long serialVersionUID = 3398521645092296706L;

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

    @ExcelField(value = "投诉内容")
    private String content;

    @ExcelField(value = "电话")
    private String phone;

    @ExcelField(value = "处理状态", writeConverterExp = "1=已解决,2=未解决,0=待解决")
    private String process;

    @ExcelField(value = "评价时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "备注")
    private String remark;

    @ExcelField(value = "微信昵称", writeConverterExp = "0=未知,1=男性,2=女性")
    private String nickName;

    @ExcelField(value = "性别")
    private String gender;

}
