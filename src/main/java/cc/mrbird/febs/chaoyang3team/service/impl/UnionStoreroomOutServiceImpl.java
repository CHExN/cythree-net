package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.UnionStoreroomOutMapper;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomOut;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomOutService;
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
@Service("unionStoreroomOutService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UnionStoreroomOutServiceImpl extends ServiceImpl<UnionStoreroomOutMapper, UnionStoreroomOut> implements UnionStoreroomOutService {

    @Override
    @Transactional
    public void deleteStoreroomOutsByStoreroomId(String[] storeroomIds) {
        List<String> list = Arrays.asList(storeroomIds);
        baseMapper.delete(new LambdaQueryWrapper<UnionStoreroomOut>().in(UnionStoreroomOut::getStoreroomId, list));
    }

    @Override
    @Transactional
    public void deleteStoreroomOutsByOutId(String[] outIds) {
        List<String> list = Arrays.asList(outIds);
        baseMapper.delete(new LambdaQueryWrapper<UnionStoreroomOut>().in(UnionStoreroomOut::getOutId, list));
    }

    @Override
    public List<String> getStoreroomIdsByOutIds(String[] outIds) {
        return this.baseMapper.getStoreroomIdsByStoreroomOutIds(StringUtils.join(outIds, ","));
    }

    @Override
    public List<UnionStoreroom> getStoreroomsByOutId(String ouId) {
        return this.baseMapper.getStoreroomsByStoreroomOutId(ouId);
    }

    @Override
    @Transactional
    public void batchInsertStoreroomOut(List<UnionStoreroomOut> list) {
        this.saveBatch(list);
    }

}
