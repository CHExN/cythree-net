package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Article;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
public interface ArticleService extends IService<Article> {

    IPage<Article> findArticleDetail(QueryRequest request, Article article);

    void createArticle(Article article, ServletRequest servletRequest);

    void updateArticle(Article article);

    void deleteArticle(String[] articleIds);

    List<Article> getArticleList();

    Map<String, Object> updateArticleSort(Article article, String isUp);

}
