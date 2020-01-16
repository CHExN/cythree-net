package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.Insurance;
import cc.mrbird.febs.chaoyang3team.dao.InsuranceMapper;
import cc.mrbird.febs.chaoyang3team.service.InsuranceService;
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
@Service("insuranceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InsuranceServiceImpl extends ServiceImpl<InsuranceMapper, Insurance> implements InsuranceService {

    @Override
    public IPage<Insurance> findInsuranceDetail(QueryRequest request, Insurance insurance) {
        try {
            Page<Insurance> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            Integer total = this.baseMapper.selectCount(null);
            return total > 0 ? this.baseMapper.findInsuranceDetail(page, insurance).setTotal(total) : null;
        } catch (Exception e) {
            log.error("查询工资信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createInsurance(Insurance insurance) {
        insurance.setCreateTime(LocalDateTime.now());
        this.save(insurance);
    }

    @Override
    @Transactional
    public void batchInsertInsurance(List<Insurance> insurances) {
        this.saveBatch(insurances);
    }

    @Override
    @Transactional
    public void updateInsurance(Insurance insurance) {
        insurance.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(insurance);
    }

    @Override
    @Transactional
    public void deleteInsurance(String[] insuranceIds) {
        List<String> ids = Arrays.asList(insuranceIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Insurance> findInsuranceSummary(Insurance insurance) {
        return this.baseMapper.findInsuranceSummary(insurance);
    }

}
