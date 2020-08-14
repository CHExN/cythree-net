package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.UnionStoreroomPutMapper;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPut;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomPutService;
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
@Service("unionStoreroomPutService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UnionStoreroomPutServiceImpl extends ServiceImpl<UnionStoreroomPutMapper, UnionStoreroomPut> implements UnionStoreroomPutService {

    @Override
    @Transactional
    public void deleteStoreroomPutsByStoreroomId(String[] storeroomIds) {
        List<String> list = Arrays.asList(storeroomIds);
        baseMapper.delete(new LambdaQueryWrapper<UnionStoreroomPut>().in(UnionStoreroomPut::getStoreroomId, list));
    }

    @Override
    @Transactional
    public void deleteStoreroomPutsByPutId(String[] putIds) {
        List<String> list = Arrays.asList(putIds);
        baseMapper.delete(new LambdaQueryWrapper<UnionStoreroomPut>().in(UnionStoreroomPut::getPutId, list));
    }

    @Override
    public List<String> getStoreroomIdsByPutIds(String[] putIds) {
        return this.baseMapper.getStoreroomIdsByStoreroomPutIds(StringUtils.join(putIds, ","));
    }

    @Override
    public List<UnionStoreroom> getStoreroomsByPutId(String ouId) {
        return this.baseMapper.getStoreroomsByStoreroomPutId(ouId);
    }

    @Override
    @Transactional
    public void batchInsertStoreroomPut(List<UnionStoreroomPut> list) {
        this.saveBatch(list);
    }


}
