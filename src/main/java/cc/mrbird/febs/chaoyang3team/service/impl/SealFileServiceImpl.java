package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.SealFile;
import cc.mrbird.febs.chaoyang3team.dao.SealFileMapper;
import cc.mrbird.febs.chaoyang3team.service.SealFileService;
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
@Service("sealFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SealFileServiceImpl extends ServiceImpl<SealFileMapper, SealFile> implements SealFileService {

    @Override
    @Transactional
    public void deleteSealFilesBySealId(String[] sealIds) {
        List<String> list = Arrays.asList(sealIds);
        baseMapper.delete(new LambdaQueryWrapper<SealFile>().in(SealFile::getSealId, list));
    }

    @Override
    @Transactional
    public void deleteSealFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<SealFile>().in(SealFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsBySealIds(String[] sealIds) {
        return baseMapper.findFileIdsBySealIds(StringUtils.join(sealIds, ","));
    }

    @Override
    public FebsResponse findFilesBySealId(String sealId) {
        List<File> fileList = this.baseMapper.findFilesBySealId(sealId);
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
    public void createSealFile(SealFile sealFile) {
        this.save(sealFile);
    }
}
