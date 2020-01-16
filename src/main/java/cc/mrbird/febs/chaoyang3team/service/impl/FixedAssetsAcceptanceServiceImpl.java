package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.FixedAssetsAcceptanceMapper;
import cc.mrbird.febs.chaoyang3team.domain.FixedAssetsAcceptance;
import cc.mrbird.febs.chaoyang3team.service.FixedAssetsAcceptanceService;
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

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("fixedAssetsAcceptanceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FixedAssetsAcceptanceServiceImpl extends ServiceImpl<FixedAssetsAcceptanceMapper, FixedAssetsAcceptance> implements FixedAssetsAcceptanceService {

    @Override
    public IPage<FixedAssetsAcceptance> findFixedAssetsAcceptance(QueryRequest request, FixedAssetsAcceptance fixedAssetsAcceptance) {
        try {
            /*LambdaQueryWrapper<FixedAssetsAcceptance> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(fixedAssetsAcceptance.getName())) {
                queryWrapper.like(FixedAssetsAcceptance::getName, fixedAssetsAcceptance.getName());
            }
            if (StringUtils.isNotBlank(fixedAssetsAcceptance.getNum())) {
                queryWrapper.like(FixedAssetsAcceptance::getNum, fixedAssetsAcceptance.getNum());
            }
            if (StringUtils.isNotBlank(fixedAssetsAcceptance.getRemark())) {
                queryWrapper.like(FixedAssetsAcceptance::getRemark, fixedAssetsAcceptance.getRemark());
            }
            if (StringUtils.isNotBlank(fixedAssetsAcceptance.getCreateTimeFrom()) &&
                StringUtils.isNotBlank(fixedAssetsAcceptance.getCreateTimeTo())) {
                queryWrapper
                        .ge(Job::getAcceptanceDate, fixedAssetsAcceptance.getCreateTimeFrom())
                        .le(Job::getAcceptanceDate, fixedAssetsAcceptance.getCreateTimeTo());
            }

            Page<FixedAssetsAcceptance> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);*/
            Page<FixedAssetsAcceptance> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findFixedAssetsAcceptanceDetail(page, fixedAssetsAcceptance);
        } catch (Exception e) {
            log.error("获取固定资产验收信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createFixedAssetsAcceptance(FixedAssetsAcceptance fixedAssetsAcceptance) {
        this.save(fixedAssetsAcceptance);
    }

    @Override
    @Transactional
    public void updateFixedAssetsAcceptance(FixedAssetsAcceptance fixedAssetsAcceptance) {
        this.baseMapper.updateById(fixedAssetsAcceptance);
    }

    @Override
    @Transactional
    public void deleteFixedAssetsAcceptance(String[] fixedAssetsAcceptanceIds) {
        List<String> list = Arrays.asList(fixedAssetsAcceptanceIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
