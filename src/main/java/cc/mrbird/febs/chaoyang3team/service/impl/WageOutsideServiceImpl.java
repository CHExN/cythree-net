package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WageOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.WageOutside;
import cc.mrbird.febs.chaoyang3team.service.WageOutsideService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@Service("wageOutsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WageOutsideServiceImpl extends ServiceImpl<WageOutsideMapper, WageOutside> implements WageOutsideService {

    @Override
    public IPage<WageOutside> findWageOutsideDetail(QueryRequest request, WageOutside wageOutside) {
        try {
            Page<WageOutside> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            Integer total = this.baseMapper.findWageOutsideDetailCount(wageOutside);
            return total > 0 ? this.baseMapper.findWageOutsideDetail(page, wageOutside).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询工资信息异常", e);
            return null;
        }
    }

    @Override
    public List<WageOutside> getWageOutsideInfoList(WageOutside wageOutside) {
        return this.baseMapper.getWageOutsideInfoList(wageOutside);
    }

    @Override
    @Transactional
    public void createWageOutside(WageOutside wageOutside) {
        wageOutside.setCreateTime(LocalDateTime.now().toString());
        // 应发工资
        wageOutside.setPayable(wageOutside.getCurrentIncome()
                .add(wageOutside.getPostAllowance())
                .add(wageOutside.getSanitationAllowance())
                .add(wageOutside.getDangerousSubsidy())
                .add(wageOutside.getPerformanceBonus())
                .add(wageOutside.getOvertimePay())
                .add(wageOutside.getHolidayCosts())
                .add(wageOutside.getEmptyColumn01())
                .add(wageOutside.getEmptyColumn02())
                .add(wageOutside.getEmptyColumn03())
                .add(wageOutside.getEmptyColumn04())
                .add(wageOutside.getEmptyColumn05())
        );
        // 实发工资
        wageOutside.setRealWage(wageOutside.getPayable()
                .subtract(wageOutside.getBasicPensionIp())
                .subtract(wageOutside.getUnemploymentIp())
                .subtract(wageOutside.getBasicMedicalIp())
                .subtract(wageOutside.getTaxDeduction())
                .subtract(wageOutside.getMembership())
                .subtract(wageOutside.getHousingFund())
                .subtract(wageOutside.getSickLeave())
                .subtract(wageOutside.getEmptyColumn06())
                .subtract(wageOutside.getEmptyColumn07())
                .subtract(wageOutside.getEmptyColumn08())
                .subtract(wageOutside.getEmptyColumn09())
                .subtract(wageOutside.getEmptyColumn10())
        );
        this.save(wageOutside);
    }

    @Override
    @Transactional
    public void batchInsertWageOutside(List<WageOutside> wageOutsides) {
        this.saveBatch(wageOutsides);
    }

    @Override
    @Transactional
    public void updateWageOutside(WageOutside wageOutside) {
        wageOutside.setModifyTime(LocalDateTime.now().toString());
        // 应发工资
        wageOutside.setPayable(wageOutside.getCurrentIncome()
                .add(wageOutside.getPostAllowance())
                .add(wageOutside.getSanitationAllowance())
                .add(wageOutside.getDangerousSubsidy())
                .add(wageOutside.getPerformanceBonus())
                .add(wageOutside.getOvertimePay())
                .add(wageOutside.getHolidayCosts())
                .add(wageOutside.getEmptyColumn01())
                .add(wageOutside.getEmptyColumn02())
                .add(wageOutside.getEmptyColumn03())
                .add(wageOutside.getEmptyColumn04())
                .add(wageOutside.getEmptyColumn05())
        );
        // 实发工资
        wageOutside.setRealWage(wageOutside.getPayable()
                .subtract(wageOutside.getBasicPensionIp())
                .subtract(wageOutside.getUnemploymentIp())
                .subtract(wageOutside.getBasicMedicalIp())
                .subtract(wageOutside.getTaxDeduction())
                .subtract(wageOutside.getMembership())
                .subtract(wageOutside.getHousingFund())
                .subtract(wageOutside.getSickLeave())
                .subtract(wageOutside.getEmptyColumn06())
                .subtract(wageOutside.getEmptyColumn07())
                .subtract(wageOutside.getEmptyColumn08())
                .subtract(wageOutside.getEmptyColumn09())
                .subtract(wageOutside.getEmptyColumn10())
        );
        this.baseMapper.updateById(wageOutside);
    }

    @Override
    @Transactional
    public void deleteWageOutside(String[] wageOutsideIds) {
        List<String> ids = Arrays.asList(wageOutsideIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void deleteAllWageOutside(String year, String month) {
        this.baseMapper.delete(new LambdaQueryWrapper<WageOutside>().eq(WageOutside::getYear, year).eq(WageOutside::getMonth, month));
    }

}
