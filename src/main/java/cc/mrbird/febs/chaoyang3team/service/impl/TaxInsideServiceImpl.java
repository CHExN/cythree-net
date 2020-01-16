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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
            return total > 0 ? this.baseMapper.findTaxInsideDetail(page, taxInside).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询编内个税信息异常", e);
            return null;
        }
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
                .subtract(taxInside.getTaxDeduction()));
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
                .subtract(taxInside.getTaxDeduction()));
        this.baseMapper.updateById(taxInside);
    }

    @Override
    @Transactional
    public void deleteTaxInside(String[] taxInsideIds) {
        List<String> ids = Arrays.asList(taxInsideIds);
        this.baseMapper.deleteBatchIds(ids);
    }

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
