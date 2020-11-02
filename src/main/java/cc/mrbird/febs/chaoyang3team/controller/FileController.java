package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("file")
public class FileController extends BaseController {

    private String message;

    @Autowired
    private FileService fileService;

    @Log("上传文件信息")
    @PostMapping("uploadFile")
    public FebsResponse uploadFile(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
            return this.fileService.uploadFile(file);
        } catch (Exception e) {
            message = "上传文件信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除文件信息")
    @DeleteMapping("/{fileIds}")
    public void deleteFiles(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.fileService.deleteFiles(ids);
        } catch (Exception e) {
            message = "删除文件信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
