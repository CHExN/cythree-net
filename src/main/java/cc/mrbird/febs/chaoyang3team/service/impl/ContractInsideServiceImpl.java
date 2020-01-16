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
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findContractInsideDetail(page, contractInside);
        } catch (Exception e) {
            log.error("查询编内合同信息异常", e);
            return null;
        }
    }

    @Override
    public ContractInside getContractInside(String staffInsideId) {
        return this.baseMapper.getContractInside(staffInsideId);
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
    public void deleteContractInside(String[] contractInsideIds) {
        List<String> list = Arrays.asList(contractInsideIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteContractInsideAndStaffInside(String[] contractInsideIds) {
        List<String> contractInsideIdsList = Arrays.asList(contractInsideIds);

        // 逗号合并记录有contractInsideId的数组
        String contractInsideIdsStr = StringUtils.join(contractInsideIdsList, ',');
        // 查找StaffInsideIds
        List<String> staffInsideIdList = this.baseMapper.getStaffInsideIds(contractInsideIdsStr);
        // 删除
        this.baseMapper.deleteBatchIds(contractInsideIdsList);
        this.staffInsideService.deleteStaffInside((String[]) staffInsideIdList.toArray());
    }
}
