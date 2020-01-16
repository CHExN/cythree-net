package cc.mrbird.febs.common.validator;

import com.wuwenze.poi.util.ValidatorUtil;
import com.wuwenze.poi.validator.Validator;

public class NumericValidator implements Validator {
    @Override
    public String valid(Object value) {
        String valueString = (String) value;
        if (valueString.equals("$EMPTY_CELL$")) return null;
        return ValidatorUtil.isNumeric(valueString) ? null : "[" + valueString + "]不是正确的浮点数或者整数.";
    }
}
