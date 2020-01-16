package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.InsuranceOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceOutside;
import cc.mrbird.febs.chaoyang3team.service.InsuranceOutsideService;
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
@Service("insuranceOutsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InsuranceOutsideServiceImpl extends ServiceImpl<InsuranceOutsideMapper, InsuranceOutside> implements InsuranceOutsideService {

    @Override
    public IPage<InsuranceOutside> findInsuranceOutsideDetail(QueryRequest request, InsuranceOutside insuranceOutside) {
        try {
            Page<InsuranceOutside> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.baseMapper.findInsuranceOutsideDetail(page, insuranceOutside);
        } catch (Exception e) {
            log.error("查询编外保险人员详细信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<InsuranceOutside> findInsuranceOutsideSimplify(QueryRequest request, InsuranceOutside insuranceOutside) {
        try {
            Page<InsuranceOutside> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.baseMapper.findInsuranceOutsideSimplify(page, insuranceOutside);
        } catch (Exception e) {
            log.error("查询编外保险人员简略信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createInsuranceOutside(InsuranceOutside insuranceOutside) {
        insuranceOutside.setCreateTime(LocalDateTime.now());
        this.save(insuranceOutside);
    }

    @Override
    public void batchInsertInsuranceOutside(List<InsuranceOutside> insuranceOutsides) {
        this.saveBatch(insuranceOutsides);
    }

    @Override
    @Transactional
    public void updateInsuranceOutside(InsuranceOutside insuranceOutside) {
        insuranceOutside.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(insuranceOutside);
    }

    @Override
    @Transactional
    public void deleteInsuranceOutside(String[] insuranceOutsideIds) {
        List<String> list = Arrays.asList(insuranceOutsideIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public InsuranceInside getInsuranceOutsideByIdNum(String idNum) {
        return this.baseMapper.getInsuranceOutsideByIdNum(idNum);
    }
}
