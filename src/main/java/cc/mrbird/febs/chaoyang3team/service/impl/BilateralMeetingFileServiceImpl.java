package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.BilateralMeetingFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.BilateralMeetingFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.service.BilateralMeetingFileService;
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
@Service("bilateralMeetingFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BilateralMeetingFileServiceImpl extends ServiceImpl<BilateralMeetingFileMapper, BilateralMeetingFile> implements BilateralMeetingFileService {

    @Override
    @Transactional
    public void deleteBilateralMeetingFilesByBilateralMeetingId(String[] bilateralMeetingIds) {
        List<String> list = Arrays.asList(bilateralMeetingIds);
        baseMapper.delete(new LambdaQueryWrapper<BilateralMeetingFile>().in(BilateralMeetingFile::getBilateralMeetingId, list));
    }

    @Override
    @Transactional
    public void deleteBilateralMeetingFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<BilateralMeetingFile>().in(BilateralMeetingFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsByBilateralMeetingIds(String[] bilateralMeetingIds) {
        return baseMapper.findFileIdsByBilateralMeetingIds(StringUtils.join(bilateralMeetingIds, ","));
    }

    @Override
    public FebsResponse findFilesByBilateralMeetingId(String bilateralMeetingId) {
        List<File> fileList = this.baseMapper.findFilesByBilateralMeetingId(bilateralMeetingId);
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
    public void createBilateralMeetingFile(BilateralMeetingFile bilateralMeetingFile) {
        this.save(bilateralMeetingFile);
    }
}
