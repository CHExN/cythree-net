package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPut;
import cc.mrbird.febs.chaoyang3team.dao.StoreroomPutMapper;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Service("storeroomPutService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreroomPutServiceImpl extends ServiceImpl<StoreroomPutMapper, StoreroomPut> implements StoreroomPutService {

    @Override
    @Transactional
    public void deleteStoreroomPutsByStoreroomId(String[] storeroomIds) {
        List<String> list = Arrays.asList(storeroomIds);
        baseMapper.delete(new LambdaQueryWrapper<StoreroomPut>().in(StoreroomPut::getStoreroomId, list));
    }

    @Override
    @Transactional
    public void deleteStoreroomPutsByPutId(String[] putIds) {
        List<String> list = Arrays.asList(putIds);
        baseMapper.delete(new LambdaQueryWrapper<StoreroomPut>().in(StoreroomPut::getPutId, list));
    }

    @Override
    public List<String> getStoreroomIdsByPutIds(String[] putIds) {
        return this.baseMapper.getStoreroomIdsByStoreroomPutIds(StringUtils.join(putIds, ","));
    }

    @Override
    public List<Storeroom> getStoreroomsByPutId(String ouId) {
        return this.baseMapper.getStoreroomsByStoreroomPutId(ouId);
    }

    @Override
    @Transactional
    public void batchInsertStoreroomPut(List<StoreroomPut> list) {
        this.saveBatch(list);
    }

    @Override
    public List<Map<String, Object>> findStoreroomPutTypeApplicationProportion(String date) {
        return baseMapper.findStoreroomPutTypeApplicationProportion(date);
    }

}
