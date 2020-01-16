package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ContractInside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface ContractInsideService extends IService<ContractInside> {

    IPage<ContractInside> findContractInsideDetail(QueryRequest request, ContractInside contractInside);

    ContractInside getContractInside(String staffInsideId);

    void createContractInside(ContractInside contractInside);

    void updateContractInside(ContractInside contractInside);

    void deleteContractInside(String[] contractInsideIds);

    void deleteContractInsideAndStaffInside(String[] staffInsideIds);
}
