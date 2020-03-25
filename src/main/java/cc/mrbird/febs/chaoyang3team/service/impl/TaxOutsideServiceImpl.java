package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.TaxOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.TaxOutside;
import cc.mrbird.febs.chaoyang3team.service.TaxOutsideService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CHExN
 */
@Slf4j
@Service("taxOutsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TaxOutsideServiceImpl extends ServiceImpl<TaxOutsideMapper, TaxOutside> implements TaxOutsideService {

    @Override
    public IPage<TaxOutside> findTaxOutsideDetail(QueryRequest request, TaxOutside taxOutside) {
        try {
            Page<TaxOutside> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            Integer total = this.baseMapper.findTaxOutsideDetailCount(taxOutside);

            // 这里做数据处理
            IPage<TaxOutside> taxOutsideDetail = this.baseMapper.findTaxOutsideDetail(page, taxOutside);
            taxOutsideDetail.getRecords().forEach(entity -> {
                // 当前月的月缴税数
                BigDecimal monthTaxPaidNowMonth = calculationMonthTaxPaid(entity.getFinalWageSum())
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                // 从一月到上月的应扣税额集合（未相加的那种）
                List<BigDecimal> finalWageList = Arrays.stream(entity.getFinalWageList().split(","))
                        .map(BigDecimal::new)
                        .collect(Collectors.toList());

                BigDecimal monthTaxPaid = monthTaxPaidNowMonth.subtract(calculationMonthTaxPaid(
                        finalWageList.subList(0, finalWageList.size() == 0 ? 0 : finalWageList.size() - 1)
                                .stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                entity.setMonthTaxPaid(monthTaxPaid.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : monthTaxPaid);
            });

            return total > 0 ? this.baseMapper.findTaxOutsideDetail(page, taxOutside).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询编外个税信息异常", e);
            return null;
        }
    }

    private static BigDecimal calculationMonthTaxPaid(BigDecimal finalWageSum) {
        if (finalWageSum.compareTo(BigDecimal.valueOf(0)) < 1) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal deductionRate; // 扣税率
        BigDecimal quickCalculationDeductions; // 速算扣除数
        // results < 1 表示 results小于相等(-1或0)就为true
        if (finalWageSum.compareTo(BigDecimal.valueOf(36000)) < 1) {
            deductionRate = BigDecimal.valueOf(0.03);
            quickCalculationDeductions = BigDecimal.valueOf(0);
        } else if (finalWageSum.compareTo(BigDecimal.valueOf(144000)) < 1) {
            deductionRate = BigDecimal.valueOf(0.1);
            quickCalculationDeductions = BigDecimal.valueOf(2520);
        } else if (finalWageSum.compareTo(BigDecimal.valueOf(300000)) < 1) {
            deductionRate = BigDecimal.valueOf(0.2);
            quickCalculationDeductions = BigDecimal.valueOf(16920);
        } else if (finalWageSum.compareTo(BigDecimal.valueOf(420000)) < 1) {
            deductionRate = BigDecimal.valueOf(0.25);
            quickCalculationDeductions = BigDecimal.valueOf(31920);
        } else if (finalWageSum.compareTo(BigDecimal.valueOf(660000)) < 1) {
            deductionRate = BigDecimal.valueOf(0.3);
            quickCalculationDeductions = BigDecimal.valueOf(52920);
        } else if (finalWageSum.compareTo(BigDecimal.valueOf(960000)) < 1) {
            deductionRate = BigDecimal.valueOf(0.35);
            quickCalculationDeductions = BigDecimal.valueOf(85920);
        } else {
            deductionRate = BigDecimal.valueOf(0.45);
            quickCalculationDeductions = BigDecimal.valueOf(181920);
        }
        return finalWageSum.multiply(deductionRate).subtract(quickCalculationDeductions);
    }

    @Override
    public List<TaxOutside> getTaxOutsideInfoList(TaxOutside taxOutside) {
        return this.baseMapper.getTaxOutsideInfoList(taxOutside);
    }

    @Override
    @Transactional
    public void createTaxOutside(TaxOutside taxOutside) {
        taxOutside.setCreateTime(LocalDateTime.now());
        taxOutside.setFinalWage(taxOutside.getCurrentIncome()
                .subtract(taxOutside.getCurrentTaxFreeIncome())
                .subtract(taxOutside.getBasicPensionIp())
                .subtract(taxOutside.getBasicMedicalIp())
                .subtract(taxOutside.getUnemploymentIp())
                .subtract(taxOutside.getHousingFund())
                .subtract(taxOutside.getCumulativeChildE())
                .subtract(taxOutside.getCumulativeContinuingE())
                .subtract(taxOutside.getCumulativeHomeLoanInterest())
                .subtract(taxOutside.getCumulativeHousingRent())
                .subtract(taxOutside.getCumulativeElderlySupport())
                .subtract(taxOutside.getCorporateAnnuity())
                .subtract(taxOutside.getCommercialHealthInsurance())
                .subtract(taxOutside.getTaxExtensionPensionInsurance())
                .subtract(taxOutside.getOther())
                .subtract(taxOutside.getAllowanceForDeduction())
                .subtract(taxOutside.getTaxDeduction())
                .subtract(BigDecimal.valueOf(5000))
        );
        this.save(taxOutside);
    }

    @Override
    @Transactional
    public void batchInsertTaxOutside(List<TaxOutside> taxOutsides) {
        this.saveBatch(taxOutsides);
    }

    @Override
    @Transactional
    public void updateTaxOutside(TaxOutside taxOutside) {
        taxOutside.setModifyTime(LocalDateTime.now());
        taxOutside.setFinalWage(taxOutside.getCurrentIncome()
                .subtract(taxOutside.getCurrentTaxFreeIncome())
                .subtract(taxOutside.getBasicPensionIp())
                .subtract(taxOutside.getBasicMedicalIp())
                .subtract(taxOutside.getUnemploymentIp())
                .subtract(taxOutside.getHousingFund())
                .subtract(taxOutside.getCumulativeChildE())
                .subtract(taxOutside.getCumulativeContinuingE())
                .subtract(taxOutside.getCumulativeHomeLoanInterest())
                .subtract(taxOutside.getCumulativeHousingRent())
                .subtract(taxOutside.getCumulativeElderlySupport())
                .subtract(taxOutside.getCorporateAnnuity())
                .subtract(taxOutside.getCommercialHealthInsurance())
                .subtract(taxOutside.getTaxExtensionPensionInsurance())
                .subtract(taxOutside.getOther())
                .subtract(taxOutside.getAllowanceForDeduction())
                .subtract(taxOutside.getTaxDeduction())
                .subtract(BigDecimal.valueOf(5000))
        );
        this.baseMapper.updateById(taxOutside);
    }

    @Override
    @Transactional
    public void deleteTaxOutside(String[] taxOutsideIds) {
        List<String> ids = Arrays.asList(taxOutsideIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<TaxOutside> getTaxOutsideReport(TaxOutside taxOutside) {
        // 查询汇总行
        TaxOutside taxOutsideAmount = this.baseMapper.getTaxOutsideAmount(taxOutside);
        taxOutsideAmount.setStaffIdCard("汇总");
        // 查询其余行
        List<TaxOutside> taxOutsideReport = this.baseMapper.getTaxOutsideReport(taxOutside);
        // 合并
        taxOutsideReport.add(taxOutsideAmount);
        return taxOutsideReport;
    }
}
