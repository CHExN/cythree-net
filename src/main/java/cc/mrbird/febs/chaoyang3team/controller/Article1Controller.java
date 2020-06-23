package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Article;
import cc.mrbird.febs.chaoyang3team.service.ArticleService;
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
@RequestMapping("news")
public class Article1Controller extends BaseController {

    private String message;

    @Autowired
    private ArticleService articleService;


    @GetMapping("weChat/getArticleList")
    public List<Article> getArticleList() {
        return this.articleService.getArticleList();
    }

    @GetMapping
    @RequiresPermissions("article:view")
    public Map<String, Object> articleList(QueryRequest request, Article article) {
        return getDataTable(this.articleService.findArticleDetail(request, article));
    }

    @Log("调整顶置文章位置")
    @PutMapping("updateSort")
    @RequiresPermissions("article:view")
    public Map<String, Object> updateArticleSort(@Valid Article article, String isUp) throws FebsException {
        try {
            return this.articleService.updateArticleSort(article, isUp);
        } catch (Exception e) {
            message = "调整顶置文章位置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("新增动态信息")
    @PostMapping
    @RequiresPermissions("article:add")
    public void addArticle(@Valid Article article, ServletRequest servletRequest) throws FebsException {
        try {
            this.articleService.createArticle(article, servletRequest);
        } catch (Exception e) {
            message = "新增动态信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改动态信息")
    @PutMapping
    @RequiresPermissions("article:update")
    public void updateArticle(@Valid Article article) throws FebsException {
        try {
            this.articleService.updateArticle(article);
        } catch (Exception e) {
            message = "修改动态信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除动态信息")
    @DeleteMapping("/{articleIds}")
    @RequiresPermissions("article:delete")
    public void deleteArticles(@NotBlank(message = "{required}") @PathVariable String articleIds) throws FebsException {
        try {
            String[] ids = articleIds.split(StringPool.COMMA);
            this.articleService.deleteArticle(ids);
        } catch (Exception e) {
            message = "删除动态信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
