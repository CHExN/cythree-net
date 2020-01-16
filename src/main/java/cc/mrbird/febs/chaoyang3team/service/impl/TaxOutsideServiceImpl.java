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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
            return total > 0 ? this.baseMapper.findTaxOutsideDetail(page, taxOutside).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询编外个税信息异常", e);
            return null;
        }
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
                .subtract(taxOutside.getTaxDeduction()));
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
                .subtract(taxOutside.getTaxDeduction()));
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
