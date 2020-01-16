package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @RequiresPermissions("staffOutside:view")
    public StaffOutside getStaffOutsideByStaffId(String staffId) {
        return this.staffOutsideService.getStaffOutsideByStaffId(staffId);
    }

    @GetMapping("getStaffOutsideByIdNum")
    @RequiresPermissions("staffOutside:view")
    public StaffOutside getStaffOutsideByIdNum(String idNum) {
        return this.staffOutsideService.getStaffOutsideByIdNum(idNum);
    }

    @GetMapping("staffOutsideSimplify")
    @RequiresPermissions("staffOutside:view")
    public Map<String, Object> staffOutsideSimplify(QueryRequest request, StaffOutside staffOutside) {
        return getDataTable(this.staffOutsideService.findStaffOutsideSimplify(request, staffOutside));
    }

    @GetMapping
    @RequiresPermissions("staffOutside:view")
    public Map<String, Object> staffOutsideList(QueryRequest request, StaffOutside staffOutside, ServletRequest servletRequest) {
        return getDataTable(this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest));
    }

    @Log("新增编外人员信息")
    @PostMapping
    @RequiresPermissions("staffOutside:add")
    public void addStaffOutside(@Valid StaffOutside staffOutside) throws FebsException {
        try {
            this.staffOutsideService.createStaffOutside(staffOutside);
        } catch (Exception e) {
            message = "新增编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外人员信息")
    @PutMapping
    @RequiresPermissions("staffOutside:update")
    public void updateStaffOutside(@Valid StaffOutside staffOutside) throws FebsException {
        try {
            this.staffOutsideService.updateStaffOutside(staffOutside);
        } catch (Exception e) {
            message = "修改编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外人员信息")
    @DeleteMapping("/{staffOutsideIds}")
    @RequiresPermissions("staffOutside:delete")
    public void deleteStaffOutsides(@NotBlank(message = "{required}") @PathVariable String staffOutsideIds) throws FebsException {
        try {
            String[] ids = staffOutsideIds.split(StringPool.COMMA);
            this.staffOutsideService.deleteStaffOutside(ids);
        } catch (Exception e) {
            message = "删除编外人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外人员信息与对应合同信息")
    @DeleteMapping("together/{staffOutsideIds}")
    @RequiresPermissions("staffOutside:delete")
    public void deleteStaffOutsideAndContractOutside(@NotBlank(message = "{required}") @PathVariable String staffOutsideIds) throws FebsException {
        try {
            String[] ids = staffOutsideIds.split(StringPool.COMMA);
            this.staffOutsideService.deleteStaffOutsideAndContractOutside(ids);
        } catch (Exception e) {
            message = "删除编外人员信息与对应合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("staffOutside:export")
    public void export(QueryRequest request, StaffOutside staffOutside, HttpServletResponse response, ServletRequest servletRequest) throws FebsException {
        try {
            List<StaffOutside> staffOutsides = this.staffOutsideService.findStaffOutsideDetail(request, staffOutside, servletRequest).getRecords();
            ExcelKit.$Export(StaffOutside.class, response).downXlsx(staffOutsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
