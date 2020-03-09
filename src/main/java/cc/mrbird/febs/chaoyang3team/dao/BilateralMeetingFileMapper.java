package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.BilateralMeetingFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface BilateralMeetingFileMapper extends BaseMapper<BilateralMeetingFile> {

    /**
     * 根据上会议题ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByBilateralMeetingIds(@Param("bilateralMeetingIds") String bilateralMeetingIds);

    /**
     * 根据上会议题ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByBilateralMeetingId(@Param("bilateralMeetingId") String bilateralMeetingId);

}
