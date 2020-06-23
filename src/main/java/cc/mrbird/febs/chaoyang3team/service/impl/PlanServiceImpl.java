package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.PlanMapper;
import cc.mrbird.febs.chaoyang3team.domain.Plan;
import cc.mrbird.febs.chaoyang3team.service.ApplicationPlanService;
import cc.mrbird.febs.chaoyang3team.service.PlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("planService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    @Autowired
    private ApplicationPlanService applicationPlanService;

    @Override
    public List<Plan> findPlans(Plan plan) {
        try {
            return baseMapper.selectList(new QueryWrapper<Plan>().orderByDesc("id"));
        } catch (Exception e) {
            log.error("获取计划信息失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void batchInsertPlan(List<Plan> list) {
        this.saveBatch(list);
    }

    @Override
    @Transactional
    public void deletePlans(String[] planIds) {
        List<String> ids = Arrays.asList(planIds);
        this.baseMapper.deleteBatchIds(ids);
        this.applicationPlanService.deleteByPlanId(planIds);
    }

    @Override
    public void deletePlansByApplicationIds(String[] applicationIds) {
        List<String> ids = Arrays.asList(applicationIds);
        baseMapper.deletePlansByApplicationIds(StringUtils.join(ids,","));
        this.applicationPlanService.deleteByApplicationId(applicationIds);
    }

    @Override
    @Transactional
    public void updatePlanStatus(String planIds) {
        this.baseMapper.updatePlanStatus(planIds);
    }

}
