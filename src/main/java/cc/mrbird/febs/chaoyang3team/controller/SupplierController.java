package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Supplier;
import cc.mrbird.febs.chaoyang3team.service.SupplierService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("supplier")
public class SupplierController extends BaseController {

    private String message;

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    @RequiresPermissions("supplier:view")
    public Map<String, Object> supplierList(QueryRequest request, Supplier supplier) {
        return getDataTable(this.supplierService.findSupplier(request, supplier));
    }

    @GetMapping("all")
    public List<Supplier> allSupplier() {
        return this.supplierService.allSupplier();
    }

    @Log("新增供应商信息")
    @PostMapping
    public Long addSupplier(@Valid Supplier supplier) throws FebsException {
        try {
            return this.supplierService.createSupplier(supplier);
        } catch (Exception e) {
            message = "新增供应商信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改供应商信息")
    @PutMapping
//    @RequiresPermissions("supplier:update")
    public void updateSupplier(@Valid Supplier supplier) throws FebsException {
        try {
            this.supplierService.updateSupplier(supplier);
        } catch (Exception e) {
            message = "修改供应商信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改供应商状态")
    @DeleteMapping("/{supplierIds}")
//    @RequiresPermissions("supplier:delete")
    public void deleteSuppliers(@NotBlank(message = "{required}") @PathVariable String supplierIds) throws FebsException {
        try {
            // 这里用更新是因为，不能真的把供应商信息删了，只能改状态让它不显示
            String[] ids = supplierIds.split(StringPool.COMMA);
            this.supplierService.updateSuppliersStatus(ids);
        } catch (Exception e) {
            message = "修改供应商状态失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
