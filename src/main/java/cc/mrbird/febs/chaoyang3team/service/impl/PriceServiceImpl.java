package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.PriceMapper;
import cc.mrbird.febs.chaoyang3team.domain.Price;
import cc.mrbird.febs.chaoyang3team.service.PriceService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("priceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements PriceService {

    @Override
    public List<Price> findByName(String name) {
        return baseMapper.selectList(new LambdaQueryWrapper<Price>().likeRight(Price::getName, name));
    }

    @Override
    public IPage<Price> findPrices(QueryRequest request, Price price) {
        try {
            LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<>();

            if (price.getId() != null) {
                queryWrapper.eq(Price::getId, price.getId());
            }
            if (StringUtils.isNotBlank(price.getName())) {
                queryWrapper.like(Price::getName, price.getName());
            }

            Page<Price> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取物品价格信息失败", e);
            return null;
        }
    }


    @Override
    @Transactional
    public void createPrice(Price price) {
        this.save(price);
    }

    @Override
    @Transactional
    public void updatePrice(Price price) {
        this.baseMapper.updateById(price);
    }

    @Override
    @Transactional
    public void deletePrices(String[] priceIds) {
        List<String> list = Arrays.asList(priceIds);
        this.baseMapper.deleteBatchIds(list);
    }

}
