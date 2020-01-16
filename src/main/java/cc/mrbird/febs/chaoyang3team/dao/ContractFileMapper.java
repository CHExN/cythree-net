package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ContractFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface ContractFileMapper extends BaseMapper<ContractFile> {

    /**
     * 根据合同ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByContractIds(@Param("contractIds") String contractIds);

    /**
     * 根据合同ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByContractId(@Param("contractId") String contractId);

}
