package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcStoreroom;
import cc.mrbird.febs.chaoyang3team.service.WcStoreroomService;
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
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wcStoreroom")
public class WcStoreroomController extends BaseController {

    @Autowired
    private WcStoreroomService wcStoreroomService;

    @GetMapping
    @RequiresPermissions("wcStoreroom:view")
    public Map<String, Object> wcStoreroomList(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest) {
        return getDataTable(this.wcStoreroomService.findWcStoreroomDetail(request, wcStoreroom, servletRequest));
    }

    @Log("查询要删除的id数量")
    @GetMapping("ids")
    @RequiresPermissions("wcStoreroom:delete")
    public Integer getDeleteWcStoreroomIds(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest) {
        return this.wcStoreroomService.getDeleteWcStoreroomIds(request, wcStoreroom, servletRequest).size();
    }

    @Log("删除分配记录")
    @DeleteMapping("/{wcStoreroomIds}")
    @RequiresPermissions("wcStoreroom:delete")
    public void deleteWcStoreroomsByWcIdAndStoreroomId(@NotBlank(message = "{required}") @PathVariable String wcStoreroomIds) throws FebsException {
        try {
            String[] ids = wcStoreroomIds.split(StringPool.COMMA);
            this.wcStoreroomService.deleteWcStorerooms(ids);
        } catch (Exception e) {
            String message = "删除分配记录失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除全部分配记录")
    @PostMapping("/deleteAll")
    @RequiresPermissions("wcStoreroom:delete")
    public void deleteAllWcStoreroomsByWcIdAndStoreroomId(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest) throws FebsException {
        try {
            this.wcStoreroomService.deleteAllWcStorerooms(request, wcStoreroom, servletRequest);
        } catch (Exception e) {
            String message = "删除分配记录失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
