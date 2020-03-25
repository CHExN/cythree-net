package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.MessageMapper;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author CHExN
 */
@Slf4j
@Service("messageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public FebsResponse findMessages(ServletRequest request, Message message, int index) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            message.setAddressee(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
            return new FebsResponse().data(this.baseMapper.findMessage(message, index));
        } catch (Exception e) {
            log.error("获取通知列表失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void oneToOne(Message message) {
        message.setDatetime(DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        message.setStatus("1");// 默认未读
        if (StringUtils.isNotBlank(message.getAddressee())) {
            String[] userNames = message.getAddressee().split(",");
            Arrays.stream(userNames).forEach(username -> {
                message.setAddressee(username);
                this.save(message);
                this.template.convertAndSendToUser(username, "/oneToOne/greetings", message);
            });
        }
    }

    @Override
    @Transactional
    public void updateMessage(Message message) {
        message.setStatus("2");// 设置为已读
        this.baseMapper.updateById(message);
    }

    @Override
    public int readCount(ServletRequest request, Message message) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        message.setAddressee(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
        return baseMapper.selectCount(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getAddressee, message.getAddressee())
                        .eq(Message::getStatus, message.getStatus()));
    }

}
