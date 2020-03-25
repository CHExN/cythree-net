package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.CarElectric;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * @author CHExN
 */
public interface CarElectricService extends IService<CarElectric> {

    IPage<CarElectric> findCarElectricDetail(QueryRequest request, CarElectric carElectric);

    void createCarElectric(CarElectric carElectric);

    void updateCarElectric(CarElectric carElectric);

    void deleteCarElectric(String[] carElectricIds);

    void batchInsertCarElectric(List<CarElectric> carElectricList);

}
