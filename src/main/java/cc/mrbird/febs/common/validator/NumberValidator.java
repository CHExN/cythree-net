package cc.mrbird.febs.common.validator;

import com.wuwenze.poi.util.ValidatorUtil;
import com.wuwenze.poi.validator.Validator;

public class NumberValidator implements Validator {
    @Override
    public String valid(Object value) {
        String valueString = (String) value;
        return ValidatorUtil.isNumber(valueString) ? null : "[" + valueString + "]不是正确的数字.";
    }
}
