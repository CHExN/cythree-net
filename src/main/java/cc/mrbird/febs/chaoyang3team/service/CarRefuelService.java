package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.CarRefuel;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface CarRefuelService extends IService<CarRefuel> {

    IPage<CarRefuel> findCarRefuel(QueryRequest request, CarRefuel carRefuel);

    List<CarRefuel> findCarRefuelDetail(CarRefuel carRefuel);

    void createCarRefuel(String carRefuels);

    void updateCarRefuel(String carRefuels);

//    void deleteCarRefuel(String[] carRefuelIds);

    void deleteCarRefuel(String[] carDate);

}
