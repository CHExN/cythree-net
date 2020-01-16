package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.ContractFile;
import cc.mrbird.febs.chaoyang3team.dao.ContractFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.service.ContractFileService;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("contractFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ContractFileServiceImpl extends ServiceImpl<ContractFileMapper, ContractFile> implements ContractFileService {

    @Override
    @Transactional
    public void deleteContractFilesByContractId(String[] contractIds) {
        List<String> list = Arrays.asList(contractIds);
        baseMapper.delete(new LambdaQueryWrapper<ContractFile>().in(ContractFile::getContractId, list));
    }

    @Override
    @Transactional
    public void deleteContractFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<ContractFile>().in(ContractFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByContractIds(String[] contractIds) {
        return baseMapper.findFileIdsByContractIds(StringUtils.join(contractIds, ","));
    }

    @Override
    public FebsResponse findFilesByContractId(String contractId) {
        List<File> fileList = this.baseMapper.findFilesByContractId(contractId);
        List<Object> result = new ArrayList<>();
        fileList.forEach(file -> {
            result.add(ImmutableMap.of(
                    "uid", file.getFileId(),// 文件唯一标识，建议设置为负数，防止和内部产生的 id 冲突
                    "url", file.getAddress(),
                    "status", "done", // 状态有：uploading done error removed
                    "name", file.getAddress().substring(25) // 文件名
            ));
        });
        return new FebsResponse().data(result);
    }

    @Override
    @Transactional
    public void createContractFile(ContractFile contractFile) {
        this.save(contractFile);
    }
}
