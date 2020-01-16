package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Insurance;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface InsuranceService extends IService<Insurance> {

    IPage<Insurance> findInsuranceDetail(QueryRequest request, Insurance insurance);

    void createInsurance(Insurance insurance);

    void batchInsertInsurance(List<Insurance> insurances);

    void updateInsurance(Insurance insurance);

    void deleteInsurance(String[] insuranceIds);

    List<Insurance> findInsuranceSummary(Insurance insurance);
}
