package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.AttendanceFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface AttendanceFileMapper extends BaseMapper<AttendanceFile> {

    /**
     * 根据年月查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByYearMonth(@Param("year") String year, @Param("month") String month);
}
