package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Contract;
import cc.mrbird.febs.chaoyang3team.service.ContractFileService;
import cc.mrbird.febs.chaoyang3team.service.ContractService;
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
@RequestMapping("contract")
public class ContractController extends BaseController {

    private String message;

    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractFileService contractFileService;

    @GetMapping("contractFiles")
    public FebsResponse findFilesByContractId(String contractId) {
        return this.contractFileService.findFilesByContractId(contractId);
    }

    @Log("上传合同联审单附件")
    @PostMapping("uploadContractFile")
    public FebsResponse uploadContractFile(@RequestParam("file") MultipartFile file, String id) throws FebsException {
        try {
            return this.contractService.uploadContractFile(file, id);
        } catch (Exception e) {
            message = "上传合同联审单附件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping
    @RequiresPermissions("contract:view")
    public Map<String, Object> contractList(QueryRequest request, Contract contract, ServletRequest servletRequest) {
        return getDataTable(this.contractService.findContractDetail(request, contract, servletRequest));
    }

    @Log("新增合同联审单")
    @PostMapping
    @RequiresPermissions("contract:add")
    public void addContract(@Valid Contract contract, ServletRequest request) throws FebsException {
        try {
            this.contractService.createContract(contract, request);
        } catch (Exception e) {
            message = "新增合同联审单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除合同联审单")
    @DeleteMapping("/{contractIds}")
    @RequiresPermissions("contract:delete")
    public void deleteContract(@NotBlank(message = "{required}") @PathVariable String contractIds) throws FebsException {
        try {
            String[] ids = contractIds.split(StringPool.COMMA);
            this.contractService.deleteContracts(ids);
        } catch (Exception e) {
            message = "删除合同联审单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除合同联审单文件")
    @DeleteMapping("/deleteFile/{fileIds}")
    public void deleteWcFile(@NotBlank(message = "{required}") @PathVariable String fileIds) throws FebsException {
        try {
            String[] ids = fileIds.split(StringPool.COMMA);
            this.contractService.deleteContractsFile(ids);
        } catch (Exception e) {
            message = "删除合同联审单文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改合同联审单")
    @PutMapping
    @RequiresPermissions("contract:update")
    public void updateContract(@Valid Contract contract) throws FebsException {
        try {
            this.contractService.updateContract(contract);
        } catch (Exception e) {
            message = "修改合同联审单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
