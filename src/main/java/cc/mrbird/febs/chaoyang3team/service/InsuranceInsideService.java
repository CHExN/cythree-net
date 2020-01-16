package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface InsuranceInsideService extends IService<InsuranceInside> {

    IPage<InsuranceInside> findInsuranceInsideDetail(QueryRequest request, InsuranceInside insuranceInside);

    IPage<InsuranceInside> findInsuranceInsideSimplify(QueryRequest request, InsuranceInside insuranceInside);

    void createInsuranceInside(InsuranceInside insuranceInside);

    void batchInsertInsuranceInside(List<InsuranceInside> insuranceInsides);

    void updateInsuranceInside(InsuranceInside insuranceInside);

    void deleteInsuranceInside(String[] insuranceInsideIds);

    InsuranceInside getInsuranceInsideByIdNum(String idNum);

}
