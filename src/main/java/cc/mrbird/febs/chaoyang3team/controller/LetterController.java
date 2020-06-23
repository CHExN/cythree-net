package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Letter;
import cc.mrbird.febs.chaoyang3team.service.LetterFileService;
import cc.mrbird.febs.chaoyang3team.service.LetterService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("letter")
public class LetterController extends BaseController {

    private String message;

    @Autowired
    private LetterService letterService;
    @Autowired
    private LetterFileService letterFileService;

    @GetMapping("letterFiles")
    public FebsResponse findFilesByLetterId(String letterId) {
        return this.letterFileService.findFilesByLetterId(letterId);
    }

    @Log("上传申请介绍信附件失败")
    @PostMapping("uploadLetterFile")
    public FebsResponse uploadLetterFile(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.letterService.uploadLetterFile(file, id);
        } catch (Exception e) {
            message = "上传申请介绍信附件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping
    @RequiresPermissions("letter:view")
    public Map<String, Object> letterList(QueryRequest request, Letter letter, ServletRequest servletRequest) {
        return getDataTable(this.letterService.findLetterDetail(request, letter, servletRequest));
    }

    @Log("新增申请介绍信")
    @PostMapping
    @RequiresPermissions("letter:add")
    public void addLetter(@Valid Letter letter, ServletRequest request) throws FebsException {
        try {
            this.letterService.createLetter(letter, request);
        } catch (Exception e) {
            message = "新增申请介绍信失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改申请介绍信")
    @PutMapping
    @RequiresPermissions("letter:update")
    public void updateLetter(@Valid Letter letter) throws FebsException {
        try {
            this.letterService.updateLetter(letter);
        } catch (Exception e) {
            message = "修改申请介绍信失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除申请介绍信")
    @DeleteMapping("/{letterIds}")
    @RequiresPermissions("letter:delete")
    public void deleteLetters(@NotBlank(message = "{required}") @PathVariable String letterIds) throws FebsException {
        try {
            String[] ids = letterIds.split(StringPool.COMMA);
            this.letterService.deleteLetters(ids);
        } catch (Exception e) {
            message = "删除申请介绍信失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除申请介绍信文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    public void deleteWcFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.letterService.deleteLetterFile(ids);
        } catch (Exception e) {
            message = "删除申请介绍信文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
