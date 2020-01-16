package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.LetterFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.LetterFile;
import cc.mrbird.febs.chaoyang3team.service.LetterFileService;
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
@Service("letterFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LetterFileServiceImpl extends ServiceImpl<LetterFileMapper, LetterFile> implements LetterFileService {

    @Override
    @Transactional
    public void deleteLetterFilesByLetterId(String[] letterIds) {
        List<String> list = Arrays.asList(letterIds);
        baseMapper.delete(new LambdaQueryWrapper<LetterFile>().in(LetterFile::getLetterId, list));
    }

    @Override
    @Transactional
    public void deleteLetterFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<LetterFile>().in(LetterFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByLetterIds(String[] letterIds) {
        return baseMapper.findFileIdsByLetterIds(StringUtils.join(letterIds, ","));
    }

    @Override
    public FebsResponse findFilesByLetterId(String letterId) {
        List<File> fileList = this.baseMapper.findFilesByLetterId(letterId);
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
    public void createLetterFile(LetterFile letterFile) {
        this.save(letterFile);
    }
}
