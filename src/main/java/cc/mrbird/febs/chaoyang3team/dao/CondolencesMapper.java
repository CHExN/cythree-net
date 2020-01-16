package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Condolences;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface CondolencesMapper extends BaseMapper<Condolences> {

    IPage<Condolences> findCondolencesDetail(Page page, @Param("condolences") Condolences condolences,
                                             @Param("roleId") boolean isRoleId);

}
