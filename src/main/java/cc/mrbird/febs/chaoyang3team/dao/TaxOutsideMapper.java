package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.TaxOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface TaxOutsideMapper extends BaseMapper<TaxOutside> {

    IPage<TaxOutside> findTaxOutsideDetail(Page page, @Param("taxOutside") TaxOutside taxOutside);

    Integer findTaxOutsideDetailCount(@Param("taxOutside") TaxOutside taxOutside);

    List<TaxOutside> getTaxOutsideInfoList(@Param("taxOutside") TaxOutside taxOutside);

    // 废弃
    List<TaxOutside> getTaxOutsideReport(@Param("taxOutside") TaxOutside taxOutside);

    // 废弃
    TaxOutside getTaxOutsideAmount(@Param("taxOutside") TaxOutside taxOutside);

}
