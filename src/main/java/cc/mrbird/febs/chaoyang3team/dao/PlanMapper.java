package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface PlanMapper extends BaseMapper<Plan> {

    void updatePlanStatus(@Param("planIds") String planIds);

}
