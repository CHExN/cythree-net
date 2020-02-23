package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ContractOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.ContractOutside;
import cc.mrbird.febs.chaoyang3team.service.ContractOutsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
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
@Service("contractOutsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ContractOutsideServiceImpl extends ServiceImpl<ContractOutsideMapper, ContractOutside> implements ContractOutsideService {

    @Autowired
    private StaffOutsideService staffOutsideService;

    @Override
    public IPage<ContractOutside> findContractOutsideDetail(QueryRequest request, ContractOutside contractOutside) {
        try {
            Page<ContractOutside> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.baseMapper.findContractOutsideDetail(page, contractOutside);
        } catch (Exception e) {
            log.error("查询编外合同信息异常", e);
            return null;
        }
    }

    @Override
    public ContractOutside getContractOutside(String idNum) {
        return this.baseMapper.getContractOutside(idNum);
    }

    @Override
    @Transactional
    public void createContractOutside(ContractOutside contractOutside) {
        this.save(contractOutside);
    }

    @Override
    @Transactional
    public void updateContractOutside(ContractOutside contractOutside) {
        this.baseMapper.updateById(contractOutside);
    }

    @Override
    @Transactional
    public void deleteContractOutside(String[] contractOutsideIds) {
        List<String> list = Arrays.asList(contractOutsideIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteContractOutsideAndStaffOutside(String[] contractOutsideIds) {
        List<String> contractOutsideIdsList = Arrays.asList(contractOutsideIds);

        // 逗号合并记录有contractOutsideId的数组
        String contractOutsideIdsStr = StringUtils.join(contractOutsideIdsList, ',');
        // 查找StaffOutsideIds
        List<String> staffOutsideIdList = this.baseMapper.getStaffOutsideIds(contractOutsideIdsStr);
        // 删除
        this.baseMapper.deleteBatchIds(contractOutsideIdsList);
        this.staffOutsideService.deleteStaffOutside((String[]) staffOutsideIdList.toArray());
    }
}
