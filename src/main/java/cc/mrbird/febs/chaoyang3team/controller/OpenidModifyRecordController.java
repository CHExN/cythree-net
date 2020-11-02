package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.OpenidModifyRecord;
import cc.mrbird.febs.chaoyang3team.service.OpenidModifyRecordService;
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

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("openidModifyRecord")
public class OpenidModifyRecordController extends BaseController {

    @Autowired
    private OpenidModifyRecordService openidModifyRecordService;

    @GetMapping
    @RequiresPermissions("openidModifyRecord:view")
    public Map<String, Object> wcEvaluateList(QueryRequest request, OpenidModifyRecord openidModifyRecord) {
        return getDataTable(this.openidModifyRecordService.findOpenidModifyRecordDetail(request, openidModifyRecord));
    }

    @Log("删除openid更改记录")
    @DeleteMapping("/{openidModifyRecordIds}")
    @RequiresPermissions("openidModifyRecord:delete")
    public void deleteWcEvaluate(@NotBlank(message = "{required}") @PathVariable String openidModifyRecordIds) throws FebsException {
        try {
            String[] ids = openidModifyRecordIds.split(StringPool.COMMA);
            this.openidModifyRecordService.deleteOpenidModifyRecord(ids);
        } catch (Exception e) {
            String message = "删除公厕评价/投诉失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
