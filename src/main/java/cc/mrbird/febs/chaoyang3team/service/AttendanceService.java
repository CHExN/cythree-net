package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Attendance;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author CHExN
 */
public interface AttendanceService extends IService<Attendance> {

    IPage<Attendance> findAttendanceDetail(QueryRequest request, Attendance attendance);

    void createAttendance(Attendance attendance, ServletRequest request);

    void batchInsertAttendance(List<Attendance> attendances);

    void updateAttendance(Attendance attendance);

    void deleteAttendance(String[] attendanceIds);

    List<Attendance> getAttendanceReport(String date);

}
