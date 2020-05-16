package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcEvaluate;
import cc.mrbird.febs.chaoyang3team.service.WcEvaluateService;
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
@RequestMapping("wcEvaluate")
public class WcEvaluateController extends BaseController {

    private String message;

    @Autowired
    private WcEvaluateService wcEvaluateService;

    @GetMapping("/evaluate")
    @RequiresPermissions("wcEvaluate:view")
    public Map<String, Object> WcEvaluateList(QueryRequest request, WcEvaluate wcEvaluate) {
        wcEvaluate.setIsComplaint("1");
        return getDataTable(this.wcEvaluateService.findWcEvaluateDetail(request, wcEvaluate));
    }

    @GetMapping("/complaint")
    @RequiresPermissions("wcComplaint:view")
    public Map<String, Object> WcComplaintList(QueryRequest request, WcEvaluate wcEvaluate) {
        wcEvaluate.setIsComplaint("0");
        return getDataTable(this.wcEvaluateService.findWcEvaluateDetail(request, wcEvaluate));
    }

    @PostMapping("/weChat/add")
    public void addWcEvaluate(@Valid WcEvaluate wcEvaluate) throws FebsException {
        try {
            System.out.println(wcEvaluate);
            this.wcEvaluateService.createWcEvaluate(wcEvaluate);
        } catch (Exception e) {
            message = "新增公厕评价/投诉失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除公厕评价/投诉")
    @DeleteMapping("/{wcEvaluateIds}")
    @RequiresPermissions("wcEvaluate:delete")
    public void deleteWcEvaluate(@NotBlank(message = "{required}") @PathVariable String wcEvaluateIds) throws FebsException {
        try {
            String[] ids = wcEvaluateIds.split(StringPool.COMMA);
            this.wcEvaluateService.deleteWcEvaluate(ids);
        } catch (Exception e) {
            message = "删除公厕评价/投诉失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
