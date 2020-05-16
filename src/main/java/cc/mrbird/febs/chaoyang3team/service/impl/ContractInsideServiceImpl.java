package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ContractInsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.ContractInside;
import cc.mrbird.febs.chaoyang3team.service.ContractInsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("contractInsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ContractInsideServiceImpl extends ServiceImpl<ContractInsideMapper, ContractInside> implements ContractInsideService {

    @Autowired
    private StaffInsideService staffInsideService;

    @Override
    public IPage<ContractInside> findContractInsideDetail(QueryRequest request, ContractInside contractInside) {
        try {
            Page<ContractInside> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findContractInsideDetail(page, contractInside);
        } catch (Exception e) {
            log.error("查询编内合同信息异常", e);
            return null;
        }
    }

    @Override
    public ContractInside getContractInside(String idNum) {
        return this.baseMapper.getContractInside(idNum);
    }

    @Override
    @Transactional
    public void createContractInside(ContractInside contractInside) {
        this.save(contractInside);
    }

    @Override
    @Transactional
    public void updateContractInside(ContractInside contractInside) {
        this.baseMapper.updateById(contractInside);
    }

    @Override
    @Transactional
    public void deleteContractInside(String[] contractInsideIds, Integer deleted) {
        if (deleted == 0) {
            List<String> list = Arrays.asList(contractInsideIds);
            this.baseMapper.deleteBatchIds(list);
        } else if (deleted == 1) {
            this.baseMapper.deleteContractInsideTrue(StringUtils.join(contractInsideIds, ","));
        }
    }

    @Override
    @Transactional
    public void deleteContractInsideAndStaffInside(String[] contractInsideIds, Integer deleted) {
        // 逗号合并记录有contractInsideId的数组
        String contractInsideIdsStr = StringUtils.join(contractInsideIds, ',');
        // 查找StaffInsideIds
        List<String> staffInsideIdList = this.baseMapper.getStaffInsideIds(contractInsideIdsStr);
        // 删除合同
        this.deleteContractInside(contractInsideIds, deleted);
        // 删除人员
        if (!staffInsideIdList.isEmpty()) {
            this.staffInsideService.deleteStaffInside(staffInsideIdList.toArray(new String[0]), deleted);
        }
    }

    @Override
    public void restoreContractInside(String contractInsideIds) {
        this.baseMapper.restoreContractInside(contractInsideIds);
    }

    @Override
    public void togetherRestoreContractInside(String contractInsideIds) {
        // 查找StaffInsideIds
        List<String> staffInsideIdList = this.baseMapper.getStaffInsideIds(contractInsideIds);
        // 恢复合同
        this.restoreContractInside(contractInsideIds);
        // 恢复人员
        if (!staffInsideIdList.isEmpty()) {
            this.staffInsideService.restoreStaffInside(StringUtils.join(staffInsideIdList, ","));
        }
    }
}
