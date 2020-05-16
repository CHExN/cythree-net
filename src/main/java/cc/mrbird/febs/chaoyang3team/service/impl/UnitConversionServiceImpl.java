package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.UnitConversionMapper;
import cc.mrbird.febs.chaoyang3team.domain.UnitConversion;
import cc.mrbird.febs.chaoyang3team.service.UnitConversionService;
import cc.mrbird.febs.chaoyang3team.service.WcStoreroomService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CHExN
 */
@Slf4j
@Service("unitConversion")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UnitConversionServiceImpl extends ServiceImpl<UnitConversionMapper, UnitConversion> implements UnitConversionService {

    @Autowired
    private WcStoreroomService wcStoreroomService;

    @Override
    public UnitConversion findById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public void saveOrUpdateUnitConversion(UnitConversion unitConversion) {
        // 如果原先数量不为空
        if (unitConversion.getAmountOriginal() != null) {
            // 计算原先数量 * 转换的数量
            unitConversion.setAmount(unitConversion.getAmountOriginal().multiply(unitConversion.getAmountConversion()));
        }
        // 这里加上判断 如果是 更新&&转换数量不一样||新增 ，就把原先已经分配过的数据全部删除，因为原先的单位与转换数量改变了，更新后会导致分配记录的数量不对等
        if (this.baseMapper.amountIsChange(unitConversion) > 0
                || this.baseMapper.selectCount(
                new LambdaQueryWrapper<UnitConversion>()
                        .eq(UnitConversion::getStoreroomId, unitConversion.getStoreroomId())) == 0) {
            // System.out.println("amountIsChange > 0");
            this.wcStoreroomService.deleteWcStoreroomsByStoreroomId(new String[]{unitConversion.getStoreroomId().toString()});
            unitConversion.setAmountDist(unitConversion.getAmountOriginal().multiply(unitConversion.getAmountConversion()));
        }
        // System.out.println(unitConversion);
        this.baseMapper.saveOrUpdate(unitConversion);
    }
}
