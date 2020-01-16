package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.ContractInside;
import cc.mrbird.febs.chaoyang3team.service.ContractInsideService;
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
@RequestMapping("contractInside")
public class ContractInsideController extends BaseController {

    private String message;

    @Autowired
    private ContractInsideService contractInsideService;

    @GetMapping("getContractInside")
    @RequiresPermissions("contractInside:view")
    public ContractInside getContractInside(String staffInsideId) {
        return this.contractInsideService.getContractInside(staffInsideId);
    }

    @GetMapping
    @RequiresPermissions("contractInside:view")
    public Map<String, Object> ContractInsideList(QueryRequest request, ContractInside contractInside) {
        return getDataTable(this.contractInsideService.findContractInsideDetail(request, contractInside));
    }

    @Log("新增编内合同信息")
    @PostMapping
    @RequiresPermissions("contractInside:add")
    public void addContractInside(@Valid ContractInside contractInside) throws FebsException {
        try {
            this.contractInsideService.createContractInside(contractInside);
        } catch (Exception e) {
            message = "新增编内合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编内合同信息")
    @PutMapping
    @RequiresPermissions("contractInside:update")
    public void updateContractInside(@Valid ContractInside contractInside) throws FebsException {
        try {
            this.contractInsideService.updateContractInside(contractInside);
        } catch (Exception e) {
            message = "修改编内合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编内合同信息")
    @DeleteMapping("/{contractInsideIds}")
    @RequiresPermissions("contractInside:delete")
    public void deleteContractInsides(@NotBlank(message = "{required}") @PathVariable String contractInsideIds) throws FebsException {
        try {
            String[] ids = contractInsideIds.split(StringPool.COMMA);
            this.contractInsideService.deleteContractInside(ids);
        } catch (Exception e) {
            message = "删除编内合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编内合同信息与对应人员信息")
    @DeleteMapping("/together/{contractInsideIds}")
    @RequiresPermissions("contractInside:delete")
    public void deleteContractInsideAndStaffInside(@NotBlank(message = "{required}") @PathVariable String contractInsideIds) throws FebsException {
        try {
            String[] ids = contractInsideIds.split(StringPool.COMMA);
            this.contractInsideService.deleteContractInsideAndStaffInside(ids);
        } catch (Exception e) {
            message = "删除编内合同信息与对应人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("contractInside:export")
    public void export(QueryRequest request, ContractInside contractInside, HttpServletResponse response) throws FebsException {
        try {
            List<ContractInside> contractInsides = this.contractInsideService.findContractInsideDetail(request, contractInside).getRecords();
            ExcelKit.$Export(ContractInside.class, response).downXlsx(contractInsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
