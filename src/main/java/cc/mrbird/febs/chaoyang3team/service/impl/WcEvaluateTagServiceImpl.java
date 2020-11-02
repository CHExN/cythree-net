package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcEvaluateTagMapper;
import cc.mrbird.febs.chaoyang3team.domain.WcEvaluateTag;
import cc.mrbird.febs.chaoyang3team.service.WcEvaluateTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("wcEvaluateTagService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcEvaluateTagServiceImpl extends ServiceImpl<WcEvaluateTagMapper, WcEvaluateTag> implements WcEvaluateTagService {


    @Override
    public List<WcEvaluateTag> getWcEvaluateTagList(String deleted) {
        try {
            return this.baseMapper.getWcEvaluateTagList(deleted);
        } catch (Exception e) {
            log.error("获取评价标签信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createWcEvaluateTag(WcEvaluateTag wcEvaluateTag) {
        wcEvaluateTag.setCreateTime(LocalDateTime.now());
        this.save(wcEvaluateTag);
    }

    @Override
    @Transactional
    public void updateWcEvaluateTag(WcEvaluateTag wcEvaluateTag) {
        wcEvaluateTag.setModifyTime(LocalDateTime.now());
        this.updateById(wcEvaluateTag);
    }

    @Override
    @Transactional
    public void deleteWcEvaluateTag(String[] ids) {
        List<String> wcEvaluateTagIds = Arrays.asList(ids);
        this.baseMapper.deleteBatchIds(wcEvaluateTagIds);
    }
}
