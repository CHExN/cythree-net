package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.SendFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.SendFile;
import cc.mrbird.febs.chaoyang3team.service.SendFileService;
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
@Service("sendFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SendFileServiceImpl extends ServiceImpl<SendFileMapper, SendFile> implements SendFileService {

    @Override
    @Transactional
    public void deleteSendFilesBySendId(String[] sendIds) {
        List<String> list = Arrays.asList(sendIds);
        baseMapper.delete(new LambdaQueryWrapper<SendFile>().in(SendFile::getSendId, list));
    }

    @Override
    @Transactional
    public void deleteSendFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<SendFile>().in(SendFile::getFileId, list));
    }

    @Override
    public List<String> findFileIdsBySendIds(String[] sendIds) {
        return baseMapper.findFileIdsBySendIds(StringUtils.join(sendIds, ","));
    }

    @Override
    public FebsResponse findFilesBySendId(String sendId) {
        List<File> fileList = this.baseMapper.findFilesBySendId(sendId);
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
    public void createSendFile(SendFile sendFile) {
        this.save(sendFile);
    }
}
