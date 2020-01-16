package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.WcElectricity;
import cc.mrbird.febs.chaoyang3team.dao.WcElectricityMapper;
import cc.mrbird.febs.chaoyang3team.service.WcElectricityService;
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
@Service("wcElectricityService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcElectricityServiceImpl extends ServiceImpl<WcElectricityMapper, WcElectricity> implements WcElectricityService {

    @Override
    @Transactional
    public void deleteByWcId(String[] wcIds) {
        List<String> list = Arrays.asList(wcIds);
        baseMapper.delete(new LambdaQueryWrapper<WcElectricity>().in(WcElectricity::getWcId, list));
    }

    @Override
    @Transactional
    public void deleteByElectricityId(String[] electricityIds) {
        List<String> list = Arrays.asList(electricityIds);
        baseMapper.delete(new LambdaQueryWrapper<WcElectricity>().in(WcElectricity::getElectricityId, list));
    }

    @Override
    public List<String> findElectricityIdsByWcIds(String[] wcIds) {
        return this.baseMapper.findElectricityIdsByWcIds(StringUtils.join(wcIds, ","));
    }

    @Override
    @Transactional
    public void createWcElectricity(WcElectricity wcElectricity) {
        this.save(wcElectricity);
    }
}
