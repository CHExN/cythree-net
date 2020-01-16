package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Plan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface PlanService extends IService<Plan> {

    List<Plan> findPlans(Plan plan);

    void batchInsertPlan(List<Plan> plan);

    void deletePlans(String[] planIds);

}
