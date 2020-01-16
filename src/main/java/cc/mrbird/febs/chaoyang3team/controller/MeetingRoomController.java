package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.MeetingRoom;
import cc.mrbird.febs.chaoyang3team.service.MeetingRoomService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("meetingRoom")
public class MeetingRoomController extends BaseController {

    private String message;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping
    @RequiresPermissions("meetingRoom:view")
    public Map<String, Object> MeetingRoomList(QueryRequest request, MeetingRoom meetingRoom, ServletRequest servletRequest) {
        return getDataTable(this.meetingRoomService.findMeetingRoomDetail(request, meetingRoom, servletRequest));
    }

    @Log("新增申请预约会议室")
    @PostMapping
    @RequiresPermissions("meetingRoom:add")
    public void addMeetingRoom(@Valid MeetingRoom meetingRoom, ServletRequest request) throws FebsException {
        try {
            this.meetingRoomService.createMeetingRoom(meetingRoom, request);
        } catch (Exception e) {
            message = "新增申请预约会议室失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改申请预约会议室")
    @PutMapping
    @RequiresPermissions("meetingRoom:update")
    public void updateMeetingRoom(@Valid MeetingRoom meetingRoom) throws FebsException {
        try {
            this.meetingRoomService.updateMeetingRoom(meetingRoom);
        } catch (Exception e) {
            message = "修改申请预约会议室失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除申请预约会议室")
    @DeleteMapping("/{meetingRoomIds}")
    @RequiresPermissions("meetingRoom:delete")
    public void deleteMeetingRooms(@NotBlank(message = "{required}") @PathVariable String meetingRoomIds) throws FebsException {
        try {
            String[] ids = meetingRoomIds.split(StringPool.COMMA);
            this.meetingRoomService.deleteMeetingRooms(ids);
        } catch (Exception e) {
            message = "删除申请预约会议室失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("meetingRoom:export")
    public void export(QueryRequest request, MeetingRoom meetingRoom, ServletRequest servletRequest,
                       HttpServletResponse response) throws FebsException {
        try {
            List<MeetingRoom> meetingRooms = this.meetingRoomService.findMeetingRoomDetail(request, meetingRoom, servletRequest).getRecords();
            ExcelKit.$Export(MeetingRoom.class, response).downXlsx(meetingRooms, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
