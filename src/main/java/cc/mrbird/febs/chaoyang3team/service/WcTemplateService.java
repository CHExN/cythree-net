package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcTemplate;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author CHExN
 */
public interface WcTemplateService extends IService<WcTemplate> {

    IPage<WcTemplate> findWcTemplateDetail(QueryRequest request, WcTemplate wcTemplate);

    void createWcTemplate(WcTemplate wcTemplate);

    void updateWcTemplate(WcTemplate wcTemplate);

    void deleteWcTemplate(String[] ids);

    Map<String, WcTemplate> getAllWcTemplate(Integer deleted);

    WcTemplate getWcTemplate(Long id);

    IPage<Wc> getTemplateWcList(QueryRequest request, Long id);
}
