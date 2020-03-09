package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.BilateralMeeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface BilateralMeetingMapper extends BaseMapper<BilateralMeeting> {

    IPage<BilateralMeeting> findBilateralMeeting(Page page, @Param("bilateralMeeting") BilateralMeeting bilateralMeeting);

}
