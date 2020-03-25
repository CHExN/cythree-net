package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.CarSend;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface CarSendService extends IService<CarSend> {

    IPage<CarSend> findCarSend(QueryRequest request, CarSend carSend, ServletRequest servletRequest);

    void createCarSend(CarSend carSend, ServletRequest servletRequest);

    void updateCarSend(CarSend carSend);

    void deleteCarSend(String[] carSendIds);

}
