package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ContractOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.ContractOutside;
import cc.mrbird.febs.chaoyang3team.service.ContractOutsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author CHExN
 */
@Slf4j
@Service("contractOutsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ContractOutsideServiceImpl extends ServiceImpl<ContractOutsideMapper, ContractOutside> implements ContractOutsideService {

    @Autowired
    private StaffOutsideService staffOutsideService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<ContractOutside> findContractOutsideDetail(QueryRequest request, ContractOutside contractOutside, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);
            // 如果拥有以下任意一个权限，就代表只能看到编外人员信息的其中一个分队
            if (userPermissions.contains("staffOutside:viewAttribution")) {
                contractOutside.setType("0"); // setType(0)的意思是只看归属人员的意思（viewAttribution）
            } else if (userPermissions.contains("staffOutside:viewClean")) {
                contractOutside.setTeam("保洁分队");
            } else if (userPermissions.contains("staffOutside:viewSouth")) {
                contractOutside.setTeam("南分队");
            } else if (userPermissions.contains("staffOutside:viewNorth")) {
                contractOutside.setTeam("北分队");
            } else if (userPermissions.contains("staffOutside:viewService")) {
                contractOutside.setTeam("维修分队");
            }

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
    public void deleteContractOutside(String[] contractOutsideIds, Integer deleted) {
        if (deleted == 0) {
            // 假删
            List<String> list = Arrays.asList(contractOutsideIds);
            this.baseMapper.deleteBatchIds(list);
        } else if (deleted == 1) {
            // 真删
            this.baseMapper.deleteContractOutsideTrue(StringUtils.join(contractOutsideIds, ","));
        }
    }

    @Override
    @Transactional
    public void deleteContractOutsideAndStaffOutside(String[] contractOutsideIds, Integer deleted) {
        // 逗号合并记录有contractOutsideId的数组
        String contractOutsideIdsStr = StringUtils.join(contractOutsideIds, ',');
        // 查找StaffOutsideIds
        List<String> staffOutsideIdList = this.baseMapper.getStaffOutsideIds(contractOutsideIdsStr);
        // 删除合同
        this.deleteContractOutside(contractOutsideIds, deleted);
        // 删除人员
        if (!staffOutsideIdList.isEmpty()) {
            this.staffOutsideService.deleteStaffOutside(staffOutsideIdList.toArray(new String[0]), deleted);
        }
    }

    @Override
    public void restoreContractOutside(String contractOutsideIds) {
        this.baseMapper.restoreContractOutside(contractOutsideIds);
    }

    @Override
    public void togetherRestoreContractOutside(String contractOutsideIds) {
        // 查找StaffOutsideIds
        List<String> staffOutsideIdList = this.baseMapper.getStaffOutsideIds(contractOutsideIds);
        // 恢复合同
        this.restoreContractOutside(contractOutsideIds);
        // 恢复人员
        if (!staffOutsideIdList.isEmpty()) {
            this.staffOutsideService.restoreStaffOutside(StringUtils.join(staffOutsideIdList, ","));
        }
    }
}
