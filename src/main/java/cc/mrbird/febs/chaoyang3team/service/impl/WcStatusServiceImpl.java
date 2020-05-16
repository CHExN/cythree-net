package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcStatus;
import cc.mrbird.febs.chaoyang3team.dao.WcStatusMapper;
import cc.mrbird.febs.chaoyang3team.service.WcStatusService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@Service("wcStatusService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcStatusServiceImpl extends ServiceImpl<WcStatusMapper, WcStatus> implements WcStatusService {

    @Override
    public IPage<WcStatus> findWcStatusDetail(QueryRequest request, WcStatus wcStatus) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findWcStatusDetail(page, wcStatus);
        } catch (Exception e) {
            log.error("查询公厕状态异常", e);
            return null;
        }
    }

    @Override
    public void createWcStatus(WcStatus wcStatus) {
        wcStatus.setCreateTime(LocalDateTime.now());
        this.save(wcStatus);
    }

    @Override
    public void deleteWcStatus(String[] wcStatusIds) {
        List<String> ids = Arrays.asList(wcStatusIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateWcStatus(WcStatus wcStatus) {
        wcStatus.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(wcStatus);
    }
}
