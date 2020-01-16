package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcWater;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcWaterMapper extends BaseMapper<WcWater> {

    /**
     * 根据公厕ID查找对应水费信息ID
     *
     * @return 计划单ID
     */
    List<String> findWaterIdsByWcIds(@Param("wcIds") String wcIds);

}
