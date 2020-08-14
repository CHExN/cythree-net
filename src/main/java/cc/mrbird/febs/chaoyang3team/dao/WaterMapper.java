package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Water;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author CHExN
 */
public interface WaterMapper extends BaseMapper<Water> {

    IPage<Water> findWaterDetail(Page page, @Param("water") Water water);

    Long findWaterIdByDateAndWcId(
            @Param("year") String year,
            @Param("month") String month,
            @Param("wcId") Long wcId,
            @Param("totalAmount") BigDecimal totalAmount
    );

}
