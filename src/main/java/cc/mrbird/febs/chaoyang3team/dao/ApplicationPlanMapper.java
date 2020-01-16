package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ApplicationPlan;
import cc.mrbird.febs.chaoyang3team.domain.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface ApplicationPlanMapper extends BaseMapper<ApplicationPlan> {

//    /**
//     * 根据申请单ID删除该申请单与计划单的关系
//     *
//     * @param applicationId 申请单Id
//     * @return boolean
//     */
//    Boolean deleteByApplicationId(@Param("applicationId") Long applicationId);
//
//    /**
//     * 根据计划单ID删除该计划单与申请单的关系
//     *
//     * @param planId 计划单ID
//     * @return boolean
//     */
//    Boolean deleteByPlanId(@Param("planId") Long planId);

    /**
     * 根据申请单ID查找对应计划单ID
     *
     * @return 计划单ID
     */
    List<String> findPlanIdsByApplicationIds(@Param("applicationIds") String applicationIds);

    /**
     * 根据申请单ID查找对应计划单详情
     *
     * @return 计划单详情
     */
    List<Plan> findPlansByApplicationId(@Param("applicationId") String applicationId);

}
