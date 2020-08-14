package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ElectricityMapper;
import cc.mrbird.febs.chaoyang3team.domain.Electricity;
import cc.mrbird.febs.chaoyang3team.domain.WcElectricity;
import cc.mrbird.febs.chaoyang3team.service.ElectricityService;
import cc.mrbird.febs.chaoyang3team.service.WcElectricityService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service("electricityService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ElectricityServiceImpl extends ServiceImpl<ElectricityMapper, Electricity> implements ElectricityService {

    @Autowired
    private WcElectricityService wcElectricityService;

    @Override
    public IPage<Electricity> findElectricityDetail(QueryRequest request, Electricity electricity) {
        try {
            Page<Electricity> page = new Page<>();
            SortUtil.handlePageSort(request, page, "year DESC, month DESC, wcId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findElectricityDetail(page, electricity);
        } catch (Exception e) {
            log.error("查询电费信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createElectricity(Electricity electricity) {
        electricity.setCreateTime(LocalDateTime.now());
        if (electricity.getMonth().length() == 1) electricity.setMonth("0" + electricity.getMonth());
        // 根据年月，金额合计和公厕id查询电费信息id，如果有就说明已经有此数据，就做更新操作，没有就新增
        Long electricityId = this.baseMapper.findElectricityIdByDateAndWcId(
                electricity.getYear(), electricity.getMonth(), electricity.getWcId(), electricity.getTotalAmount());
        if (electricityId != null && electricityId > 0) {
            electricity.setElectricityId(electricityId);
            this.updateElectricity(electricity);
        } else {
            this.save(electricity);
            this.wcElectricityService.createWcElectricity(new WcElectricity(electricity.getWcId(), electricity.getElectricityId()));
        }
    }

    @Override
    @Transactional
    public void updateElectricity(Electricity electricity) {
        electricity.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(electricity);
        if (electricity.getWcId() != null) {
            // 先删除，后重新添加
            this.wcElectricityService.deleteByElectricityId(new String[]{electricity.getElectricityId().toString()});
            this.wcElectricityService.createWcElectricity(new WcElectricity(electricity.getWcId(), electricity.getElectricityId()));
        }
    }

    @Override
    @Transactional
    public void deleteElectricity(String[] electricityIds) {
        this.wcElectricityService.deleteByElectricityId(electricityIds);
        List<String> ids = Arrays.asList(electricityIds);
        this.baseMapper.deleteBatchIds(ids);
    }
}
