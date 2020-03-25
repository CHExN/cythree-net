package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.TaxInside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface TaxInsideService extends IService<TaxInside> {

    IPage<TaxInside> findTaxInsideDetail(QueryRequest request, TaxInside taxInside);

    List<TaxInside> getTaxInsideInfoList(TaxInside taxInside);

    void createTaxInside(TaxInside taxInside);

    void batchInsertTaxInside(List<TaxInside> taxInsides);

    void updateTaxInside(TaxInside taxInside);

    void deleteTaxInside(String[] taxInsideIds);

    // 废弃
    List<TaxInside> getTaxInsideReport(TaxInside taxInside);
}
