package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.StoreroomOutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutOutService;
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
@RequestMapping("storeroomOut")
public class StoreroomOutController extends BaseController {

    private String message;

    @Autowired
    private StoreroomOutService storeroomOutService;

    @Autowired
    private StoreroomPutOutService storeroomPutOutService;

    @GetMapping
    @RequiresPermissions("storeroomOut:view")
    public Map<String, Object> outStoreroomList(QueryRequest request, StoreroomPutOut storeroomPutOut) {
        return getDataTable(this.storeroomPutOutService.findStoreroomOutDetail(request, storeroomPutOut));
    }

    @GetMapping("getStoreroomOutSimplify")
    public Map<String, Object> getStoreroomOutSimplify(QueryRequest request, StoreroomPutOut storeroomPutOut) {
        return getDataTable(this.storeroomPutOutService.getStoreroomOutSimplify(request, storeroomPutOut));
    }

    @GetMapping("storeroomByOutId")
    @RequiresPermissions("storeroomOut:view")

    public List<Storeroom> getStoreroomsByOutId(String outId) {
        return this.storeroomOutService.getStoreroomsByOutId(outId);
    }

    @Log("新增出库单")
    @PostMapping
    @RequiresPermissions("storeroomOut:add")
    public void addStoreroomOutByStoreroom(@Valid StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) throws FebsException {
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
    @RequiresPermissions("storeroomOut:update")
    public void updateStoreroomOut(@Valid StoreroomPutOut storeroomPutOut) throws FebsException {
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
    @RequiresPermissions("storeroomOut:delete")
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

    @PostMapping("export")
    @RequiresPermissions("storeroomOut:export")
    public void export(QueryRequest request, StoreroomPutOut storeroomPutOut, HttpServletResponse response) throws FebsException {

    }

}
