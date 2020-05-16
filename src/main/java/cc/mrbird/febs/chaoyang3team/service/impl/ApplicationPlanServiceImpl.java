package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ApplicationPlanMapper;
import cc.mrbird.febs.chaoyang3team.domain.ApplicationPlan;
import cc.mrbird.febs.chaoyang3team.domain.Plan;
import cc.mrbird.febs.chaoyang3team.service.ApplicationPlanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("applicationPlanService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationPlanServiceImpl extends ServiceImpl<ApplicationPlanMapper, ApplicationPlan> implements ApplicationPlanService {

    @Override
    @Transactional
    public void deleteByApplicationId(String[] applicationIds) {
        List<String> list = Arrays.asList(applicationIds);
        baseMapper.delete(new LambdaQueryWrapper<ApplicationPlan>().in(ApplicationPlan::getApplicationId, list));
    }

    @Override
    @Transactional
    public void deleteByPlanId(String[] planIds) {
        List<String> list = Arrays.asList(planIds);
        baseMapper.delete(new LambdaQueryWrapper<ApplicationPlan>().in(ApplicationPlan::getPlanId, list));
    }

    @Override
    public List<String> findPlanIdsByApplicationIds(String[] applicationIds) {
        return this.baseMapper.findPlanIdsByApplicationIds(StringUtils.join(applicationIds, ","));
    }

    @Override
    public List<Plan> findPlansByApplicationId(String applicationId, Boolean status) {
        return this.baseMapper.findPlansByApplicationId(applicationId, status);
    }

    @Override
    @Transactional
    public void batchInsertApplicationPlan(List<ApplicationPlan> list) {
        this.saveBatch(list);
    }
}
