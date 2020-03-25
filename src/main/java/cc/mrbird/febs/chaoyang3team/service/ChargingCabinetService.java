package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ChargingCabinet;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface ChargingCabinetService extends IService<ChargingCabinet> {

    IPage<ChargingCabinet> findChargingCabinetDetail(QueryRequest request, ChargingCabinet chargingCabinet);

    void createChargingCabinet(ChargingCabinet chargingCabinet);

    void updateChargingCabinet(ChargingCabinet chargingCabinet);

    void deleteChargingCabinet(String[] chargingCabinetIds);

    void batchInsertChargingCabinet(List<ChargingCabinet> chargingCabinetList);
}
