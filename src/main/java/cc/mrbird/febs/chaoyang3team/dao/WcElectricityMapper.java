package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcElectricity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcElectricityMapper extends BaseMapper<WcElectricity> {

    /**
     * 根据公厕ID查找对应电费费信息ID
     *
     * @return 计划单ID
     */
    List<String> findElectricityIdsByWcIds(@Param("wcIds") String wcIds);

}
