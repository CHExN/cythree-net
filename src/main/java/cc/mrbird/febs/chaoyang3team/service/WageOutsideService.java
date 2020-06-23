package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WageOutside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WageOutsideService extends IService<WageOutside> {

    IPage<WageOutside> findWageOutsideDetail(QueryRequest request, WageOutside wage);

    List<WageOutside> getWageOutsideInfoList(WageOutside wage);

    void createWageOutside(WageOutside wage);

    void batchInsertWageOutside(List<WageOutside> wages);

    void updateWageOutside(WageOutside wage);

    void deleteWageOutside(String[] wageIds);
}
