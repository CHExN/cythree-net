package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.UnitConversion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author CHExN
 */
public interface UnitConversionMapper extends BaseMapper<UnitConversion> {

    void saveOrUpdate(UnitConversion unitConversion);

    Long amountIsChange(UnitConversion unitConversion);

}
