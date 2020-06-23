package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcStatus;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcStatusService extends IService<WcStatus> {

    IPage<WcStatus> findWcStatusDetail(QueryRequest request, WcStatus wcStatus);

    List<WcStatus> getWcStatusList(WcStatus wcStatus);

    void createWcStatus(WcStatus wcStatus);

    void deleteWcStatus(String[] ids);

    void updateWcStatus(WcStatus wcStatus);

    /**
     * 获取公厕状态
     * @return 现有的无重复公厕状态列
     */
    List<String> getStatus();
}
