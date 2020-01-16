package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.MeetingRoom;
import cc.mrbird.febs.chaoyang3team.dao.MeetingRoomMapper;
import cc.mrbird.febs.chaoyang3team.service.MeetingRoomService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
@Service("meetingRoomService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements MeetingRoomService {

    @Autowired
    private UserManager userManager;

    @Override
    public IPage<MeetingRoom> findMeetingRoomDetail(QueryRequest request, MeetingRoom meetingRoom, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“办公室”（id：79）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("79");
            if (!isRoleId) meetingRoom.setDeptId(user.getDeptId());

            Page<MeetingRoom> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findMeetingRoom(page, meetingRoom);
        } catch (Exception e) {
            log.error("申请预约会议室信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createMeetingRoom(MeetingRoom meetingRoom, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        meetingRoom.setDeptId(user.getDeptId());
        meetingRoom.setDeptName(user.getDeptName());
        meetingRoom.setDateTime(DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        this.save(meetingRoom);
    }

    @Override
    @Transactional
    public void updateMeetingRoom(MeetingRoom meetingRoom) {
        this.baseMapper.updateById(meetingRoom);
    }

    @Override
    @Transactional
    public void deleteMeetingRooms(String[] meetingRoomIds) {
        List<String> list = Arrays.asList(meetingRoomIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
