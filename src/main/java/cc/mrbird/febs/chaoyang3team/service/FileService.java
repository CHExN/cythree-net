package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author CHExN
 */
public interface FileService extends IService<File> {

    /**
     * 通过文件id查找文件信息
     *
     * @param fileId fileId
     * @return user
     */
    File findByFileId(Long fileId);

    FebsResponse uploadFile(MultipartFile file) throws Exception;

    void createFile(File file);

    void deleteFiles(String[] fileIds);

    String findFileIdByFileName(String fileName);

}
