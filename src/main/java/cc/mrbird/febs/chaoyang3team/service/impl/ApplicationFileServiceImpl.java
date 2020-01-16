package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.ApplicationFile;
import cc.mrbird.febs.chaoyang3team.dao.ApplicationFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.service.ApplicationFileService;
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
@Service("applicationFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationFileServiceImpl extends ServiceImpl<ApplicationFileMapper, ApplicationFile> implements ApplicationFileService {

    @Override
    @Transactional
    public void deleteApplicationFilesByApplicationId(String[] applicationIds) {
        List<String> list = Arrays.asList(applicationIds);
        baseMapper.delete(new LambdaQueryWrapper<ApplicationFile>().in(ApplicationFile::getApplicationId, list));
    }

    @Override
    @Transactional
    public void deleteApplicationFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<ApplicationFile>().in(ApplicationFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByApplicationIds(String[] applicationIds) {
        return baseMapper.findFileIdsByApplicationIds(StringUtils.join(applicationIds, ","));
    }

    @Override
    public FebsResponse findFilesByApplicationId(String applicationId) {
        List<File> fileList = this.baseMapper.findFilesByApplicationId(applicationId);
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
    public void createApplicationFile(ApplicationFile applicationFile) {
        this.save(applicationFile);
    }
}
