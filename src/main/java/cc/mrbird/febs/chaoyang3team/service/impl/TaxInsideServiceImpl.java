package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.TaxInsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.TaxInside;
import cc.mrbird.febs.chaoyang3team.service.TaxInsideService;
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
@Service("taxInsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TaxInsideServiceImpl extends ServiceImpl<TaxInsideMapper, TaxInside> implements TaxInsideService {

    @Override
    public IPage<TaxInside> findTaxInsideDetail(QueryRequest request, TaxInside taxInside) {
        try {
            Page<TaxInside> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            Integer total = this.baseMapper.findTaxInsideDetailCount(taxInside);

            // 这里做数据处理
            IPage<TaxInside> taxInsideDetail = this.baseMapper.findTaxInsideDetail(page, taxInside);
            taxInsideDetail.getRecords().forEach(entity -> {
                // 当前月的月缴税数
                BigDecimal monthTaxPaidNowMonth = calculationMonthTaxPaid(entity.getFinalWageSum())
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                // 从一月到上月的应扣税额集合（未相加的那种）
                List<BigDecimal> finalWageList = Arrays.stream(entity.getFinalWageList().split(","))
                        .map(BigDecimal::new)
                        .collect(Collectors.toList());

                // 第二种方法
                BigDecimal monthTaxPaid = monthTaxPaidNowMonth.subtract(calculationMonthTaxPaid(
                        finalWageList.subList(0, finalWageList.size() == 0 ? 0 : finalWageList.size() - 1)
                                .stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                entity.setMonthTaxPaid(monthTaxPaid.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : monthTaxPaid);

                // 第一种方法
                /*// 从一月到上月的月缴税数集合
                List<BigDecimal> allMonthTaxPaidList = new ArrayList<>();

                for (int i = 1; i < finalWageList.size(); ++i) {
                    allMonthTaxPaidList.add( // 6.加到集合里
                            calculationMonthTaxPaid( // 3.根据"个人所得税预扣率表一"计算得出相应月的月缴税数（未减以往月的那种）
                                    finalWageList.subList(0, i) // 1.遍历截取从一月到上月应扣税额的合集
                                            .stream().reduce(BigDecimal.ZERO, BigDecimal::add)) // 2.相加得出相应月份最终的应扣税额
                                    .setScale(2, BigDecimal.ROUND_HALF_UP) // 4.四舍五入
                                    .subtract(allMonthTaxPaidList.stream().reduce(BigDecimal.ZERO, BigDecimal::add)) // 5.减去相应月以往月份得出的最终月缴税数，得出相应月最终的应扣税额
                    );
                }
                BigDecimal monthTaxPaid = monthTaxPaidNowMonth.subtract(allMonthTaxPaidList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
                entity.setMonthTaxPaid(monthTaxPaid.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : monthTaxPaid);*/
            });

            return total > 0 ? taxInsideDetail.setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询编内个税信息异常", e);
            return null;
        }
    }

    private static BigDecimal calculationMonthTaxPaid(BigDecimal finalWageSum) {
        if (finalWageSum.compareTo(BigDecimal.valueOf(0)) < 1) {
            return BigDecimal.ZERO;
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
    public List<TaxInside> getTaxInsideInfoList(TaxInside taxInside) {
        return this.baseMapper.getTaxInsideInfoList(taxInside);
    }

    @Override
    @Transactional
    public void createTaxInside(TaxInside taxInside) {
        taxInside.setCreateTime(LocalDateTime.now());
        taxInside.setFinalWage(taxInside.getCurrentIncome()
                .subtract(taxInside.getCurrentTaxFreeIncome())
                .subtract(taxInside.getBasicPensionIp())
                .subtract(taxInside.getBasicMedicalIp())
                .subtract(taxInside.getUnemploymentIp())
                .subtract(taxInside.getHousingFund())
                .subtract(taxInside.getCumulativeChildE())
                .subtract(taxInside.getCumulativeContinuingE())
                .subtract(taxInside.getCumulativeHomeLoanInterest())
                .subtract(taxInside.getCumulativeHousingRent())
                .subtract(taxInside.getCumulativeElderlySupport())
                .subtract(taxInside.getCorporateAnnuity())
                .subtract(taxInside.getCommercialHealthInsurance())
                .subtract(taxInside.getTaxExtensionPensionInsurance())
                .subtract(taxInside.getOther())
                .subtract(taxInside.getAllowanceForDeduction())
                .subtract(taxInside.getTaxDeduction())
                .subtract(BigDecimal.valueOf(5000))
        );
        this.save(taxInside);
    }

    @Override
    @Transactional
    public void batchInsertTaxInside(List<TaxInside> taxInsides) {
        this.saveBatch(taxInsides);
    }

    @Override
    @Transactional
    public void updateTaxInside(TaxInside taxInside) {
        taxInside.setModifyTime(LocalDateTime.now());
        taxInside.setFinalWage(taxInside.getCurrentIncome()
                .subtract(taxInside.getCurrentTaxFreeIncome())
                .subtract(taxInside.getBasicPensionIp())
                .subtract(taxInside.getBasicMedicalIp())
                .subtract(taxInside.getUnemploymentIp())
                .subtract(taxInside.getHousingFund())
                .subtract(taxInside.getCumulativeChildE())
                .subtract(taxInside.getCumulativeContinuingE())
                .subtract(taxInside.getCumulativeHomeLoanInterest())
                .subtract(taxInside.getCumulativeHousingRent())
                .subtract(taxInside.getCumulativeElderlySupport())
                .subtract(taxInside.getCorporateAnnuity())
                .subtract(taxInside.getCommercialHealthInsurance())
                .subtract(taxInside.getTaxExtensionPensionInsurance())
                .subtract(taxInside.getOther())
                .subtract(taxInside.getAllowanceForDeduction())
                .subtract(taxInside.getTaxDeduction())
                .subtract(BigDecimal.valueOf(5000))
        );
        this.baseMapper.updateById(taxInside);
    }

    @Override
    @Transactional
    public void deleteTaxInside(String[] taxInsideIds) {
        List<String> ids = Arrays.asList(taxInsideIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    // 废弃
    @Override
    public List<TaxInside> getTaxInsideReport(TaxInside taxInside) {
        // 查询汇总行
        TaxInside taxInsideAmount = this.baseMapper.getTaxInsideAmount(taxInside);
        taxInsideAmount.setStaffIdCard("汇总");
        // 查询其余行
        List<TaxInside> taxInsideReport = this.baseMapper.getTaxInsideReport(taxInside);
        // 合并
        taxInsideReport.add(taxInsideAmount);
        return taxInsideReport;
    }
}
