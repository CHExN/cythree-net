package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ReviewModifyMapper;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.domain.ReviewModify;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.chaoyang3team.service.ReviewModifyService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("reviewModifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReviewModifyServiceImpl extends ServiceImpl<ReviewModifyMapper, ReviewModify> implements ReviewModifyService {

    @Autowired
    private UserManager userManager;

    @Autowired
    private MessageService messageService;

    @Override
    public IPage<ReviewModify> findReviewModifyDetail(QueryRequest request, ReviewModify reviewModify) {
        try {
            Page<ReviewModify> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findReviewModifyDetail(page, reviewModify);
        } catch (Exception e) {
            log.error("查询审核申请信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createReviewModify(ReviewModify reviewModify, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        User user = userManager.getUser(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
        reviewModify.setDeptId(user.getDeptId());
        reviewModify.setDeptName(user.getDeptName());
        reviewModify.setUsername(user.getUsername());
        reviewModify.setCreateTime(LocalDateTime.now());
        this.baseMapper.insert(reviewModify);

        StringBuilder message = new StringBuilder();
        message.append(user.getDeptName())
                .append("提交了 '")
                .append(reviewModify.getTableCname())
                .append("内")
                .append(reviewModify.getInfo())
                .append("' 的修改申请");
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                user.getUsername(),
                user.getDeptName(),
                "captain",
                null)
        );
        message.delete(0, message.length());
    }

    @Override
    @Transactional
    public void updateReviewModify(ReviewModify reviewModify) {
        reviewModify.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(reviewModify);

        StringBuilder message = new StringBuilder();
        message.append("'").append(reviewModify.getTableCname()).append("内").append(reviewModify.getInfo());

        if (reviewModify.getProcess().equals("1")) {
            message.append("' 的修改审核已通过");
        } else {
            message.append("' 的修改审核未通过");
        }
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                "captain",
                "队长",
                reviewModify.getUsername(),
                null)
        );
        message.delete(0, message.length());
    }

    @Override
    public void alreadyEdited(ReviewModify reviewModify) {
        this.baseMapper.update(
                null,
                Wrappers.<ReviewModify>lambdaUpdate()
                        .set(ReviewModify::getProcess, "3")
                        .eq(ReviewModify::getTableName, reviewModify.getTableName())
                        .eq(ReviewModify::getFieldName, reviewModify.getFieldName())
                        .eq(ReviewModify::getTableId, reviewModify.getTableId())
        );
    }

    @Override
    @Transactional
    public void deleteReviewModifys(String[] reviewModifyIds) {
        List<String> list = Arrays.asList(reviewModifyIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public ReviewModify findByInfo(ReviewModify reviewModify, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        reviewModify.setUsername(
                userManager.getUser(
                        JWTUtil.getUsername(
                                FebsUtil.decryptToken(
                                        httpServletRequest.getHeader("Authentication"))))
                        .getUsername());
        return baseMapper.selectReviewModifyOne(reviewModify);
    }
}
