package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ApplicationPlan;
import cc.mrbird.febs.chaoyang3team.domain.Plan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface ApplicationPlanService extends IService<ApplicationPlan> {

    void deleteByApplicationId(String[] applicationIds);

    void deleteByPlanId(String[] planIds);

    List<String> findPlanIdsByApplicationIds(String[] applicationIds);

    List<Plan> findPlansByApplicationId(String applicationId);

    void batchInsertApplicationPlan(List<ApplicationPlan> list);

}
