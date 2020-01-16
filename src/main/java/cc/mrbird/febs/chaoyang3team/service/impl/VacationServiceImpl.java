package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.VacationMapper;
import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import cc.mrbird.febs.chaoyang3team.domain.VacationImport;
import cc.mrbird.febs.chaoyang3team.service.VacationService;
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
@Service("vacationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VacationServiceImpl extends ServiceImpl<VacationMapper, Vacation> implements VacationService {

    @Override
    public IPage<Vacation> findVacationDetail(QueryRequest request, Vacation vacation) {
        try {
            Page<Vacation> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findVacationDetail(page, vacation);
        } catch (Exception e) {
            log.error("查询人员休假信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createVacation(Vacation vacation) {
        vacation.setCreateTime(LocalDateTime.now());
        this.save(vacation);
    }

    @Override
    @Transactional
    public void updateVacation(Vacation vacation) {
        vacation.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(vacation);
    }

    @Override
    @Transactional
    public void deleteVacation(String[] vacationIds) {
        List<String> list = Arrays.asList(vacationIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<String> getType() {
        return this.baseMapper.getType();
    }

    @Override
    public IPage<VacationImport> findInsideAnnualLeave(QueryRequest request, Vacation vacation) {
        try {
            Page<VacationImport> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            Long total = this.baseMapper.findInsideAnnualLeaveCount(vacation);
            return total > 0 ? this.baseMapper.findInsideAnnualLeave(page, vacation).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询编内年假统计信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<VacationImport> findInsOutVacation(QueryRequest request, Vacation vacation) {
        try {
            Page<VacationImport> page = new Page<>();
            page.setSearchCount(false);
            switch (vacation.getInsOut()) {
                case "0": {
                    SortUtil.handlePageSort(request, page, false);
                    Long total = this.baseMapper.findInsVacationCount(vacation);
                    return total > 0 ? this.baseMapper.findInsVacation(page, vacation).setTotal(total) : null;
                }
                case "1": {
                    SortUtil.handlePageSort(request, page, false);
                    Long total = this.baseMapper.findOutVacationCount(vacation);
                    return total > 0 ? this.baseMapper.findOutVacation(page, vacation).setTotal(total) : null;
                }
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("查询编内年假统计信息异常", e);
            return null;
        }
    }
}
