package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.ContractOutside;
import cc.mrbird.febs.chaoyang3team.service.ContractOutsideService;
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
@RequestMapping("contractOutside")
public class ContractOutsideController extends BaseController {

    private String message;

    @Autowired
    private ContractOutsideService contractOutsideService;

    @GetMapping("getContractOutside")
    @RequiresPermissions("contractOutside:view")
    public ContractOutside getContractOutside(String idNum) {
        return this.contractOutsideService.getContractOutside(idNum);
    }

    @GetMapping
    @RequiresPermissions("contractOutside:view")
    public Map<String, Object> ContractOutsideList(QueryRequest request, ContractOutside contractOutside) {
        return getDataTable(this.contractOutsideService.findContractOutsideDetail(request, contractOutside));
    }

    @Log("新增编外合同信息")
    @PostMapping
    @RequiresPermissions("contractOutside:add")
    public void addContractOutside(@Valid ContractOutside contractOutside) throws FebsException {
        try {
            this.contractOutsideService.createContractOutside(contractOutside);
        } catch (Exception e) {
            message = "新增编外合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外合同信息")
    @PutMapping
    @RequiresPermissions("contractOutside:update")
    public void updateContractOutside(@Valid ContractOutside contractOutside) throws FebsException {
        try {
            this.contractOutsideService.updateContractOutside(contractOutside);
        } catch (Exception e) {
            message = "修改编外合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外合同信息")
    @DeleteMapping("/{contractOutsideIds}")
    @RequiresPermissions("contractOutside:delete")
    public void deleteContractOutsides(@NotBlank(message = "{required}") @PathVariable String contractOutsideIds) throws FebsException {
        try {
            String[] ids = contractOutsideIds.split(StringPool.COMMA);
            this.contractOutsideService.deleteContractOutside(ids);
        } catch (Exception e) {
            message = "删除编外合同信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外合同信息与对应人员信息")
    @DeleteMapping("/together/{contractOutsideIds}")
    @RequiresPermissions("contractOutside:delete")
    public void deleteContractOutsideAndStaffOutside(@NotBlank(message = "{required}") @PathVariable String contractOutsideIds) throws FebsException {
        try {
            String[] ids = contractOutsideIds.split(StringPool.COMMA);
            this.contractOutsideService.deleteContractOutsideAndStaffOutside(ids);
        } catch (Exception e) {
            message = "删除编外合同信息与对应人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("contractOutside:export")
    public void export(QueryRequest request, ContractOutside contractOutside, HttpServletResponse response) throws FebsException {
        try {
            List<ContractOutside> contractOutsides = this.contractOutsideService.findContractOutsideDetail(request, contractOutside).getRecords();
            ExcelKit.$Export(ContractOutside.class, response).downXlsx(contractOutsides, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
