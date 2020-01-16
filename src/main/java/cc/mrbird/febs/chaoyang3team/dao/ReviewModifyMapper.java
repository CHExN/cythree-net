package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ReviewModify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface ReviewModifyMapper extends BaseMapper<ReviewModify> {

    IPage<ReviewModify> findReviewModifyDetail(Page page, @Param("reviewModify") ReviewModify reviewModify);

    ReviewModify selectReviewModifyOne(ReviewModify reviewModify);

}
