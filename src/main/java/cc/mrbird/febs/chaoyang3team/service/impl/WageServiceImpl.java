package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.Wage;
import cc.mrbird.febs.chaoyang3team.dao.WageMapper;
import cc.mrbird.febs.chaoyang3team.service.WageService;
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
@Service("wageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WageServiceImpl extends ServiceImpl<WageMapper, Wage> implements WageService {

    @Override
    public IPage<Wage> findWageDetail(QueryRequest request, Wage wage) {
        try {
            Page<Wage> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            Integer total = this.baseMapper.findWageDetailCount(wage);
            return total > 0 ? this.baseMapper.findWageDetail(page, wage).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询工资信息异常", e);
            return null;
        }
    }

    @Override
    public List<Wage> getWageInfoList(Wage wage) {
        return this.baseMapper.getWageInfoList(wage);
    }

    @Override
    @Transactional
    public void createWage(Wage wage) {
        wage.setCreateTime(LocalDateTime.now().toString());
        this.save(wage);
    }

    @Override
    @Transactional
    public void batchInsertWage(List<Wage> wages) {
        this.saveBatch(wages);
    }

    @Override
    @Transactional
    public void updateWage(Wage wage) {
        wage.setModifyTime(LocalDateTime.now().toString());
        this.baseMapper.updateById(wage);
    }

    @Override
    @Transactional
    public void deleteWage(String[] wageIds) {
        List<String> ids = Arrays.asList(wageIds);
        this.baseMapper.deleteBatchIds(ids);
    }

}
