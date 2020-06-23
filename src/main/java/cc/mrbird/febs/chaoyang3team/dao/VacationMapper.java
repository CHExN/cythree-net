package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface VacationMapper extends BaseMapper<Vacation> {

    IPage<Vacation> findVacationDetail(Page<Vacation> page, @Param("vacation") Vacation vacation);

    List<String> getType();

    Long findInsideAnnualLeaveCount(@Param("vacation") Vacation vacation);

    IPage<Vacation> findInsideAnnualLeave(Page<Vacation> page, @Param("vacation") Vacation vacation);

    /**
     * 编内人员
     */
    Long findInsideVacationCount(@Param("vacation") Vacation vacation);
    IPage<Vacation> findInsideVacation(Page<Vacation> page, @Param("vacation") Vacation vacation);

    /**
     * 编外归属人员
     */
    Long findAttributionVacationCount(@Param("vacation") Vacation vacation);
    IPage<Vacation> findAttributionVacation(Page<Vacation> page, @Param("vacation") Vacation vacation);

    /**
     * 编外分队人员
     */
    Long findOutsideVacationCount(@Param("vacation") Vacation vacation);
    IPage<Vacation> findOutsideVacation(Page<Vacation> page, @Param("vacation") Vacation vacation);

    /**
     * 劳务派遣人员
     */
    Long findSendVacationCount(@Param("vacation") Vacation vacation);
    IPage<Vacation> findSendVacation(Page<Vacation> page, @Param("vacation") Vacation vacation);

}
