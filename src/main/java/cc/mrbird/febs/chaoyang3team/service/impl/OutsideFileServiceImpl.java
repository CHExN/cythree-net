package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.OutsideFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.dao.OutsideFileMapper;
import cc.mrbird.febs.chaoyang3team.service.OutsideFileService;
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
@Service("outsideFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OutsideFileServiceImpl extends ServiceImpl<OutsideFileMapper, OutsideFile> implements OutsideFileService {

    @Override
    @Transactional
    public void deleteOutsideFilesByOutsideId(String[] outsideIds) {
        List<String> list = Arrays.asList(outsideIds);
        baseMapper.delete(new LambdaQueryWrapper<OutsideFile>().in(OutsideFile::getOutsideId, list));
    }

    @Override
    @Transactional
    public void deleteOutsideFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<OutsideFile>().in(OutsideFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByOutsideIds(String[] outsideIds) {
        return baseMapper.findFileIdsByOutsideIds(StringUtils.join(outsideIds, ","));
    }

    @Override
    public FebsResponse findFilesByOutsideId(String outsideId) {
        List<File> fileList = this.baseMapper.findFilesByOutsideId(outsideId);
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
    public void createOutsideFile(OutsideFile outsideFile) {
        this.save(outsideFile);
    }

}
