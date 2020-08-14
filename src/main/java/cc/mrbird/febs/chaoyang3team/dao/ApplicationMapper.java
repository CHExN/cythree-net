package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Application1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface ApplicationMapper extends BaseMapper<Application1> {

    IPage<Application1> findApplicationDetail(Page page, @Param("application") Application1 application,
                                             @Param("is3") boolean is3,
                                             @Param("isLogistics") boolean isLogistics);

}
