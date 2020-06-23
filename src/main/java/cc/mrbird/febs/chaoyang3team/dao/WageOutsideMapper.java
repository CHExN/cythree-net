package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WageOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WageOutsideMapper extends BaseMapper<WageOutside> {

    IPage<WageOutside> findWageOutsideDetail(Page page, @Param("wageOutside") WageOutside wageOutside);

    Integer findWageOutsideDetailCount(@Param("wageOutside") WageOutside wageOutside);

    List<WageOutside> getWageOutsideInfoList(@Param("wageOutside") WageOutside wageOutside);

}
