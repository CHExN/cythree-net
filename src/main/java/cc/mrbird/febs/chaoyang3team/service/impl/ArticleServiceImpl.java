package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ArticleMapper;
import cc.mrbird.febs.chaoyang3team.domain.Article;
import cc.mrbird.febs.chaoyang3team.service.ArticleService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Service("articleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public IPage<Article> findArticleDetail(QueryRequest request, Article article) {
        try {
            Page<Article> page = new Page<>();
            SortUtil.handlePageSort(request, page, "isTop DESC, sortNum ASC, createTime", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findArticleDetail(page, article);
        } catch (Exception e) {
            log.error("查询动态信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createArticle(Article article, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        article.setCreateTime(LocalDateTime.now());
        article.setUsername(username);
        this.save(article);
    }

    @Override
    @Transactional
    public void updateArticle(Article article) {
        article.setModifyTime(LocalDateTime.now());
        if (article.getIsTop() != null && article.getIsTop().equals("0")) article.setSortNum(999L);
        this.baseMapper.updateById(article);
        this.baseMapper.updateArticleSortNum();
    }

    @Override
    @Transactional
    public void deleteArticle(String[] articleIds) {
        List<String> ids = Arrays.asList(articleIds);
        this.baseMapper.deleteBatchIds(ids);
        this.baseMapper.updateArticleSortNum();
    }

    @Override
    public List<Article> getArticleList() {
        return baseMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .orderByDesc(Article::getIsTop)
                        .orderByAsc(Article::getSortNum)
                        .orderByDesc(Article::getCreateTime)
        );
    }

    @Override
    @Transactional
    public Map<String, Object> updateArticleSort(Article article, String isUp) {
        Map<String, Object> data = new HashMap<>();
        // 判断如果是第一名想要往上调位，或最后一名想往下调为，则直接返回
        if (article.getSortNum() == 1 && isUp.equals("0")) {
            data.put("status", 0);
            data.put("message", "已排名第一，无法再往上调序");
            return data;
        }
        // 获取顶置文章总数
        long articleTopCount = this.baseMapper.getArticleTopCount();
        if (article.getSortNum() == articleTopCount && isUp.equals("1")) {
            data.put("status", 0);
            data.put("message", "已排名最后，无法再往下调序");
            return data;
        }

        // 这里进行位置调换操作
        long sortNum = article.getSortNum();
        this.baseMapper.updateArticleSwapSort(sortNum, isUp.equals("0") ? sortNum - 1 : sortNum + 1);
        data.put("status", 1);
        data.put("message", "调整排序成功");
        return data;
    }

}
