package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.Wage;
import cc.mrbird.febs.chaoyang3team.dao.WageMapper;
import cc.mrbird.febs.chaoyang3team.service.WageService;
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
@Service("wageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WageServiceImpl extends ServiceImpl<WageMapper, Wage> implements WageService {

    @Override
    public IPage<Wage> findWageDetail(QueryRequest request, Wage wage) {
        try {
            Page<Wage> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, "IS_LEAVE ASC, sortNum", FebsConstant.ORDER_ASC, false);
            Integer total = this.baseMapper.findWageDetailCount(wage);
            return total > 0 ? this.baseMapper.findWageDetail(page, wage).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询编内工资信息异常", e);
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
        // 应发工资
        wage.setPayable(wage.getCurrentIncome()
                .add(wage.getReissueSalaryScale())
                .add(wage.getSalarySalary())
                .add(wage.getPostAllowance())
                .add(wage.getFinancialBurdenPerformancePay())
                .add(wage.getOvertimePay())
                .add(wage.getEnvironmentalSanitationDutyAllowance())
                .add(wage.getHousingSubsidy())
                .add(wage.getOnlyChildFee())
                .add(wage.getTemporarySubsidy())
                .add(wage.getJobPerformance())
                .add(wage.getHolidayCosts())
                .add(wage.getAnnualLeavePay())
                .add(wage.getComprehensiveSubsidy())
                .add(wage.getEmptyColumn01())
                .add(wage.getEmptyColumn02())
                .add(wage.getEmptyColumn03())
                .add(wage.getEmptyColumn04())
                .add(wage.getEmptyColumn05())
        );
        // 实发工资
        wage.setRealWage(wage.getPayable()
                .subtract(wage.getHousingFund())
                .subtract(wage.getBasicPensionIp())
                .subtract(wage.getUnemploymentIp())
                .subtract(wage.getBasicMedicalIp())
                .subtract(wage.getMedicalMutualAid())
                .subtract(wage.getCorporateAnnuity())
                .subtract(wage.getTaxDeduction())
                .subtract(wage.getEmptyColumn06())
                .subtract(wage.getEmptyColumn07())
                .subtract(wage.getEmptyColumn08())
                .subtract(wage.getEmptyColumn09())
                .subtract(wage.getEmptyColumn10())
        );
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
        // 应发工资
        wage.setPayable(wage.getCurrentIncome()
                .add(wage.getReissueSalaryScale())
                .add(wage.getSalarySalary())
                .add(wage.getPostAllowance())
                .add(wage.getFinancialBurdenPerformancePay())
                .add(wage.getOvertimePay())
                .add(wage.getEnvironmentalSanitationDutyAllowance())
                .add(wage.getHousingSubsidy())
                .add(wage.getOnlyChildFee())
                .add(wage.getTemporarySubsidy())
                .add(wage.getJobPerformance())
                .add(wage.getHolidayCosts())
                .add(wage.getAnnualLeavePay())
                .add(wage.getComprehensiveSubsidy())
                .add(wage.getEmptyColumn01())
                .add(wage.getEmptyColumn02())
                .add(wage.getEmptyColumn03())
                .add(wage.getEmptyColumn04())
                .add(wage.getEmptyColumn05())
        );
        // 实发工资
        wage.setRealWage(wage.getPayable()
                .subtract(wage.getHousingFund())
                .subtract(wage.getBasicPensionIp())
                .subtract(wage.getUnemploymentIp())
                .subtract(wage.getBasicMedicalIp())
                .subtract(wage.getMedicalMutualAid())
                .subtract(wage.getCorporateAnnuity())
                .subtract(wage.getTaxDeduction())
                .subtract(wage.getEmptyColumn06())
                .subtract(wage.getEmptyColumn07())
                .subtract(wage.getEmptyColumn08())
                .subtract(wage.getEmptyColumn09())
                .subtract(wage.getEmptyColumn10())
        );
        this.baseMapper.updateById(wage);
    }

    @Override
    @Transactional
    public void deleteWage(String[] wageIds) {
        List<String> ids = Arrays.asList(wageIds);
        this.baseMapper.deleteBatchIds(ids);
    }


}
