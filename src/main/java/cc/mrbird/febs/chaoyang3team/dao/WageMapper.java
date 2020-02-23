package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Wage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WageMapper extends BaseMapper<Wage> {

    IPage<Wage> findWageDetail(Page page, @Param("wage") Wage wage);

    Integer findWageDetailCount(@Param("wage") Wage wage);

    List<Wage> getWageInfoList(@Param("wage") Wage wage);

}
