package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Contract;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface ContractService extends IService<Contract> {

    IPage<Contract> findContractDetail(QueryRequest request, Contract contract, ServletRequest servletRequest);

    void createContract(Contract contract, ServletRequest request);

    void updateContract(Contract contract);

    void deleteContracts(String[] contractIds);

    void deleteContractsFile(String[] fileIds);

    FebsResponse uploadContractFile(MultipartFile file, String contractId) throws Exception;

}
