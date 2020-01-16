package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcWaterMapper;
import cc.mrbird.febs.chaoyang3team.domain.WcWater;
import cc.mrbird.febs.chaoyang3team.service.WcWaterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("wcWaterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcWaterServiceImpl extends ServiceImpl<WcWaterMapper, WcWater> implements WcWaterService {

    @Override
    @Transactional
    public void deleteByWcId(String[] wcIds) {
        List<String> list = Arrays.asList(wcIds);
        baseMapper.delete(new LambdaQueryWrapper<WcWater>().in(WcWater::getWcId, list));
    }

    @Override
    @Transactional
    public void deleteByWaterId(String[] waterIds) {
        List<String> list = Arrays.asList(waterIds);
        baseMapper.delete(new LambdaQueryWrapper<WcWater>().in(WcWater::getWaterId, list));
    }

    @Override
    public List<String> findWaterIdsByWcIds(String[] wcIds) {
        return this.baseMapper.findWaterIdsByWcIds(StringUtils.join(wcIds, ","));
    }

    @Override
    @Transactional
    public void createWcWater(WcWater wcWater) {
        this.save(wcWater);
    }
}