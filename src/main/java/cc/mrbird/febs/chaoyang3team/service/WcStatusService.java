package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcStatus;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface WcStatusService extends IService<WcStatus> {

    IPage<WcStatus> findWcStatusDetail(QueryRequest request, WcStatus wcStatus);

    void createWcStatus(WcStatus wcStatus);

    void deleteWcStatus(String[] ids);

    void updateWcStatus(WcStatus wcStatus);
}
