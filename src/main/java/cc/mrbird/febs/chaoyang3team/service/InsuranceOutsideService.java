package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceOutside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface InsuranceOutsideService extends IService<InsuranceOutside> {

    IPage<InsuranceOutside> findInsuranceOutsideDetail(QueryRequest request, InsuranceOutside insuranceOutside);

    IPage<InsuranceOutside> findInsuranceOutsideSimplify(QueryRequest request, InsuranceOutside insuranceOutside);

    void createInsuranceOutside(InsuranceOutside insuranceOutside);

    void batchInsertInsuranceOutside(List<InsuranceOutside> insuranceOutsides);

    void updateInsuranceOutside(InsuranceOutside insuranceOutside);

    void deleteInsuranceOutside(String[] insuranceOutsideIds);

    InsuranceInside getInsuranceOutsideByIdNum(String idNum);

}
