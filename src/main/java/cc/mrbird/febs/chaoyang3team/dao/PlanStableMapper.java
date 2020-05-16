package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.PlanStable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author CHExN
 */
public interface PlanStableMapper extends BaseMapper<PlanStable> {

    List<PlanStable> planStableList();
}
