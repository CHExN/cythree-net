package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcEvaluateTag;
import cc.mrbird.febs.chaoyang3team.service.WcEvaluateTagService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
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

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wcEvaluateTag")
public class WcEvaluateTagController extends BaseController {

    private String message;

    @Autowired
    private WcEvaluateTagService wcEvaluateTagService;

    @GetMapping
    @RequiresPermissions("wcEvaluateTag:view")
    public List<WcEvaluateTag> wcEvaluateTagList() {
        return this.wcEvaluateTagService.getWcEvaluateTagList("0");
    }

    @GetMapping("all")
    public List<WcEvaluateTag> getWcEvaluateTag(String deleted) {
        return this.wcEvaluateTagService.getWcEvaluateTagList(deleted);
    }

    @Log("新增评价标签")
    @PostMapping
    @RequiresPermissions("wcEvaluateTag:add")
    public void addWcEvaluateTag(@Valid WcEvaluateTag wcEvaluateTag) throws FebsException {
        try {
            this.wcEvaluateTagService.createWcEvaluateTag(wcEvaluateTag);
        } catch (Exception e) {
            message = "新增评价标签失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改评价标签")
    @PutMapping
    @RequiresPermissions("wcEvaluateTag:update")
    public void updateWcEvaluateTag(@Valid WcEvaluateTag wcEvaluateTag) throws FebsException {
        try {
            this.wcEvaluateTagService.updateWcEvaluateTag(wcEvaluateTag);
        } catch (Exception e) {
            message = "修改评价标签失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除评价标签")
    @DeleteMapping("/{wcEvaluateTagIds}")
    @RequiresPermissions("wcEvaluateTag:delete")
    public void deleteWcEvaluateTag(@NotBlank(message = "{required}") @PathVariable String wcEvaluateTagIds) throws FebsException {
        try {
            String[] ids = wcEvaluateTagIds.split(StringPool.COMMA);
            this.wcEvaluateTagService.deleteWcEvaluateTag(ids);
        } catch (Exception e) {
            message = "删除评价标签失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
