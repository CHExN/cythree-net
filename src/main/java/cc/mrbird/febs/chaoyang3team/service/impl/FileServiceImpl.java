package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.FileMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("fileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Override
    public File findByFileId(Long fileId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<File>().eq(File::getFileId, fileId));
    }

    @Override
    @Transactional
    public FebsResponse uploadFile(MultipartFile file) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.baseMapper.insert(fileInfo);

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(7)
        );
        return new FebsResponse().data(result);
    }

    @Override
    @Transactional
    public void createFile(File file) {
        this.baseMapper.insert(file);
    }

    @Override
    @Transactional
    public void deleteFiles(String[] fileIds) {
        // 删除文件
        Arrays.stream(fileIds).forEach(fileId -> {
            String fileName = this.findByFileId(Long.valueOf(fileId)).getAddress();
            FileUploadUtil.fileDelete(fileName);
        });
        List<String> list = Arrays.asList(fileIds);
        // 删除文件信息
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public String findFileIdByFileName(String fileName) {
        return this.baseMapper.findFileIdByFileName(fileName);
    }
}
