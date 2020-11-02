package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcInspection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author CHExN
 */
public interface WcInspectionMapper extends BaseMapper<WcInspection> {

    IPage<WcInspection> findWcInspectionDetail(Page page, @Param("wcInspection") WcInspection wcInspection);

    @MapKey("username")
    Map<String, WcInspection> getWcInspectionUsername(Long deptId, Integer deleted);

}
