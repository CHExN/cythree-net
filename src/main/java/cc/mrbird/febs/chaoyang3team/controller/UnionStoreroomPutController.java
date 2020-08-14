package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomPutOutService;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomPutService;
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
@RequestMapping("unionStoreroomPut")
public class UnionStoreroomPutController extends BaseController {

    private String message;

    @Autowired
    private UnionStoreroomPutService storeroomPutService;

    @Autowired
    private UnionStoreroomPutOutService storeroomPutOutService;

    @GetMapping
    @RequiresPermissions("unionStoreroomPut:view")
    public Map<String, Object> putStoreroomList(QueryRequest request, UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        return getDataTable(this.storeroomPutOutService.findStoreroomPutDetail(request, storeroomPutOut, servletRequest));
    }

    @GetMapping("storeroomByPutId")
    @RequiresPermissions("unionStoreroomPut:view")
    public List<UnionStoreroom> getStoreroomsByPutId(String putId) {
        return this.storeroomPutService.getStoreroomsByPutId(putId);
    }

    @Log("新增入库单")
    @PostMapping
    @RequiresPermissions("unionStoreroomPut:add")
    public void addStoreroomPut(@Valid UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) throws FebsException {
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
    @RequiresPermissions("unionStoreroomPut:update")
    public void updateStoreroomPut(@Valid UnionStoreroomPutOut storeroomPutOut) throws FebsException {
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
    @RequiresPermissions("unionStoreroomPut:delete")
    public Map<String, Object> deleteStoreroomPuts(@NotBlank(message = "{required}") @PathVariable String putIds) throws FebsException {
        try {
            String[] ids = putIds.split(StringPool.COMMA);
            return this.storeroomPutOutService.deleteStoreroomPuts(ids);
        } catch (Exception e) {
            message = "删除入库单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("即入即出")
    @PostMapping("inOut")
    @RequiresPermissions(value = {"unionStoreroomPut:add", "storeroomOut:add"})
    public void inOut(@Valid UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) throws FebsException {
        try {
            this.storeroomPutOutService.inOut(storeroomPutOut, servletRequest);
        } catch (Exception e) {
            message = "即入即出失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
