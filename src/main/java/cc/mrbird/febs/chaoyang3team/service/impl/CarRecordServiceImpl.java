package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.CarRecord;
import cc.mrbird.febs.chaoyang3team.dao.CarRecordMapper;
import cc.mrbird.febs.chaoyang3team.domain.CarRepair;
import cc.mrbird.febs.chaoyang3team.service.CarRecordService;
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
@Service("carRecordService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarRecordServiceImpl extends ServiceImpl<CarRecordMapper, CarRecord> implements CarRecordService {

    @Override
    public IPage<CarRecord> findCarRecordDetail(QueryRequest request, CarRecord carRecord) {
        try {
            Page<CarRepair> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findCarRecordDetail(page, carRecord);
        } catch (Exception e) {
            log.error("查询车辆维修保养记录异常", e);
            return null;
        }
    }

    @Override
    public IPage<CarRecord> findCarRecordDetailExcel(QueryRequest request, CarRecord carRecord) {
        try {
            Page<CarRecord> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findCarRecordDetailExcel(page, carRecord);
        } catch (Exception e) {
            log.error("查询车辆维修保养记录导出数据异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createCarRecord(CarRecord carRecord) {
        this.save(carRecord);
    }

    @Override
    @Transactional
    public void updateCarRecord(CarRecord carRecord) {
        this.baseMapper.updateById(carRecord);
    }

    @Override
    @Transactional
    public void deleteCarRecord(String[] carRecordIds) {
        List<String> list = Arrays.asList(carRecordIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
