package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.UnitConversion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface UnitConversionService extends IService<UnitConversion> {

    UnitConversion findById(String id);

    void saveOrUpdateUnitConversion(UnitConversion unitConversion);

}
