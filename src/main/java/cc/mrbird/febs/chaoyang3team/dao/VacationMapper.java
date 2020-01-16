package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import cc.mrbird.febs.chaoyang3team.domain.VacationImport;
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

    IPage<VacationImport> findInsideAnnualLeave(Page<VacationImport> page, @Param("vacation") Vacation vacation);

    Long findInsVacationCount(@Param("vacation") Vacation vacation);

    IPage<VacationImport> findInsVacation(Page<VacationImport> page, @Param("vacation") Vacation vacation);

    Long findOutVacationCount(@Param("vacation") Vacation vacation);

    IPage<VacationImport> findOutVacation(Page<VacationImport> page, @Param("vacation") Vacation vacation);

}
