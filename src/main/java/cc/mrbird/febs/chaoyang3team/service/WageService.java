package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Wage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WageService extends IService<Wage> {

    IPage<Wage> findWageDetail(QueryRequest request, Wage wage);

    List<Wage> getWageInfoList(Wage wage);

    void createWage(Wage wage);

    void batchInsertWage(List<Wage> wages);

    void updateWage(Wage wage);

    void deleteWage(String[] wageIds);

    List<Wage> getWageReport(Wage wage);

}
