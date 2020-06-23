package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ReviewDelete;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface ReviewDeleteService extends IService<ReviewDelete> {

    IPage<ReviewDelete> findReviewDeleteDetail(QueryRequest request, ReviewDelete reviewDelete);

    void createReviewDelete(ReviewDelete reviewDelete, ServletRequest request);

    void updateReviewDelete(ReviewDelete reviewDelete);

    void deleteReviewDeletes(String[] reviewDeleteIds);

    ReviewDelete findByInfo(ReviewDelete reviewDelete, ServletRequest request);

}
