package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcStatusMapper extends BaseMapper<WcStatus> {

    IPage<WcStatus> findWcStatusDetail(Page page, @Param("wcStatus") WcStatus wcStatus);
    List<WcStatus> getWcStatusList(@Param("wcStatus") WcStatus wcStatus);

    List<String> getStatus();

}
