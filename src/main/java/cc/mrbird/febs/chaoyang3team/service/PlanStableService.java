package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.PlanStable;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface PlanStableService extends IService<PlanStable> {

    IPage<PlanStable> findPlanStable(QueryRequest request, PlanStable planStable);

    List<PlanStable> planStableList();

    void createPlanStable(PlanStable planStable);

    void updatePlanStable(PlanStable planStable);

    void deletePlanStable(String[] planStableIds);

}
