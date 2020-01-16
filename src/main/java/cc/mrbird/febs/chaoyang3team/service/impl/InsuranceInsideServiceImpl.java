package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.chaoyang3team.dao.InsuranceInsideMapper;
import cc.mrbird.febs.chaoyang3team.service.InsuranceInsideService;
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
@Service("insuranceInsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InsuranceInsideServiceImpl extends ServiceImpl<InsuranceInsideMapper, InsuranceInside> implements InsuranceInsideService {

    @Override
    public IPage<InsuranceInside> findInsuranceInsideDetail(QueryRequest request, InsuranceInside insuranceInside) {
        try {
            Page<InsuranceInside> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findInsuranceInsideDetail(page, insuranceInside);
        } catch (Exception e) {
            log.error("查询编内保险人员详细信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<InsuranceInside> findInsuranceInsideSimplify(QueryRequest request, InsuranceInside insuranceInside) {
        try {
            Page<InsuranceInside> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findInsuranceInsideSimplify(page, insuranceInside);
        } catch (Exception e) {
            log.error("查询编内保险人员简略信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createInsuranceInside(InsuranceInside insuranceInside) {
        insuranceInside.setCreateTime(LocalDateTime.now());
        this.save(insuranceInside);
    }

    @Override
    public void batchInsertInsuranceInside(List<InsuranceInside> insuranceInsides) {
        this.saveBatch(insuranceInsides);
    }

    @Override
    @Transactional
    public void updateInsuranceInside(InsuranceInside insuranceInside) {
        insuranceInside.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(insuranceInside);
    }

    @Override
    @Transactional
    public void deleteInsuranceInside(String[] insuranceInsideIds) {
        List<String> list = Arrays.asList(insuranceInsideIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public InsuranceInside getInsuranceInsideByIdNum(String idNum) {
        return this.baseMapper.getInsuranceInsideByIdNum(idNum);
    }
}
