package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcTemplateMapper;
import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcTemplate;
import cc.mrbird.febs.chaoyang3team.service.WcTemplateService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Service("wcTemplateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcTemplateServiceImpl extends ServiceImpl<WcTemplateMapper, WcTemplate> implements WcTemplateService {

    @Override
    public IPage<WcTemplate> findWcTemplateDetail(QueryRequest request, WcTemplate wcTemplate) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            if (wcTemplate.getDeleted() == null) wcTemplate.setDeleted(0);
            return baseMapper.findWcTemplateDetail(page, wcTemplate);
        } catch (Exception e) {
            log.error("查询公厕巡检模板异常", e);
            return null;
        }
    }

    @Override
    public Map<String, WcTemplate> getAllWcTemplate(Integer deleted) {
        return baseMapper.getAllWcTemplate(deleted);
    }

    @Override
    public WcTemplate getWcTemplate(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public IPage<Wc> getTemplateWcList(QueryRequest request, Long id) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return baseMapper.getTemplateWcList(page, id);
        } catch (Exception e) {
            log.error("查询模板公厕异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createWcTemplate(WcTemplate wcTemplate) {
        wcTemplate.setCreateTime(LocalDateTime.now());
        this.save(wcTemplate);
    }

    @Override
    @Transactional
    public void updateWcTemplate(WcTemplate wcTemplate) {
        wcTemplate.setModifyTime(LocalDateTime.now());
        baseMapper.updateById(wcTemplate);
    }

    @Override
    @Transactional
    public void deleteWcTemplate(String[] wcTemplateIds) {
        List<String> ids = Arrays.asList(wcTemplateIds);
        baseMapper.deleteBatchIds(ids);
    }

}
