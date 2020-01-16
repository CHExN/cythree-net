package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.CarRefuel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface CarRefuelMapper extends BaseMapper<CarRefuel> {

    IPage<CarRefuel> findCarRefuel(Page page, @Param("carRefuel") CarRefuel carRefuel);

    List<CarRefuel> findCarRefuelDetail(@Param("carRefuel") CarRefuel carRefuel);

}
