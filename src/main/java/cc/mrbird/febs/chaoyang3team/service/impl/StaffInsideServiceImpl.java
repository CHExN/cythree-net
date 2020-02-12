package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StaffInsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.service.ContractInsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Service("staffInsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StaffInsideServiceImpl extends ServiceImpl<StaffInsideMapper, StaffInside> implements StaffInsideService {

    @Autowired
    private ContractInsideService contractInsideService;

    @Override
    public IPage<StaffInside> findStaffInsideDetail(QueryRequest request, StaffInside staffInside) {
        try {
            Page<StaffInside> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findStaffInsideDetail(page, staffInside);
        } catch (Exception e) {
            log.error("查询编内人员信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<StaffInside> findStaffInsideSimplify(QueryRequest request, StaffInside staffInside) {
        try {
            Page<StaffInside> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findStaffInsideSimplify(page, staffInside);
        } catch (Exception e) {
            log.error("查询合同绑定人员界面，还未绑定的人员列信息异常", e);
            return null;
        }
    }

    @Override
    public StaffInside getStaffInsideByIdNum(String idNum) {
        return this.baseMapper.getStaffInsideByIdNum(idNum);
    }

    @Override
    public StaffInside getStaffInsideByStaffId(String staffId) {
        return this.baseMapper.getStaffInsideByStaffId(staffId);
    }

    @Override
    @Transactional
    public void createStaffInside(StaffInside staffInside) {
        staffInside.setCreateTime(LocalDateTime.now());
        this.save(staffInside);
    }

    @Override
    @Transactional
    public void updateStaffInside(StaffInside staffInside) {
        staffInside.setModifyTime(LocalDateTime.now());
        if (staffInside.getIsLeave().equals("0")) {
            staffInside.setLeaveDate(null);
        }
        this.baseMapper.update(
                staffInside,
                Wrappers.<StaffInside>lambdaUpdate()
                        .set(StaffInside::getLeaveDate, staffInside.getLeaveDate())
                        .eq(StaffInside::getStaffId, staffInside.getStaffId()));
    }

    @Override
    @Transactional
    public void deleteStaffInside(String[] staffInsideIds) {
        List<String> list = Arrays.asList(staffInsideIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteStaffInsideAndContractInside(String[] staffInsideIds) {
        List<String> staffInsideIdsList = Arrays.asList(staffInsideIds);

        // 逗号合并记录有staffInsideId的数组
        String staffInsideIdsStr = StringUtils.join(staffInsideIdsList, ',');
        // 查找ContractInsideIds
        List<String> contractInsideIdList = this.baseMapper.getContractInsideIds(staffInsideIdsStr);
        // 删除
        this.baseMapper.deleteBatchIds(staffInsideIdsList);
        this.contractInsideService.deleteContractInside((String[]) contractInsideIdList.toArray());
    }

    @Override
    public List<String> getTechnicalType() {
        return this.baseMapper.getTechnicalType();
    }

    @Override
    public List<String> getEntryStatus() {
        return this.baseMapper.getEntryStatus();
    }

    @Override
    public List<String> getPostLevel() {
        return this.baseMapper.getPostLevel();
    }

    @Override
    public StaffInside getStaffIdByIdNum(String idNum) {
        return baseMapper.getStaffIdByIdNum(idNum);
    }

    @Override
    public List<StaffInside> getIncreaseOrDecreaseStaffInside(StaffInside staffInside) {
        return baseMapper.getIncreaseOrDecreaseStaffInside(staffInside);
    }
}
