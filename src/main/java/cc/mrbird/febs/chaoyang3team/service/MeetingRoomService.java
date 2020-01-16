package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.MeetingRoom;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface MeetingRoomService extends IService<MeetingRoom> {

    IPage<MeetingRoom> findMeetingRoomDetail(QueryRequest request, MeetingRoom meetingRoom, ServletRequest servletRequest);

    void createMeetingRoom(MeetingRoom meetingRoom, ServletRequest request);

    void updateMeetingRoom(MeetingRoom meetingRoom);

    void deleteMeetingRooms(String[] meetingRoomIds);

}
