package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.FireExtinguisher;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface FireExtinguisherService extends IService<FireExtinguisher> {

    IPage<FireExtinguisher> findFireExtinguisherDetail(QueryRequest request, FireExtinguisher fireExtinguisher);

    void createFireExtinguisher(FireExtinguisher fireExtinguisher);

    void updateFireExtinguisher(FireExtinguisher fireExtinguisher);

    void deleteFireExtinguisher(String[] fireExtinguisherIds);

    void batchInsertFireExtinguisher(List<FireExtinguisher> fireExtinguisherList);
}
