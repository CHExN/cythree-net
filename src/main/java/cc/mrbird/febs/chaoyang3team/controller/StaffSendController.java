package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffSend;
import cc.mrbird.febs.chaoyang3team.service.SendFileService;
import cc.mrbird.febs.chaoyang3team.service.StaffSendService;
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
@RequestMapping("staffSend")
public class StaffSendController extends BaseController {
    private String message;

    @Autowired
    private StaffSendService staffSendService;
    @Autowired
    private SendFileService sendFileService;

    @Log("恢复回收站内劳务派遣人员信息")
    @PutMapping("restore")
    @RequiresPermissions("staffSend:restore")
    public void restoreStaffSend(@Valid String staffSendIds) throws FebsException {
        try {
            this.staffSendService.restoreStaffSend(staffSendIds);
        } catch (Exception e) {
            message = "恢复回收站内劳务派遣人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    // 没有合同
    /*@Log("恢复回收站内劳务派遣人员信息与对应合同信息")
    @PutMapping("togetherRestore")
    @RequiresPermissions("staffSend:restore")
    public void togetherRestoreStaffSend(@Valid String staffSendIds) throws FebsException {
        try {
            this.staffSendService.togetherRestoreStaffSend(staffSendIds);
        } catch (Exception e) {
            message = "恢复回收站内劳务派遣人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }*/

    @GetMapping("staffSendFiles")
    @RequiresPermissions("staffSend:view")
    public FebsResponse findFilesByStaffSendId(String staffSendId) {
        return this.sendFileService.findFilesBySendId(staffSendId);
    }

    @Log("上传劳务派遣人员相关图片")
    @PostMapping("uploadStaffSendPhoto")
    public FebsResponse uploadStaffSendPhoto(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.staffSendService.uploadStaffSendPhoto(file, id);
        } catch (Exception e) {
            message = "上传劳务派遣人员相关图片失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("getBankCardAttribution")
    public List<String> getBankCardAttribution() {
        return this.staffSendService.getBankCardAttribution();
    }

    @GetMapping("getCompany")
    public List<String> getCompany() {
        return this.staffSendService.getCompany();
    }

    @GetMapping("getStaffSendByStaffId")
    @RequiresPermissions("staffSend:view")
    public StaffSend getStaffSendByStaffId(String staffId) {
        return this.staffSendService.getStaffSendByStaffId(staffId);
    }

    @GetMapping("getStaffSendByIdNum")
    @RequiresPermissions("staffSend:view")
    public StaffSend getStaffSendByIdNum(String idNum) {
        return this.staffSendService.getStaffSendByIdNum(idNum);
    }

    @GetMapping
    @RequiresPermissions("staffSend:view")
    public Map<String, Object> staffSendList(QueryRequest request, StaffSend staffSend) {
        return getDataTable(this.staffSendService.findStaffSendDetail(request, staffSend));
    }

    @Log("新增劳务派遣人员信息")
    @PostMapping
    @RequiresPermissions("staffSend:add")
    public void addStaffSend(@Valid StaffSend staffSend) throws FebsException {
        try {
            this.staffSendService.createStaffSend(staffSend);
        } catch (Exception e) {
            message = "新增劳务派遣人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改劳务派遣人员信息")
    @PutMapping
    @RequiresPermissions("staffSend:update")
    public void updateStaffSend(@Valid StaffSend staffSend) throws FebsException {
        try {
            this.staffSendService.updateStaffSend(staffSend);
        } catch (Exception e) {
            message = "修改劳务派遣人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除劳务派遣人员信息")
    @DeleteMapping
    @RequiresPermissions("staffSend:delete")
    public void deleteStaffSends(@Valid String staffSendIds, Integer deleted) throws FebsException {
        try {
            String[] ids = staffSendIds.split(StringPool.COMMA);
            this.staffSendService.deleteStaffSend(ids, deleted);
        } catch (Exception e) {
            message = "删除劳务派遣人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    // 没有合同合同信息
    /*@Log("删除劳务派遣人员信息与对应合同信息")
    @DeleteMapping("together")
    @RequiresPermissions("staffSend:delete")
    public void deleteStaffSendAndContractSend(@Valid String staffSendIds, Integer deleted) throws FebsException {
        try {
            String[] ids = staffSendIds.split(StringPool.COMMA);
            this.staffSendService.deleteStaffSendAndContractSend(ids, deleted);
        } catch (Exception e) {
            message = "删除劳务派遣人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }*/

    @Log("删除劳务派遣人员相关文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    @RequiresPermissions("staffSend:addDeletePhoto")
    public void deleteStaffSendFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.staffSendService.deleteStaffSendsFile(ids);
        } catch (Exception e) {
            message = "删除劳务派遣人员相关文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("export")
    @RequiresPermissions("attributionSend:export")
    public void allExport(QueryRequest request, StaffSend staffSend, HttpServletResponse response) throws FebsException {
        try {
            List<StaffSend> staffSends = this.staffSendService.findStaffSendDetail(request, staffSend).getRecords();
            ExcelKit.$Export(StaffSend.class, response).downXlsx(staffSends, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
