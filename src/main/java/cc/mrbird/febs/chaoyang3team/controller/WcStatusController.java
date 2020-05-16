package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcStatus;
import cc.mrbird.febs.chaoyang3team.service.WcStatusService;
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
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wcStatus")
public class WcStatusController extends BaseController {

    private String message;

    @Autowired
    private WcStatusService wcStatusService;

    @GetMapping
    @RequiresPermissions("wcStatus:view")
    public Map<String, Object> WcStatusList(QueryRequest request, WcStatus wcStatus) {
        return getDataTable(this.wcStatusService.findWcStatusDetail(request, wcStatus));
    }

    @Log("新增公厕状态")
    @PostMapping
    @RequiresPermissions("wcStatus:add")
    public void addWcStatus(@Valid WcStatus wcStatus) throws FebsException {
        try {
            this.wcStatusService.createWcStatus(wcStatus);
        } catch (Exception e) {
            message = "新增公厕状态失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除公厕状态")
    @DeleteMapping("/{wcStatusIds}")
    @RequiresPermissions("wcStatus:delete")
    public void deleteWcStatus(@NotBlank(message = "{required}") @PathVariable String wcStatusIds) throws FebsException {
        try {
            String[] ids = wcStatusIds.split(StringPool.COMMA);
            this.wcStatusService.deleteWcStatus(ids);
        } catch (Exception e) {
            message = "删除公厕状态失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改公厕状态")
    @PutMapping
    @RequiresPermissions("wcStatus:update")
    public void updateWcStatus(@Valid WcStatus wcStatus) throws FebsException {
        try {
            this.wcStatusService.updateWcStatus(wcStatus);
        } catch (Exception e) {
            message = "修改公厕状态失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
