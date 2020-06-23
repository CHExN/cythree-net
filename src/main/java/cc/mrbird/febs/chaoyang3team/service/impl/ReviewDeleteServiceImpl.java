package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ReviewDeleteMapper;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.domain.ReviewDelete;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.chaoyang3team.service.ReviewDeleteService;
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
@Service("reviewDeleteService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReviewDeleteServiceImpl extends ServiceImpl<ReviewDeleteMapper, ReviewDelete> implements ReviewDeleteService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageService messageService;

    @Override
    public IPage<ReviewDelete> findReviewDeleteDetail(QueryRequest request, ReviewDelete reviewDelete) {
        try {
            Page<ReviewDelete> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findReviewDeleteDetail(page, reviewDelete);
        } catch (Exception e) {
            log.error("查询编外分队人员删除申请信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createReviewDelete(ReviewDelete reviewDelete, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        User user = userManager.getUser(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
        reviewDelete.setDeptId(user.getDeptId());
        reviewDelete.setDeptName(user.getDeptName());
        reviewDelete.setUsername(user.getUsername());
        reviewDelete.setCreateTime(LocalDateTime.now());
        this.baseMapper.insert(reviewDelete);

        StringBuilder message = new StringBuilder();
        message.append(user.getDeptName()).append("提交了编外分队人员的");
        if (reviewDelete.getType().equals("0")) {
            message.append("删除申请'");
        } else if (reviewDelete.getType().equals("1")) {
            message.append("转入归属人员申请'");
        } else {
            message.append("申请'");
        }
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                user.getUsername(),
                user.getDeptName(),
                "laborwage",
                null)
        );
        message.delete(0, message.length());
    }

    @Override
    @Transactional
    public void updateReviewDelete(ReviewDelete reviewDelete) {
        reviewDelete.setModifyTime(LocalDateTime.now());
        if (reviewDelete.getId() != null) {
            baseMapper.updateById(reviewDelete);
        } else {
            baseMapper.update(
                    reviewDelete,
                    Wrappers.<ReviewDelete>lambdaUpdate() // 这里set是因为如果用默认的update，null值是不会更新的，set的话，不管你是什么都会更新
                            .eq(ReviewDelete::getTableId, reviewDelete.getTableId())
                            .eq(ReviewDelete::getProcess, "1")
            );
        }
        if (reviewDelete.getProcess().equals("3")) return;

        StringBuilder message = new StringBuilder();
        message.append("' ").append(reviewDelete.getInfo());

        if (reviewDelete.getProcess().equals("1")) {
            message.append("' 的审核已通过");
        } else { // reviewDelete.getProcess().equals("2")
            message.append("' 的审核未通过");
        }
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                "bot",
                "系统",
                reviewDelete.getUsername(),
                null)
        );
        message.delete(0, message.length());
    }

    @Override
    @Transactional
    public void deleteReviewDeletes(String[] reviewDeleteIds) {
        List<String> list = Arrays.asList(reviewDeleteIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public ReviewDelete findByInfo(ReviewDelete reviewDelete, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        reviewDelete.setUsername(
                userManager.getUser(
                        JWTUtil.getUsername(
                                FebsUtil.decryptToken(
                                        httpServletRequest.getHeader("Authentication"))))
                        .getUsername());
        return baseMapper.selectReviewDeleteOne(reviewDelete);
    }
}
