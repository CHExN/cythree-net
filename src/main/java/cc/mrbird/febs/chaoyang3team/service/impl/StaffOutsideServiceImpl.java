package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StaffOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.ContractOutsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author CHExN
 */
@Slf4j
@Service("staffOutsideService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StaffOutsideServiceImpl extends ServiceImpl<StaffOutsideMapper, StaffOutside> implements StaffOutsideService {

    @Autowired
    private ContractOutsideService contractOutsideService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<StaffOutside> findStaffOutsideDetail(QueryRequest request, StaffOutside staffOutside, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        // 通过用户名获取用户权限集合
        Set<String> userPermissions = this.userManager.getUserPermissions(username);
        // 如果拥有以下任意一个权限，就代表只能看到编外人员信息的其中一个分队
        if (userPermissions.contains("staffOutside:viewClean")) {
            staffOutside.setTeam("保洁分队");
        } else if (userPermissions.contains("staffOutside:viewSouth")) {
            staffOutside.setTeam("南分队");
        } else if (userPermissions.contains("staffOutside:viewNorth")) {
            staffOutside.setTeam("北分队");
        } else if (userPermissions.contains("staffOutside:viewService")) {
            staffOutside.setTeam("维修分队");
        }
        try {
            Page<StaffOutside> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.baseMapper.findStaffOutsideDetail(page, staffOutside);
        } catch (Exception e) {
            log.error("查询编外人员信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<StaffOutside> findStaffOutsideSimplify(QueryRequest request, StaffOutside staffOutside) {
        try {
            Page<StaffOutside> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.baseMapper.findStaffOutsideSimplify(page, staffOutside);
        } catch (Exception e) {
            log.error("查询合同绑定人员界面，还未绑定的人员列信息异常", e);
            return null;
        }
    }

    @Override
    public StaffOutside getStaffOutsideByStaffId(String staffId) {
        return this.baseMapper.getStaffOutsideByStaffId(staffId);
    }

    @Override
    public StaffOutside getStaffOutsideByIdNum(String idNum) {
        return this.baseMapper.getStaffOutsideByIdNum(idNum);
    }

    @Override
    @Transactional
    public void createStaffOutside(StaffOutside staffOutside) {
        staffOutside.setCreateTime(LocalDateTime.now());
        this.save(staffOutside);
    }

    @Override
    @Transactional
    public void updateStaffOutside(StaffOutside staffOutside) {
        staffOutside.setModifyTime(LocalDateTime.now());
        if (staffOutside.getIsLeave().equals("0")) {
            staffOutside.setLeaveDate(null);
        }
        this.baseMapper.update(
                staffOutside,
                Wrappers.<StaffOutside>lambdaUpdate()
                        .set(StaffOutside::getLeaveDate, staffOutside.getLeaveDate())
                        .eq(StaffOutside::getStaffId, staffOutside.getStaffId()));
    }

    @Override
    @Transactional
    public void deleteStaffOutside(String[] staffOutsideIds) {
        List<String> list = Arrays.asList(staffOutsideIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void deleteStaffOutsideAndContractOutside(String[] staffOutsideIds) {
        List<String> staffOutsideIdsList = Arrays.asList(staffOutsideIds);

        // 逗号合并记录有staffOutsideId的数组
        String staffOutsideIdsStr = StringUtils.join(staffOutsideIdsList, ',');
        // 查找ContractOutsideIds
        List<String> contractOutsideIdList = this.baseMapper.getContractOutsideIds(staffOutsideIdsStr);
        // 删除
        this.baseMapper.deleteBatchIds(staffOutsideIdsList);
        this.contractOutsideService.deleteContractOutside((String[]) contractOutsideIdList.toArray());
    }

    @Override
    public List<String> getTechnicalType() {
        return this.baseMapper.getTechnicalType();
    }

    @Override
    public List<String> getPost() {
        return this.baseMapper.getPost();
    }

    @Override
    public List<String> getTeam() {
        return this.baseMapper.getTeam();
    }

    @Override
    public StaffOutside getStaffIdByIdNum(String idNum) {
        return baseMapper.getStaffIdByIdNum(idNum);
    }
}
