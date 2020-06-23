package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.ReviewDelete;
import cc.mrbird.febs.chaoyang3team.service.ReviewDeleteService;
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
@RequestMapping("reviewDelete")
public class ReviewDeleteController extends BaseController {

    private String message;

    @Autowired
    private ReviewDeleteService reviewDeleteService;

    @GetMapping("getOne")
    public ReviewDelete findByInfo(ReviewDelete reviewDelete, ServletRequest request) {
        return this.reviewDeleteService.findByInfo(reviewDelete, request);
    }

    @GetMapping
    @RequiresPermissions("reviewDelete:view")
    public Map<String, Object> ReviewDeleteList(QueryRequest request, ReviewDelete reviewDelete) {
        return getDataTable(this.reviewDeleteService.findReviewDeleteDetail(request, reviewDelete));
    }

    @Log("新增编外分队人员删除申请")
    @PostMapping
    public void addReviewDelete(@Valid ReviewDelete reviewDelete, ServletRequest request) throws FebsException {
        try {
            this.reviewDeleteService.createReviewDelete(reviewDelete, request);
        } catch (Exception e) {
            message = "新增编外分队人员删除申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外分队人员删除申请")
    @PutMapping
    @RequiresPermissions("reviewDelete:update")
    public void updateReviewDelete(@Valid ReviewDelete reviewDelete) throws FebsException {
        try {
            this.reviewDeleteService.updateReviewDelete(reviewDelete);
        } catch (Exception e) {
            message = "修改编外分队人员删除申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外分队人员删除申请")
    @DeleteMapping("/{reviewDeleteIds}")
    @RequiresPermissions("reviewDelete:delete")
    public void deleteReviewDeletes(@NotBlank(message = "{required}") @PathVariable String reviewDeleteIds) throws FebsException {
        try {
            String[] ids = reviewDeleteIds.split(StringPool.COMMA);
            this.reviewDeleteService.deleteReviewDeletes(ids);
        } catch (Exception e) {
            message = "删除编外分队人员删除申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
