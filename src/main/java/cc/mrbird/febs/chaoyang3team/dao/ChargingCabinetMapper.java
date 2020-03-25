package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ChargingCabinet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface ChargingCabinetMapper extends BaseMapper<ChargingCabinet> {

    IPage<ChargingCabinet> findChargingCabinetDetail(Page page, @Param("chargingCabinet") ChargingCabinet chargingCabinet);

    Integer findChargingCabinetDetailCount(@Param("chargingCabinet") ChargingCabinet chargingCabinet);

}
