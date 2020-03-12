package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.ReviewModify;
import cc.mrbird.febs.chaoyang3team.service.ReviewModifyService;
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
@RequestMapping("reviewModify")
public class ReviewModifyController extends BaseController {

    private String message;

    @Autowired
    private ReviewModifyService reviewModifyService;

    @GetMapping("getOne")
    public ReviewModify findByInfo(ReviewModify reviewModify, ServletRequest request) {
        return this.reviewModifyService.findByInfo(reviewModify, request);
    }

    @GetMapping
    @RequiresPermissions("reviewModify:view")
    public Map<String, Object> ReviewModifyList(QueryRequest request, ReviewModify reviewModify) {
        return getDataTable(this.reviewModifyService.findReviewModifyDetail(request, reviewModify));
    }

    @Log("新增审核申请")
    @PostMapping
    public void addReviewModify(@Valid ReviewModify reviewModify, ServletRequest request) throws FebsException {
        try {
            this.reviewModifyService.createReviewModify(reviewModify, request);
        } catch (Exception e) {
            message = "新增审核申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改审核申请")
    @PutMapping
    @RequiresPermissions("reviewModify:update")
    public void updateReviewModify(@Valid ReviewModify reviewModify) throws FebsException {
        try {
            this.reviewModifyService.updateReviewModify(reviewModify);
        } catch (Exception e) {
            message = "修改审核申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("对应审核申请已修改")
    @PutMapping("alreadyEdited")
    public void alreadyEditedReviewModify(@Valid ReviewModify reviewModify) throws FebsException {
        try {
            this.reviewModifyService.alreadyEdited(reviewModify);
        } catch (Exception e) {
            message = "对应审核申请已修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除审核申请")
    @DeleteMapping("/{reviewModifyIds}")
    @RequiresPermissions("reviewModify:delete")
    public void deleteReviewModifys(@NotBlank(message = "{required}") @PathVariable String reviewModifyIds) throws FebsException {
        try {
            String[] ids = reviewModifyIds.split(StringPool.COMMA);
            this.reviewModifyService.deleteReviewModifys(ids);
        } catch (Exception e) {
            message = "删除审核申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
