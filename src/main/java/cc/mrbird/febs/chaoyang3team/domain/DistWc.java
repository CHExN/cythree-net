package cc.mrbird.febs.chaoyang3team.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

@Data
@Excel("公厕简略表")
public class DistWc implements Serializable {

    private static final long serialVersionUID = 92453953911223179L;

    @ExcelField(value = "公厕名称")
    private String wcName;

    @ExcelField(value = "公厕编号")
    private String wcNum;

    @ExcelField(value = "所属分队")
    private String wcOwn;
}
