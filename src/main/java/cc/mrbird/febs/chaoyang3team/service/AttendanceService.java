package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Attendance;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author CHExN
 */
public interface AttendanceService extends IService<Attendance> {

    IPage<Attendance> findAttendanceDetail(QueryRequest request, Attendance attendance, ServletRequest servletRequest);

    void createAttendance(Attendance attendance, ServletRequest request);

    void batchInsertAttendance(List<Attendance> attendances);

    void updateAttendance(Attendance attendance);

    void deleteAttendance(String[] attendanceIds);

    List<Attendance> getAttendanceReport(String date);

    void deleteAttendanceFile(String[] fileIds);

    FebsResponse uploadAttendanceImage(MultipartFile file, String year, String month) throws Exception;

}
