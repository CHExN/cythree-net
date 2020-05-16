package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcEvaluate;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface WcEvaluateService extends IService<WcEvaluate> {

    IPage<WcEvaluate> findWcEvaluateDetail(QueryRequest request, WcEvaluate wcEvaluate);

    void createWcEvaluate(WcEvaluate wcEvaluate);

    void deleteWcEvaluate(String[] ids);

}
