package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcWater;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcWaterService extends IService<WcWater> {

    void deleteByWcId(String[] wcIds);

    void deleteByWaterId(String[] waterIds);

    List<String> findWaterIdsByWcIds(String[] wcIds);

    void createWcWater(WcWater wcWater);

}
