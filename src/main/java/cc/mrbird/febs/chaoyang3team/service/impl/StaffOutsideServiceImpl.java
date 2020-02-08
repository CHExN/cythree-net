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
import java.util.*;

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
    public void createStaffOutside(StaffOutside staffOutside, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        staffOutside.setType("0");
        staffOutside.setCreateTime(LocalDateTime.now());
        if (staffOutside.getSortNum2() == null) staffOutside.setSortNum2("9999");
        // 通过用户名获取用户权限集合
        Set<String> userPermissions = this.userManager.getUserPermissions(username);
        if (userPermissions.contains("staffOutside:viewNorth")) {
            staffOutside.setType("1");
            staffOutside.setTeam("北分队");
        } else if (userPermissions.contains("staffOutside:viewSouth")) {
            staffOutside.setType("2");
            staffOutside.setTeam("南分队");
        } else if (userPermissions.contains("staffOutside:viewClean")) {
            staffOutside.setType("3");
            staffOutside.setTeam("保洁分队");
        } // 没有维修分队，因为维修分队好像是由劳资录入的

        // 插入
        this.save(staffOutside);

        // 更新序号
        this.updateStaffOutsideSortNum(staffOutside.getType());
    }



    @Override
    @Transactional
    public void updateStaffOutside(StaffOutside staffOutside) {
        System.out.println("=========================");
        System.out.println(staffOutside);
        staffOutside.setModifyTime(LocalDateTime.now());
        if (staffOutside.getIsLeave().equals("0")) {
            staffOutside.setLeaveDate(null);
        }
        this.baseMapper.update(
                staffOutside,
                Wrappers.<StaffOutside>lambdaUpdate() // 这里set是因为如果用默认的update，null值是不会更新的，set的话，不管你是什么都会更新
                        .set(StaffOutside::getLeaveDate, staffOutside.getLeaveDate())
                        .eq(StaffOutside::getStaffId, staffOutside.getStaffId()));
        if (staffOutside.getSortNum2() != null) {
            // 更新序号
            this.updateStaffOutsideSortNum(staffOutside.getType());
        }
    }

    @Override
    @Transactional
    public void deleteStaffOutside(String[] staffOutsideIds) {
        List<String> list = Arrays.asList(staffOutsideIds);
        this.baseMapper.deleteBatchIds(list);
        // 更新序号
        this.updateStaffOutsideSortNum(null);
    }

    @Override
    @Transactional
    public void deleteStaffOutsideAndContractOutside(String[] staffOutsideIds) {
        List<String> staffOutsideIdsList = Arrays.asList(staffOutsideIds);

        // 逗号合并记录有staffOutsideId的数组
        String staffOutsideIdsStr = StringUtils.join(staffOutsideIdsList, ',');
        // 查找ContractOutsideIds
        List<String> contractOutsideIdList = this.baseMapper.getContractOutsideIds(staffOutsideIdsStr);
        // 删除编外人员信息
        this.baseMapper.deleteBatchIds(staffOutsideIdsList);
        // 更新编外人员序号
        this.updateStaffOutsideSortNum(null);
        // 删除合同信息
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

    @Override
    @Transactional
    public Map<String, Object> updateSortStaffOutside(StaffOutside staffOutside, String isUp) {
        Map<String, Object> data = new HashMap<>();
        // 判断如果是第一名想要往上调位，或最后一名想往下调为，则直接返回
        if (staffOutside.getSortNum2().equals("1") && isUp.equals("0")) {
            data.put("status", 0);
            data.put("message", "已排名第一，无法再往上调序");
            return data;
        }
        Map<String, String> staffOutsideTypeCount = this.baseMapper.getStaffOutsideTypeCount();
        // 这里不能直接用String类型接收，并且不能toString(),会报类型转换错误java.math.BigDecimal cannot be cast to java.lang.String
        Object count = staffOutsideTypeCount.get(staffOutside.getType());
        if (staffOutside.getSortNum2().equals(count.toString()) && isUp.equals("1")) {
            data.put("status", 0);
            data.put("message", "已排名最后，无法再往下调序");
            return data;
        }

        // 这里进行位置调换操作
        long sortNum1 = Long.parseLong(staffOutside.getSortNum1());
        long sortNum2 = Long.parseLong(staffOutside.getSortNum2());
        this.baseMapper.updateStaffOutsideSwapSort(
                // 要调整排序位置
                sortNum1,
                sortNum2,
                // 被调换排序位置
                isUp.equals("0") ? sortNum1 - 1 : sortNum1 + 1,
                isUp.equals("0") ? sortNum2 - 1 : sortNum2 + 1);

        data.put("status", 1);
        data.put("message", "调整排序成功");
        return data;
    }


    public void updateStaffOutsideSortNum(String type) {
        // 更新序号
        this.baseMapper.updateStaffOutsideSortNum1();
        if (type == null) {
            for (int i = 0; i < 4; i++) {
                this.baseMapper.updateStaffOutsideSortNum2(i + "");
            }
        } else {
            this.baseMapper.updateStaffOutsideSortNum2(type);
        }
    }

}
