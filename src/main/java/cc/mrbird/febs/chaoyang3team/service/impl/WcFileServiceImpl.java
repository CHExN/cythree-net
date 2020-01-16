package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.WcFile;
import cc.mrbird.febs.chaoyang3team.service.WcFileService;
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
@Service("wcFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcFileServiceImpl extends ServiceImpl<WcFileMapper, WcFile> implements WcFileService {

    @Override
    @Transactional
    public void deleteWcFilesByWcId(String[] wcIds) {
        List<String> list = Arrays.asList(wcIds);
        baseMapper.delete(new LambdaQueryWrapper<WcFile>().in(WcFile::getWcId, list));
    }

    @Override
    @Transactional
    public void deleteWcFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<WcFile>().in(WcFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByWcIds(String[] wcIds) {
        return baseMapper.findFileIdsByWcIds(StringUtils.join(wcIds, ","));
    }

    @Override
    public FebsResponse findFilesByWcId(String wcId) {
        List<File> fileList = this.baseMapper.findFilesByWcId(wcId);
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
    public void createWcFile(WcFile wcFile) {
        this.save(wcFile);
    }
}
