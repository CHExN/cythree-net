package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.TaxInside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface TaxInsideMapper extends BaseMapper<TaxInside> {

    IPage<TaxInside> findTaxInsideDetail(Page page, @Param("taxInside") TaxInside taxInside);

    Integer findTaxInsideDetailCount(@Param("taxInside") TaxInside taxInside);

    List<TaxInside> getTaxInsideInfoList(@Param("taxInside") TaxInside taxInside);

    // 废弃
    List<TaxInside> getTaxInsideReport(@Param("taxInside") TaxInside taxInside);

    // 废弃
    TaxInside getTaxInsideAmount(@Param("taxInside") TaxInside taxInside);

}
