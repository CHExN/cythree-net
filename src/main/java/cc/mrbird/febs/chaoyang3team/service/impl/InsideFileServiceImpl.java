package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.InsideFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.InsideFile;
import cc.mrbird.febs.chaoyang3team.service.InsideFileService;
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
@Service("insideFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InsideFileServiceImpl extends ServiceImpl<InsideFileMapper, InsideFile> implements InsideFileService {

    @Override
    @Transactional
    public void deleteInsideFilesByInsideId(String[] insideIds) {
        List<String> list = Arrays.asList(insideIds);
        baseMapper.delete(new LambdaQueryWrapper<InsideFile>().in(InsideFile::getInsideId, list));
    }

    @Override
    @Transactional
    public void deleteInsideFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<InsideFile>().in(InsideFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByInsideIds(String[] insideIds) {
        return baseMapper.findFileIdsByInsideIds(StringUtils.join(insideIds, ","));
    }

    @Override
    public FebsResponse findFilesByInsideId(String insideId) {
        List<File> fileList = this.baseMapper.findFilesByInsideId(insideId);
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
    public void createInsideFile(InsideFile insideFile) {
        this.save(insideFile);
    }

}
