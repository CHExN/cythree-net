package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Condolences;
import cc.mrbird.febs.chaoyang3team.service.CondolencesService;
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
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("condolences")
public class CondolencesController extends BaseController {

    private String message;

    @Autowired
    private CondolencesService condolencesService;

    @GetMapping
    @RequiresPermissions("condolences:view")
    public Map<String, Object> CondolencesList(QueryRequest request, Condolences condolences, ServletRequest servletRequest) {
        return getDataTable(this.condolencesService.findCondolencesDetail(request, condolences, servletRequest));
    }

    @Log("新增职工慰问登记")
    @PostMapping
    @RequiresPermissions("condolences:add")
    public void addCondolences(@Valid Condolences condolences, ServletRequest request) throws FebsException {
        try {
            this.condolencesService.createCondolences(condolences, request);
        } catch (Exception e) {
            message = "新增职工慰问登记失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改职工慰问登记")
    @PutMapping
    @RequiresPermissions("condolences:update")
    public void updateCondolences(@Valid Condolences condolences) throws FebsException {
        try {
            this.condolencesService.updateCondolences(condolences);
        } catch (Exception e) {
            message = "修改职工慰问登记失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除职工慰问登记")
    @DeleteMapping("/{condolencesIds}")
    @RequiresPermissions("condolences:delete")
    public void deleteCondolencess(@NotBlank(message = "{required}") @PathVariable String condolencesIds) throws FebsException {
        try {
            String[] ids = condolencesIds.split(StringPool.COMMA);
            this.condolencesService.deleteCondolences(ids);
        } catch (Exception e) {
            message = "删除职工慰问登记失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
