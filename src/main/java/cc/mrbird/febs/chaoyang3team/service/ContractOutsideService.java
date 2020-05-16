package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ContractOutside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface ContractOutsideService extends IService<ContractOutside> {

    IPage<ContractOutside> findContractOutsideDetail(QueryRequest request, ContractOutside contractOutside, ServletRequest servletRequest);

    ContractOutside getContractOutside(String idNum);

    void createContractOutside(ContractOutside contractOutside);

    void updateContractOutside(ContractOutside contractOutside);

    void deleteContractOutside(String[] contractOutsideIds, Integer deleted);

    void deleteContractOutsideAndStaffOutside(String[] contractOutsideIds, Integer deleted);

    void restoreContractOutside(String contractOutsideIds);

    void togetherRestoreContractOutside(String contractOutsideIds);
}
