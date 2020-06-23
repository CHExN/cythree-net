package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ReviewDelete;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface ReviewDeleteMapper extends BaseMapper<ReviewDelete> {

    IPage<ReviewDelete> findReviewDeleteDetail(Page page, @Param("reviewDelete") ReviewDelete reviewDelete);

    ReviewDelete selectReviewDeleteOne(ReviewDelete reviewDelete);

}
