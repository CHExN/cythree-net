package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Seal;
import cc.mrbird.febs.chaoyang3team.service.SealFileService;
import cc.mrbird.febs.chaoyang3team.service.SealService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("seal")
public class SealController extends BaseController {

    private String message;

    @Autowired
    private SealService sealService;
    @Autowired
    private SealFileService sealFileService;

    @GetMapping("sealFiles")
    public FebsResponse findFilesBySealId(String sealId) {
        return this.sealFileService.findFilesBySealId(sealId);
    }

    @Log("上传申请印章附件")
    @PostMapping("uploadSealFile")
    public FebsResponse uploadSealFile(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.sealService.uploadSealFile(file, id);
        } catch (Exception e) {
            message = "上传申请印章附件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping
    @RequiresPermissions("seal:view")
    public Map<String, Object> sealList(QueryRequest request, Seal seal, ServletRequest servletRequest) {
        return getDataTable(this.sealService.findSealDetail(request, seal, servletRequest));
    }

    @Log("新增申请印章")
    @PostMapping
    @RequiresPermissions("seal:add")
    public void addSeal(@Valid Seal seal, ServletRequest request) throws FebsException {
        try {
            this.sealService.createSeal(seal, request);
        } catch (Exception e) {
            message = "新增申请印章失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改申请印章")
    @PutMapping
    @RequiresPermissions("seal:update")
    public void updateSeal(@Valid Seal seal) throws FebsException {
        try {
            this.sealService.updateSeal(seal);
        } catch (Exception e) {
            message = "修改申请印章失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除申请印章")
    @DeleteMapping("/{sealIds}")
    @RequiresPermissions("seal:delete")
    public void deleteSeals(@NotBlank(message = "{required}") @PathVariable String sealIds) throws FebsException {
        try {
            String[] ids = sealIds.split(StringPool.COMMA);
            this.sealService.deleteSeals(ids);
        } catch (Exception e) {
            message = "删除申请印章失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除申请印章文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    public void deleteWcFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.sealService.deleteSealsFile(ids);
        } catch (Exception e) {
            message = "删除申请印章文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("seal:export")
    public void export(QueryRequest request, Seal seal, ServletRequest servletRequest,
                       HttpServletResponse response) throws FebsException {
        try {
            List<Seal> seals = this.sealService.findSealDetail(request, seal, servletRequest).getRecords();
            ExcelKit.$Export(Seal.class, response).downXlsx(seals, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
