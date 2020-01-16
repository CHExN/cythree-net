package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import cc.mrbird.febs.chaoyang3team.domain.VacationImport;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface VacationService extends IService<Vacation> {

    IPage<Vacation> findVacationDetail(QueryRequest request, Vacation vacation);

    void createVacation(Vacation vacation);

    void updateVacation(Vacation vacation);

    void deleteVacation(String[] vacationIds);

    /**
     * 获取休假类型
     * @return 现有的无重复休假类型列
     */
    List<String> getType();

    IPage<VacationImport> findInsideAnnualLeave(QueryRequest request, Vacation vacation);

    IPage<VacationImport> findInsOutVacation(QueryRequest request, Vacation vacation);

}
