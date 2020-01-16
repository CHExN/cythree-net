package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface ApplicationMapper extends BaseMapper<Application> {

    IPage<Application> findApplicationDetail(Page page, @Param("application") Application application,
                                             @Param("isLogistics") boolean isLogistics);

}
