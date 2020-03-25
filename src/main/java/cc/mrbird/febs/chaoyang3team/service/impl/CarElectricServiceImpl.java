package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.CarElectricMapper;
import cc.mrbird.febs.chaoyang3team.domain.CarElectric;
import cc.mrbird.febs.chaoyang3team.service.CarElectricService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("carElectricService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarElectricServiceImpl extends ServiceImpl<CarElectricMapper, CarElectric> implements CarElectricService {

    @Override
    public IPage<CarElectric> findCarElectricDetail(QueryRequest request, CarElectric carElectric) {
        try {
            Page<CarElectric> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page,"id", FebsConstant.ORDER_ASC, false);
            Integer total = this.baseMapper.findCarElectricDetailCount(carElectric);
            return total > 0 ? this.baseMapper.findCarElectricDetail(page, carElectric).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询电动车信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createCarElectric(CarElectric carElectric) {
        carElectric.setCreateTime(LocalDateTime.now());
        this.save(carElectric);
    }

    @Override
    @Transactional
    public void updateCarElectric(CarElectric carElectric) {
        carElectric.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(carElectric);
    }

    @Override
    @Transactional
    public void deleteCarElectric(String[] carElectricIds) {
        List<String> list = Arrays.asList(carElectricIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public void batchInsertCarElectric(List<CarElectric> carElectricList) {
        this.saveBatch(carElectricList);
    }
}
