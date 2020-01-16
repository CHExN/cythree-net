package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("message")
public class MessageController extends BaseController {

    private String message;

    @Autowired
    private MessageService messageService;


    @GetMapping
    @RequiresPermissions("message:view")
    public FebsResponse messageList(ServletRequest request, Message message, int index) {
        return this.messageService.findMessages(request, message, index);
    }

    @GetMapping("readCount")
    public int readCount(ServletRequest request, Message message) {
        return this.messageService.readCount(request, message);
    }

    @Log("一对一推送消息")
    @PostMapping
    @RequiresPermissions("message:add")
    public void oneToOne(@Valid Message message) throws FebsException {
        try {
            this.messageService.oneToOne(message);
        } catch (Exception e) {
            this.message = "一对一推送消息失败";
            log.error(this.message, e);
            throw new FebsException(this.message);
        }
    }

    @Log("修改消息状态")
    @PutMapping
    @RequiresPermissions("message:update")
    public void updateMessage(@Valid Message message) throws FebsException {
        try {
            this.messageService.updateMessage(message);
        } catch (Exception e) {
            this.message = "修改通知消息状态失败";
            log.error(this.message, e);
            throw new FebsException(this.message);
        }
    }

}
