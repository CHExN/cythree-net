package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutOutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutService;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("storeroomPut")
public class StoreroomPutController extends BaseController {

    private String message;

    @Autowired
    private StoreroomPutService storeroomPutService;

    @Autowired
    private StoreroomPutOutService storeroomPutOutService;

    @GetMapping("index/{date}")
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String date) {
        Map<String, Object> data = new HashMap<>();
        // 入库类别占比
        List<Map<String, Object>> storeroomPutTypeApplicationProportion = storeroomPutService.findStoreroomPutTypeApplicationProportion(date);
        data.put("storeroomPutTypeApplicationProportion", storeroomPutTypeApplicationProportion);
        return new FebsResponse().data(data);
    }

    @GetMapping
    @RequiresPermissions("storeroomPut:view")
    public Map<String, Object> putStoreroomList(QueryRequest request, StoreroomPutOut storeroomPutOut) {
        return getDataTable(this.storeroomPutOutService.findStoreroomPutDetail(request, storeroomPutOut));
    }

    @GetMapping("storeroomByPutId")
    @RequiresPermissions("storeroomPut:view")
    public List<Storeroom> getStoreroomsByPutId(String putId) {
        return this.storeroomPutService.getStoreroomsByPutId(putId);
    }

    @Log("新增入库单")
    @PostMapping
    @RequiresPermissions("storeroomPut:add")
    public void addStoreroomPut(@Valid StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) throws FebsException {
        try {
            this.storeroomPutOutService.createStoreroomPut(storeroomPutOut, servletRequest);
        } catch (Exception e) {
            message = "新增入库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改入库单")
    @PutMapping
    @RequiresPermissions("storeroomPut:update")
    public void updateStoreroomPut(@Valid StoreroomPutOut storeroomPutOut) throws FebsException {
        try {
            this.storeroomPutOutService.updateStoreroom(storeroomPutOut);
        } catch (Exception e) {
            message = "修改入库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除入库单")
    @DeleteMapping("/{putIds}")
    @RequiresPermissions("storeroomPut:delete")
    public void deleteStoreroomPuts(@NotBlank(message = "{required}") @PathVariable String putIds) throws FebsException {
        try {
            String[] ids = putIds.split(StringPool.COMMA);
            this.storeroomPutOutService.deleteStoreroomPuts(ids);
        } catch (Exception e) {
            message = "删除入库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("即入即出")
    @PostMapping("inOut")
    @RequiresPermissions(value = {"storeroomPut:add", "storeroomOut:add"})
    public void inOut(@Valid StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) throws FebsException {
        try {
            this.storeroomPutOutService.inOut(storeroomPutOut, servletRequest);
        } catch (Exception e) {
            message = "即入即出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("export")
    @RequiresPermissions("storeroomPut:export")
    public void export(QueryRequest request, StoreroomPutOut storeroomPutOut, HttpServletResponse response) throws FebsException {

    }

}
