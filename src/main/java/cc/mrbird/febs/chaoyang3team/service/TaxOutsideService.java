package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.TaxOutside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface TaxOutsideService extends IService<TaxOutside> {

    IPage<TaxOutside> findTaxOutsideDetail(QueryRequest request, TaxOutside taxOutside);

    List<TaxOutside> getTaxOutsideInfoList(TaxOutside taxOutside);

    void createTaxOutside(TaxOutside taxOutside);

    void batchInsertTaxOutside(List<TaxOutside> taxOutsides);

    void updateTaxOutside(TaxOutside taxOutside);

    void deleteTaxOutside(String[] taxOutsideIds);

    List<TaxOutside> getTaxOutsideReport(TaxOutside taxOutside);
}
