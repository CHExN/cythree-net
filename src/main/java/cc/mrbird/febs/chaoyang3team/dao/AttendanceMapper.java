package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Attendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {

    IPage<Attendance> findAttendanceDetail(Page page, @Param("attendance") Attendance attendance);

    List<Attendance> getAttendanceReport(String date);
}
