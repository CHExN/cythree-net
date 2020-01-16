package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcElectricity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcElectricityService extends IService<WcElectricity> {

    void deleteByWcId(String[] wcIds);

    void deleteByElectricityId(String[] electricityIds);

    List<String> findElectricityIdsByWcIds(String[] wcIds);

    void createWcElectricity(WcElectricity wcElectricity);

}
