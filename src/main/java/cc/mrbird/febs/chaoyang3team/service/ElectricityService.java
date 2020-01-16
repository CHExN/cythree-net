package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Electricity;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface ElectricityService extends IService<Electricity> {

    IPage<Electricity> findElectricityDetail(QueryRequest request, Electricity electricity);

    void createElectricity(Electricity electricity);

    void updateElectricity(Electricity electricity);

    void deleteElectricity(String[] electricityIds);

}
