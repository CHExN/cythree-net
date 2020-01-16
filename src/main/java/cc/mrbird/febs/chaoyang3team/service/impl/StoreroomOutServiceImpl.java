package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StoreroomOutMapper;
import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomOut;
import cc.mrbird.febs.chaoyang3team.service.StoreroomOutService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("storeroomOutService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreroomOutServiceImpl extends ServiceImpl<StoreroomOutMapper, StoreroomOut> implements StoreroomOutService {

    @Override
    @Transactional
    public void deleteStoreroomOutsByStoreroomId(String[] storeroomIds) {
        List<String> list = Arrays.asList(storeroomIds);
        baseMapper.delete(new LambdaQueryWrapper<StoreroomOut>().in(StoreroomOut::getStoreroomId, list));
    }

    @Override
    @Transactional
    public void deleteStoreroomOutsByOutId(String[] outIds) {
        List<String> list = Arrays.asList(outIds);
        baseMapper.delete(new LambdaQueryWrapper<StoreroomOut>().in(StoreroomOut::getOutId, list));
    }

    @Override
    public List<String> getStoreroomIdsByOutIds(String[] outIds) {
        return this.baseMapper.getStoreroomIdsByStoreroomOutIds(StringUtils.join(outIds, ","));
    }

    @Override
    public List<Storeroom> getStoreroomsByOutId(String ouId) {
        return this.baseMapper.getStoreroomsByStoreroomOutId(ouId);
    }

    @Override
    @Transactional
    public void batchInsertStoreroomOut(List<StoreroomOut> list) {
        this.saveBatch(list);
    }

}
