package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ContractFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface ContractFileService extends IService<ContractFile> {

    void deleteContractFilesByContractId(String[] contractIds);

    void deleteContractFilesByFileId(String[] fileIds);

    List<String> findFileIdsByContractIds(String[] contractIds);

    FebsResponse findFilesByContractId(String contractId);

    void createContractFile(ContractFile contractFile);
}
