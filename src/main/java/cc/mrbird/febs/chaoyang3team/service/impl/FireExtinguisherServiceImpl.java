package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.FireExtinguisher;
import cc.mrbird.febs.chaoyang3team.dao.FireExtinguisherMapper;
import cc.mrbird.febs.chaoyang3team.service.FireExtinguisherService;
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

/**
 * @author CHExN
 */
@Slf4j
@Service("fireExtinguisherService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FireExtinguisherServiceImpl extends ServiceImpl<FireExtinguisherMapper, FireExtinguisher> implements FireExtinguisherService {

    @Override
    public IPage<FireExtinguisher> findFireExtinguisherDetail(QueryRequest request, FireExtinguisher fireExtinguisher) {
        try {
            Page<FireExtinguisher> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findFireExtinguisherDetail(page, fireExtinguisher);
        } catch (Exception e) {
            log.error("查询灭火器信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createFireExtinguisher(FireExtinguisher fireExtinguisher) {
        fireExtinguisher.setCreateTime(LocalDateTime.now());
        this.save(fireExtinguisher);
    }

    @Override
    @Transactional
    public void updateFireExtinguisher(FireExtinguisher fireExtinguisher) {
        fireExtinguisher.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(fireExtinguisher);
    }

    @Override
    @Transactional
    public void deleteFireExtinguisher(String[] fireExtinguisherIds) {
        List<String> list = Arrays.asList(fireExtinguisherIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public void batchInsertFireExtinguisher(List<FireExtinguisher> fireExtinguisherList) {
        this.saveBatch(fireExtinguisherList);
    }
}
