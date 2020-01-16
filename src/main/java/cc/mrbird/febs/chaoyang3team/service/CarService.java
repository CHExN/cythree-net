package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Car;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface CarService extends IService<Car> {

    IPage<Car> findCar(QueryRequest request, Car car);

    List<Car> getCarSimplify();

    void createCar(Car car);

    void updateBatchCar(String cars);

    void updateCar(Car car);

    void deleteCar(String[] carIds);

    List<String> getCarKind();

    List<String> getCarUse();

    /**
     * 检查车辆生日
     */
    void handleCarBirthday();

}
