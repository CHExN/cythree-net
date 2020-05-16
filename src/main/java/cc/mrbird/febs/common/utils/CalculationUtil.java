package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * 计算工具类
 */
@Slf4j
public class CalculationUtil {

    public static Integer getAge(String birthday) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date birth = df.parse(birthday);
            Calendar now = Calendar.getInstance();
            Calendar born = Calendar.getInstance();

            now.setTime(new Date());
            born.setTime(birth);

            if (born.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }

            int age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
            return age;
        } catch (Exception e) {
            log.error("根据生日计算年龄异常", e);
            return 0;
        }
    }

    public static String addYear(String date, Integer yearAmount) {
        if (StringUtils.isBlank(date) || yearAmount == null) {
            return null;
        }
        return LocalDate.parse(date).plusYears(yearAmount).toString();
    }
}
