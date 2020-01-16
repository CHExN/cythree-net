package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Water;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface WaterService extends IService<Water> {

    IPage<Water> findWaterDetail(QueryRequest request, Water water);

    void createWater(Water water);

    void updateWater(Water water);

    void deleteWater(String[] waterIds);

}
