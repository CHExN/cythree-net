package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.AttendanceFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface AttendanceFileService extends IService<AttendanceFile> {

    void deleteAttendanceFilesByFileId(String[] fileIds);

    FebsResponse findFilesByYearMonth(String year, String month);

    void createAttendanceFile(AttendanceFile attendanceFile);

}
