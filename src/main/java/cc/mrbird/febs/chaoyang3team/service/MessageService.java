package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface MessageService extends IService<Message> {

    FebsResponse findMessages(ServletRequest request, Message message, int index);

    void oneToOne(Message message);

    void updateMessage(Message message);

    int readCount(ServletRequest request, Message message);

}
