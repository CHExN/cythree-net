package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ReviewModify;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface ReviewModifyService extends IService<ReviewModify> {

    IPage<ReviewModify> findReviewModifyDetail(QueryRequest request, ReviewModify reviewModify);

    void createReviewModify(ReviewModify reviewModify, ServletRequest request);

    void updateReviewModify(ReviewModify reviewModify);

    void alreadyEdited(ReviewModify reviewModify);

    void deleteReviewModifys(String[] reviewModifyIds);

    ReviewModify findByInfo(ReviewModify reviewModify, ServletRequest request);

}
