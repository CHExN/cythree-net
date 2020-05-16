package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.PlanStableMapper;
import cc.mrbird.febs.chaoyang3team.domain.PlanStable;
import cc.mrbird.febs.chaoyang3team.service.PlanStableService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("planStableService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PlanStableServiceImpl extends ServiceImpl<PlanStableMapper, PlanStable> implements PlanStableService {

    @Override
    public IPage<PlanStable> findPlanStable(QueryRequest request, PlanStable planStable) {
        try {
            LambdaQueryWrapper<PlanStable> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(planStable.getName())) {
                queryWrapper.like(PlanStable::getName, planStable.getName());
            }
            if (StringUtils.isNotBlank(planStable.getType())) {
                queryWrapper.like(PlanStable::getType, planStable.getType());
            }
            if (StringUtils.isNotBlank(planStable.getUnit())) {
                queryWrapper.like(PlanStable::getUnit, planStable.getUnit());
            }
            if (StringUtils.isNotBlank(planStable.getRemark())) {
                queryWrapper.like(PlanStable::getRemark, planStable.getRemark());
            }

            Page<PlanStable> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("查询固定办公用品选项异常", e);
            return null;
        }
    }

    @Override
    public List<PlanStable> planStableList() {
        return this.baseMapper.planStableList();
    }

    @Override
    @Transactional
    public void createPlanStable(PlanStable planStable) {
        this.save(planStable);
    }

    @Override
    @Transactional
    public void updatePlanStable(PlanStable planStable) {
        this.baseMapper.updateById(planStable);
    }

    @Override
    @Transactional
    public void deletePlanStable(String[] planStableIds) {
        List<String> list = Arrays.asList(planStableIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
