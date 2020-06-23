package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<Article> findArticleDetail(Page page, @Param("article") Article article);

    long getArticleTopCount();

    void updateArticleSwapSort(@Param("sortNum") Long sortNum, @Param("beSortNum")Long beSortNum);

    /**
     * 更新顶置文章序号
     */
    void updateArticleSortNum();

}
