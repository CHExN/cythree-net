package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.CarRecord;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface CarRecordService extends IService<CarRecord> {

    IPage<CarRecord> findCarRecordDetail(QueryRequest request, CarRecord carRecord);

    IPage<CarRecord> findCarRecordDetailExcel(QueryRequest request, CarRecord carRecord);

    void createCarRecord(CarRecord carRecord);

    void updateCarRecord(CarRecord carRecord);

    void deleteCarRecord(String[] carRecordIds);

}
