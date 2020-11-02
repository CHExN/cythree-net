package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author CHExN
 */
public interface WcTemplateMapper extends BaseMapper<WcTemplate> {

    IPage<WcTemplate> findWcTemplateDetail(Page page, @Param("wcTemplate") WcTemplate wcTemplate);

    IPage<Wc> getTemplateWcList(Page page, @Param("id") Long id);

    @MapKey("name")
    Map<String, WcTemplate> getAllWcTemplate(Integer deleted);
}
