package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Price;
import cc.mrbird.febs.chaoyang3team.service.PriceService;
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
@RequestMapping("price")
public class PriceController extends BaseController {

    private String message;

    @Autowired
    private PriceService priceService;

    @GetMapping("name")
    public List<Price> checkUserName(String name) {
        return this.priceService.findByName(name);
    }

    @GetMapping
    @RequiresPermissions("price:view")
    public Map<String, Object> PriceList(QueryRequest request, Price price) {
        return getDataTable(this.priceService.findPrices(request, price));
    }

    @Log("新增物品价格")
    @PostMapping
    @RequiresPermissions("price:add")
    public void addPrice(@Valid Price price) throws FebsException {
        try {
            this.priceService.createPrice(price);
        } catch (Exception e) {
            message = "新增物品价格失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除物品价格")
    @DeleteMapping("/{priceIds}")
    @RequiresPermissions("price:delete")
    public void deletePrices(@NotBlank(message = "{required}") @PathVariable String priceIds) throws FebsException {
        try {
            String[] ids = priceIds.split(StringPool.COMMA);
            this.priceService.deletePrices(ids);
        } catch (Exception e) {
            message = "删除物品价格失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改物品价格")
    @PutMapping
    @RequiresPermissions("price:update")
    public void updatePrice(@Valid Price price) throws FebsException {
        try {
            this.priceService.updatePrice(price);
        } catch (Exception e) {
            message = "修改物品价格失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
