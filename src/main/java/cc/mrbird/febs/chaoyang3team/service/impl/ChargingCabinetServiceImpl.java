package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.ChargingCabinet;
import cc.mrbird.febs.chaoyang3team.dao.ChargingCabinetMapper;
import cc.mrbird.febs.chaoyang3team.service.ChargingCabinetService;
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
@Service("chargingCabinetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChargingCabinetServiceImpl extends ServiceImpl<ChargingCabinetMapper, ChargingCabinet> implements ChargingCabinetService {

    @Override
    public IPage<ChargingCabinet> findChargingCabinetDetail(QueryRequest request, ChargingCabinet chargingCabinet) {
        try {
            Page<ChargingCabinet> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findChargingCabinetDetail(page, chargingCabinet);
        } catch (Exception e) {
            log.error("查询充电柜信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createChargingCabinet(ChargingCabinet chargingCabinet) {
        chargingCabinet.setCreateTime(LocalDateTime.now());
        this.save(chargingCabinet);
    }

    @Override
    @Transactional
    public void updateChargingCabinet(ChargingCabinet chargingCabinet) {
        chargingCabinet.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(chargingCabinet);
    }

    @Override
    @Transactional
    public void deleteChargingCabinet(String[] chargingCabinetIds) {
        List<String> list = Arrays.asList(chargingCabinetIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
