package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.service.InsideFileService;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
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
@RequestMapping("staffInside")
public class StaffInsideController extends BaseController {

    private String message;

    @Autowired
    private StaffInsideService staffInsideService;
    @Autowired
    private InsideFileService insideFileService;

    @Log("恢复回收站内编内人员信息")
    @PutMapping("restore")
    @RequiresPermissions("staffInside:restore")
    public void restoreStaffInside(@Valid String staffInsideIds) throws FebsException {
        try {
            this.staffInsideService.restoreStaffInside(staffInsideIds);
        } catch (Exception e) {
            message = "恢复回收站内编内人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("恢复回收站内编内人员信息与对应合同信息")
    @PutMapping("togetherRestore")
    @RequiresPermissions("staffInside:restore")
    public void togetherRestoreStaffInside(@Valid String staffInsideIds) throws FebsException {
        try {
            this.staffInsideService.togetherRestoreStaffInside(staffInsideIds);
        } catch (Exception e) {
            message = "恢复回收站内编内人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("staffInsideFiles")
    @RequiresPermissions("staffInside:view")
    public FebsResponse findFilesByStaffInsideId(String staffInsideId) {
        return this.insideFileService.findFilesByInsideId(staffInsideId);
    }

    @Log("上传编内人员相关图片")
    @PostMapping("uploadStaffInsidePhoto")
    @RequiresPermissions("staffInside:addDeletePhoto")
    public FebsResponse uploadStaffInsidePhoto(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.staffInsideService.uploadStaffInsidePhoto(file, id);
        } catch (Exception e) {
            message = "上传编内人员相关图片失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("getIncreaseOrDecreaseStaffInside")
    public List<StaffInside> getIncreaseOrDecreaseStaffInside(StaffInside staffInside) {
        return this.staffInsideService.getIncreaseOrDecreaseStaffInside(staffInside);
    }

    @GetMapping("getTechnicalType")
    public List<String> getTechnicalType() {
        return this.staffInsideService.getTechnicalType();
    }

    @GetMapping("getEntryStatus")
    public List<String> getEntryStatus() {
        return this.staffInsideService.getEntryStatus();
    }

    @GetMapping("getPostLevel")
    public List<String> getPostLevel() {
        return this.staffInsideService.getPostLevel();
    }

    @GetMapping("getStaffInsideByIdNum")
    @RequiresPermissions("staffInside:view")
    public StaffInside getStaffInsideByIdNum(String idNum) {
        return this.staffInsideService.getStaffInsideByIdNum(idNum);
    }

    @GetMapping("getStaffInsideByStaffId")
    @RequiresPermissions("staffInside:view")
    public StaffInside getStaffInside(String staffId) {
        return this.staffInsideService.getStaffInsideByStaffId(staffId);
    }

    @GetMapping("staffInsideSimplify")
    @RequiresPermissions("staffInside:view")
    public Map<String, Object> staffInsideSimplify(QueryRequest request, StaffInside staffInside) {
        return getDataTable(this.staffInsideService.findStaffInsideSimplify(request, staffInside));
    }

    @GetMapping
    @RequiresPermissions("staffInside:view")
    public Map<String, Object> staffInsideList(QueryRequest request, StaffInside staffInside) {
        return getDataTable(this.staffInsideService.findStaffInsideDetail(request, staffInside));
    }

    @Log("新增编内人员信息")
    @PostMapping
    @RequiresPermissions("staffInside:add")
    public void addStaffInside(@Valid StaffInside staffInside) throws FebsException {
        try {
            this.staffInsideService.createStaffInside(staffInside);
        } catch (Exception e) {
            message = "新增编内人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编内人员信息")
    @PutMapping
    @RequiresPermissions("staffInside:update")
    public void updateStaffInside(@Valid StaffInside staffInside) throws FebsException {
        try {
            this.staffInsideService.updateStaffInside(staffInside);
        } catch (Exception e) {
            message = "修改编内人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    // 不提供单独删除人员信息，留着合同信息
    /*@Log("删除编内人员信息")
    @DeleteMapping
    @RequiresPermissions("staffInside:delete")
    public void deleteStaffInsides(@Valid String staffInsideIds, Integer deleted) throws FebsException {
        try {
            String[] ids = staffInsideIds.split(StringPool.COMMA);
            this.staffInsideService.deleteStaffInside(ids, deleted);
        } catch (Exception e) {
            message = "删除编内人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }*/

    @Log("删除编内人员信息与对应合同信息")
    @DeleteMapping("together")
    @RequiresPermissions("staffInside:delete")
    public void deleteStaffInsideAndContractInside(@Valid String staffInsideIds, Integer deleted) throws FebsException {
        try {
            String[] ids = staffInsideIds.split(StringPool.COMMA);
            this.staffInsideService.deleteStaffInsideAndContractInside(ids, deleted);
        } catch (Exception e) {
            message = "删除编内人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编内人员相关文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    @RequiresPermissions("staffInside:addDeletePhoto")
    public void deleteStaffInsideFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.staffInsideService.deleteStaffInsidesFile(ids);
        } catch (Exception e) {
            message = "删除编内人员相关文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("staffInside:export")
    public void export(QueryRequest request, StaffInside staffInside, HttpServletResponse response) throws FebsException {
        try {
            List<StaffInside> staffInsides = this.staffInsideService.findStaffInsideDetail(request, staffInside).getRecords();
            ExcelKit.$Export(StaffInside.class, response).downXlsx(staffInsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
