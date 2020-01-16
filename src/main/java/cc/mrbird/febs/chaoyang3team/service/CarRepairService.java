package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.CarRepair;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface CarRepairService extends IService<CarRepair> {

    IPage<CarRepair> findRepairDetail(QueryRequest request, CarRepair carRepair);

    void createRepair(CarRepair carRepair, ServletRequest servletRequest);

    void updateRepair(CarRepair carRepair);

    void deleteRepair(String[] repairIds);

}
