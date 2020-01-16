package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.FixedAssetsAcceptance;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface FixedAssetsAcceptanceService extends IService<FixedAssetsAcceptance> {

    IPage<FixedAssetsAcceptance> findFixedAssetsAcceptance(QueryRequest request, FixedAssetsAcceptance fixedAssetsAcceptance);

    void createFixedAssetsAcceptance(FixedAssetsAcceptance fixedAssetsAcceptance);

    void updateFixedAssetsAcceptance(FixedAssetsAcceptance fixedAssetsAcceptance);

    void deleteFixedAssetsAcceptance(String[] fixedAssetsAcceptanceIds);

}
