package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.OutsideFileService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
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
@RequestMapping("staffOutside")
public class StaffOutsideController extends BaseController {

    private String message;

    @Autowired
    private StaffOutsideService staffOutsideService;
    @Autowired
    private OutsideFileService outsideFileService;

    @Log("恢复回收站内编外人员信息")
    @PutMapping("restore")
    @RequiresPermissions(value={"staffOutside:restore", "attributionOutside:restore"}, logical= Logical.OR)
    public void restoreStaffOutside(@Valid String staffOutsideIds) throws FebsException {
        try {
            this.staffOutsideService.restoreStaffOutside(staffOutsideIds);
        } catch (Exception e) {
            message = "恢复回收站内编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("恢复回收站内编外人员信息与对应合同信息")
    @PutMapping("togetherRestore")
    @RequiresPermissions(value={"staffOutside:restore", "attributionOutside:restore"}, logical= Logical.OR)
    public void togetherRestoreStaffOutside(@Valid String staffOutsideIds) throws FebsException {
        try {
            this.staffOutsideService.togetherRestoreStaffOutside(staffOutsideIds);
        } catch (Exception e) {
            message = "恢复回收站内编外人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("staffOutsideFiles")
    @RequiresPermissions(value={"staffOutside:view", "attributionOutside:view"}, logical= Logical.OR)
    public FebsResponse findFilesByStaffOutsideId(String staffOutsideId) {
        return this.outsideFileService.findFilesByOutsideId(staffOutsideId);
    }

    @Log("上传编外人员相关图片")
    @PostMapping("uploadStaffOutsidePhoto")
    public FebsResponse uploadStaffOutsidePhoto(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.staffOutsideService.uploadStaffOutsidePhoto(file, id);
        } catch (Exception e) {
            message = "上传编外人员相关图片失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("getIncreaseOrDecreaseStaffOutside")
    public List<StaffOutside> getIncreaseOrDecreaseStaffOutside(StaffOutside staffOutside) {
        return this.staffOutsideService.getIncreaseOrDecreaseStaffOutside(staffOutside);
    }

    @Log("调整编外人员位置")
    @PutMapping("updateSort")
    @RequiresPermissions("staffOutside:update")
    public Map<String, Object> updateSortStaffOutside(@Valid StaffOutside staffOutside, String isUp) throws FebsException {
        try {
            return this.staffOutsideService.updateSortStaffOutside(staffOutside, isUp);
        } catch (Exception e) {
            message = "调整编外人员位置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("getTechnicalType")
    public List<String> getTechnicalType() {
        return this.staffOutsideService.getTechnicalType();
    }

    @GetMapping("getPost")
    public List<String> getPost() {
        return this.staffOutsideService.getPost();
    }

    @GetMapping("getTeam")
    public List<String> getTeam() {
        return this.staffOutsideService.getTeam();
    }

    @GetMapping("getStaffOutsideByStaffId")
    @RequiresPermissions(value={"staffOutside:view", "attributionOutside:view"}, logical= Logical.OR)
    public StaffOutside getStaffOutsideByStaffId(String staffId) {
        return this.staffOutsideService.getStaffOutsideByStaffId(staffId);
    }

    @GetMapping("getStaffOutsideByIdNum")
    @RequiresPermissions(value={"staffOutside:view", "attributionOutside:view"}, logical= Logical.OR)
    public StaffOutside getStaffOutsideByIdNum(String idNum) {
        return this.staffOutsideService.getStaffOutsideByIdNum(idNum);
    }

    @GetMapping("staffOutsideSimplify")
    @RequiresPermissions(value={"staffOutside:view", "attributionOutside:view"}, logical= Logical.OR)
    public Map<String, Object> staffOutsideSimplify(QueryRequest request, StaffOutside staffOutside) {
        return getDataTable(this.staffOutsideService.findStaffOutsideSimplify(request, staffOutside));
    }

    @GetMapping
    @RequiresPermissions("staffOutside:view")
    public Map<String, Object> staffOutsideList(QueryRequest request, StaffOutside staffOutside, ServletRequest servletRequest) {
        staffOutside.setType("1,2,3");
        return getDataTable(this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest));
    }

    @GetMapping("attributionOutside")
    @RequiresPermissions("attributionOutside:view")
    public Map<String, Object> attributionOutsideList(QueryRequest request, StaffOutside staffOutside, ServletRequest servletRequest) {
        staffOutside.setType("0");
        System.out.println("");
        return getDataTable(this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest));
    }

    @Log("新增编外人员信息")
    @PostMapping
    @RequiresPermissions(value={"staffOutside:add", "attributionOutside:add"}, logical= Logical.OR)
    public void addStaffOutside(@Valid StaffOutside staffOutside, ServletRequest servletRequest) throws FebsException {
        try {
            this.staffOutsideService.createStaffOutside(staffOutside, servletRequest);
        } catch (Exception e) {
            message = "新增编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外人员信息")
    @PutMapping
    @RequiresPermissions(value={"staffOutside:update", "attributionOutside:update"}, logical= Logical.OR)
    public void updateStaffOutside(@Valid StaffOutside staffOutside) throws FebsException {
        try {
            this.staffOutsideService.updateStaffOutside(staffOutside);
        } catch (Exception e) {
            message = "修改编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    // 不提供单独删除人员信息，留着合同信息
    /*@Log("删除编外人员信息")
    @DeleteMapping
    @RequiresPermissions("staffOutside:delete")
    public void deleteStaffOutsides(@Valid String staffOutsideIds, Integer deleted) throws FebsException {
        try {
            String[] ids = staffOutsideIds.split(StringPool.COMMA);
            this.staffOutsideService.deleteStaffOutside(ids, deleted);
        } catch (Exception e) {
            message = "删除编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }*/

    @Log("删除编外人员信息与对应合同信息")
    @DeleteMapping("together")
    @RequiresPermissions(value={"staffOutside:delete", "attributionOutside:delete"}, logical= Logical.OR)
    public void deleteStaffOutsideAndContractOutside(@Valid String staffOutsideIds, Integer deleted) throws FebsException {
        try {
            String[] ids = staffOutsideIds.split(StringPool.COMMA);
            this.staffOutsideService.deleteStaffOutsideAndContractOutside(ids, deleted);
        } catch (Exception e) {
            message = "删除编外人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外人员相关文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    @RequiresPermissions(value={"staffOutside:addDeletePhoto", "attributionOutside:addDeletePhoto"}, logical= Logical.OR)
    public void deleteStaffOutsideFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.staffOutsideService.deleteStaffOutsidesFile(ids);
        } catch (Exception e) {
            message = "删除编外人员相关文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("allExport")
    @RequiresPermissions("attributionOutside:allExport")
    public void allExport(QueryRequest request, StaffOutside staffOutside, HttpServletResponse response, ServletRequest servletRequest) throws FebsException {
        try {
            List<StaffOutside> staffOutsides = this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest).getRecords();
            ExcelKit.$Export(StaffOutside.class, response).downXlsx(staffOutsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("attributionOutsideExport")
    @RequiresPermissions("attributionOutside:export")
    public void attributionOutsideExport(QueryRequest request, StaffOutside staffOutside, HttpServletResponse response, ServletRequest servletRequest) throws FebsException {
        try {
            staffOutside.setType("0");
            List<StaffOutside> staffOutsides = this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest).getRecords();
            ExcelKit.$Export(StaffOutside.class, response).downXlsx(staffOutsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("staffOutsideExport")
    @RequiresPermissions("staffOutside:export")
    public void staffOutsideExport(QueryRequest request, StaffOutside staffOutside, HttpServletResponse response, ServletRequest servletRequest) throws FebsException {
        try {
            staffOutside.setType("1,2,3");
            List<StaffOutside> staffOutsides = this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest).getRecords();
            ExcelKit.$Export(StaffOutside.class, response).downXlsx(staffOutsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
