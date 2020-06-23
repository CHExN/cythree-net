package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.Water;
import cc.mrbird.febs.chaoyang3team.dao.WaterMapper;
import cc.mrbird.febs.chaoyang3team.domain.WcWater;
import cc.mrbird.febs.chaoyang3team.service.WaterService;
import cc.mrbird.febs.chaoyang3team.service.WcWaterService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("waterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WaterServiceImpl extends ServiceImpl<WaterMapper, Water> implements WaterService {

    @Autowired
    private WcWaterService wcWaterService;

    @Override
    public IPage<Water> findWaterDetail(QueryRequest request, Water water) {
        try {
            Page<Water> page = new Page<>();
            SortUtil.handlePageSort(request, page, "year DESC, month DESC, wcId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findWaterDetail(page, water);
        } catch (Exception e) {
            log.error("查询水费信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createWater(Water water) {
        water.setCreateDate(new Date());
        if (water.getMonth().length() == 1) water.setMonth("0" + water.getMonth());
        this.save(water);
        this.wcWaterService.createWcWater(new WcWater(water.getWcId(), water.getWaterId()));
    }

    @Override
    @Transactional
    public void updateWater(Water water) {
        this.baseMapper.updateById(water);
        if (water.getWcId() != null) {
            // 先删除，后重新添加
            this.wcWaterService.deleteByWaterId(water.getWaterId().toString().split(","));
            this.wcWaterService.createWcWater(new WcWater(water.getWcId(), water.getWaterId()));
        }
    }

    @Override
    @Transactional
    public void deleteWater(String[] waterIds) {
        this.wcWaterService.deleteByWaterId(waterIds);
        List<String> ids = Arrays.asList(waterIds);
        this.baseMapper.deleteBatchIds(ids);
    }
}
