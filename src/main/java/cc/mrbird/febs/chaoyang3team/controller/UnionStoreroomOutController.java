package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomOutService;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomPutOutService;
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

import javax.servlet.ServletRequest;
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
@RequestMapping("unionStoreroomOut")
public class UnionStoreroomOutController extends BaseController {

    private String message;

    @Autowired
    private UnionStoreroomOutService storeroomOutService;

    @Autowired
    private UnionStoreroomPutOutService storeroomPutOutService;

    @GetMapping
    @RequiresPermissions("unionStoreroomOut:view")
    public Map<String, Object> outStoreroomList(QueryRequest request, UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        return getDataTable(this.storeroomPutOutService.findStoreroomOutDetail(request, storeroomPutOut, servletRequest));
    }

    @GetMapping("storeroomByOutId")
    @RequiresPermissions("unionStoreroomOut:view")
    public List<UnionStoreroom> getStoreroomsByOutId(String outId) {
        return this.storeroomOutService.getStoreroomsByOutId(outId);
    }

    @Log("新增出库单")
    @PostMapping
    @RequiresPermissions("unionStoreroomOut:add")
    public void addStoreroomOutByStoreroom(@Valid UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) throws FebsException {
        try {
            this.storeroomPutOutService.createStoreroomOut(storeroomPutOut, servletRequest);
        } catch (Exception e) {
            message = "新增出库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改出库单")
    @PutMapping
    @RequiresPermissions("unionStoreroomOut:update")
    public void updateStoreroomOut(@Valid UnionStoreroomPutOut storeroomPutOut) throws FebsException {
        try {
            this.storeroomPutOutService.updateStoreroom(storeroomPutOut);
        } catch (Exception e) {
            message = "修改出库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除出库单")
    @DeleteMapping("/{outIds}")
    @RequiresPermissions("unionStoreroomOut:delete")
    public void deleteStoreroomOuts(@NotBlank(message = "{required}") @PathVariable String outIds) throws FebsException {
        try {
            String[] ids = outIds.split(StringPool.COMMA);
            this.storeroomPutOutService.deleteStoreroomOuts(ids);
        } catch (Exception e) {
            message = "删除出库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
