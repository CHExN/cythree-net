package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.OpenidModifyRecordMapper;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.domain.OpenidModifyRecord;
import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.chaoyang3team.service.OpenidModifyRecordService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author CHExN
 */
@Slf4j
@Service("openidModifyRecordService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OpenidModifyRecordServiceImpl extends ServiceImpl<OpenidModifyRecordMapper, OpenidModifyRecord> implements OpenidModifyRecordService {

    @Autowired
    private MessageService messageService;

    @Override
    public IPage<OpenidModifyRecord> findOpenidModifyRecordDetail(QueryRequest request, OpenidModifyRecord openidModifyRecord) {

        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            if (openidModifyRecord.getDeleted() == null) openidModifyRecord.setDeleted(0);
            return baseMapper.findOpenidModifyRecordDetail(page, openidModifyRecord);
        } catch (Exception e) {
            log.error("查询公厕巡检模板异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createOpenidModifyRecord(OpenidModifyRecord openidModifyRecord) {
        openidModifyRecord.setCreateTime(LocalDateTime.now());
        this.save(openidModifyRecord);
        // 当日更换绑定微信次数超过2次则通知业务账号
        this.messageNotice(2, openidModifyRecord.getUsername());
    }

    /**
     * 当天更换openid次数超过dayCountLimit，就通知业务部门人员
     * @param dayCountLimit 当天更换openid次数限额
     * @param username 用户名称
     */
    private void messageNotice(int dayCountLimit, String username) {
        String now = DateUtil.formatFullTime(LocalDateTime.now(), "yyyy-MM-dd");

        Integer modifyCount = baseMapper.getModifyCountByUsername(username, now);
        if (modifyCount < dayCountLimit) return;

        StringBuilder message = new StringBuilder();
        message.append(username).append(" 在 ").append(now).append(" 当日更换绑定微信已达「").append(modifyCount).append("」次");
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                "bot",
                "系统",
                "business",
                null)
        );
    }

    @Override
    @Transactional
    public void deleteOpenidModifyRecord(String[] ids) {
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
