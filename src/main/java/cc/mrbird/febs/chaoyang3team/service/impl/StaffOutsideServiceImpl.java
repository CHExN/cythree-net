package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StaffOutsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.OutsideFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.ContractOutsideService;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.OutsideFileService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.CalculationUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
    private FileService fileService;
    @Autowired
    private OutsideFileService outsideFileService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<StaffOutside> findStaffOutsideDetail(QueryRequest request, StaffOutside staffOutside, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);
            // 如果拥有以下任意一个权限，就代表只能看到编外人员信息的其中一个分队
            // staffOutside:viewAttribution不在人员里进行限制
            if (userPermissions.contains("staffOutside:viewClean")) {
                staffOutside.setTeam("保洁分队");
            } else if (userPermissions.contains("staffOutside:viewSouth")) {
                staffOutside.setTeam("南分队");
            } else if (userPermissions.contains("staffOutside:viewNorth")) {
                staffOutside.setTeam("北分队");
            } else if (userPermissions.contains("staffOutside:viewService")) {
                staffOutside.setTeam("维修分队");
            }

            Page<StaffOutside> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            IPage<StaffOutside> staffOutsideDetail = this.baseMapper.findStaffOutsideDetail(page, staffOutside);
            staffOutsideDetail.getRecords().forEach(this::addAgeAddYearByBirthDate);
            return staffOutsideDetail;
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
        return addAgeAddYearByBirthDate(this.baseMapper.getStaffOutsideByStaffId(staffId));
    }

    @Override
    public StaffOutside getStaffOutsideByIdNum(String idNum) {
        return addAgeAddYearByBirthDate(this.baseMapper.getStaffOutsideByIdNum(idNum));
    }

    private StaffOutside addAgeAddYearByBirthDate(StaffOutside staffOutside) {
        if (staffOutside == null) return null;
        staffOutside.setAge(CalculationUtil.getAge(staffOutside.getBirthDate()));
        if (staffOutside.getGender().equals("1")) {
            staffOutside.setRetirementAge(60);
            staffOutside.setRetirementDate(CalculationUtil.addYear(staffOutside.getBirthDate(), 60));
        } else if (staffOutside.getGender().equals("0")) {
            staffOutside.setRetirementAge(50);
            staffOutside.setRetirementDate(CalculationUtil.addYear(staffOutside.getBirthDate(), 50));
        }
        return staffOutside;
    }

    @Override
    @Transactional
    public void createStaffOutside(StaffOutside staffOutside, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        staffOutside.setType("0");
        staffOutside.setCreateTime(LocalDateTime.now());
        if (StringUtils.isBlank(staffOutside.getTransferDate())) {
            staffOutside.setAddDate(LocalDate.now().toString());
        } else {
            staffOutside.setAddDate(staffOutside.getTransferDate());
        }
        if (staffOutside.getSortNum2() == null) staffOutside.setSortNum2("99999");
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
        staffOutside.setModifyTime(LocalDateTime.now());
        String leaveDate = staffOutside.getLeaveDate();
        if (staffOutside.getIsLeave().equals("0")) {
            staffOutside.setLeaveDate(null);
        }
        this.baseMapper.update(
                staffOutside,
                Wrappers.<StaffOutside>lambdaUpdate() // 这里set是因为如果用默认的update，null值是不会更新的，set的话，不管你是什么都会更新
                        .set(StaffOutside::getLeaveDate, staffOutside.getLeaveDate())
                        .eq(StaffOutside::getStaffId, staffOutside.getStaffId()));
        //if (staffOutside.getSortNum2() != null || staffOutside.getLeaveDate() != null || staffOutside.getLeaveDate() != leaveDate) {
            // 更新序号
            this.updateStaffOutsideSortNum(null);
            this.updateStaffOutsideLeaveSortNum();
        //}
    }

    @Override
    @Transactional
    public void deleteStaffOutside(String[] staffOutsideIds, Integer deleted) {
        if (deleted == 0) {
            List<String> list = Arrays.asList(staffOutsideIds);
            this.baseMapper.deleteBatchIds(list);
        } else if (deleted == 1) {
            this.baseMapper.deleteStaffOutsideTrue(StringUtils.join(staffOutsideIds, ","));
            // 根据编外人员id查找对应照片id
            List<String> fileIdList = this.outsideFileService.findFileIdsByOutsideIds(staffOutsideIds);
            if (!fileIdList.isEmpty()) {
                String[] fileIds = fileIdList.toArray(new String[0]);
                this.deleteStaffOutsidesFile(fileIds);
            }
        }
        // 更新序号
        this.updateStaffOutsideSortNum(null);
        this.updateStaffOutsideLeaveSortNum();
    }

    @Override
    @Transactional
    public void deleteStaffOutsideAndContractOutside(String[] staffOutsideIds, Integer deleted) {
        // 逗号合并记录有staffOutsideId的数组
        String staffOutsideIdsStr = StringUtils.join(staffOutsideIds, ',');
        // 查找ContractOutsideIds
        List<String> contractOutsideIdList = this.baseMapper.getContractOutsideIds(staffOutsideIdsStr);
        // 删除人员
        this.deleteStaffOutside(staffOutsideIds, deleted);
        // 删除合同
        if (!contractOutsideIdList.isEmpty()) {
            this.contractOutsideService.deleteContractOutside(contractOutsideIdList.toArray(new String[0]), deleted);
        }
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
        // 获取每个类型人员的总数
        Map<String, String> staffOutsideTypeCount = this.baseMapper.getStaffOutsideTypeCount(staffOutside.getIsLeave());
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
                isUp.equals("0") ? sortNum2 - 1 : sortNum2 + 1,
                // 是否在职
                staffOutside.getIsLeave());

        data.put("status", 1);
        data.put("message", "调整排序成功");
        return data;
    }

    @Override
    public List<StaffOutside> getIncreaseOrDecreaseStaffOutside(StaffOutside staffOutside) {
        return baseMapper.getIncreaseOrDecreaseStaffOutside(staffOutside);
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

    public void updateStaffOutsideLeaveSortNum() {
        // 更新序号
        this.baseMapper.updateStaffOutsideLeaveSortNum1();
        for (int i = 0; i < 4; i++) {
            this.baseMapper.updateStaffOutsideLeaveSortNum2(i + "");
        }
    }

    @Override
    public void restoreStaffOutside(String staffOutsideIds) {
        this.baseMapper.restoreStaffOutside(staffOutsideIds);
        // 更新序号
        this.updateStaffOutsideSortNum(null);
        this.updateStaffOutsideLeaveSortNum();
    }

    @Override
    public void togetherRestoreStaffOutside(String staffOutsideIds) {
        // 查找ContractOutsideIds
        List<String> contractOutsideIdList = this.baseMapper.getContractOutsideIds(staffOutsideIds);
        // 恢复人员
        this.restoreStaffOutside(staffOutsideIds);
        // 恢复合同
        if (!contractOutsideIdList.isEmpty()) {
            this.contractOutsideService.restoreContractOutside(StringUtils.join(contractOutsideIdList, "."));
        }
    }

    @Override
    @Transactional
    public void deleteStaffOutsidesFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.outsideFileService.deleteOutsideFilesByFileId(fileIds);
    }

    @Override
    public FebsResponse uploadStaffOutsidePhoto(MultipartFile file, String id) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.outsideFileService.createOutsideFile(new OutsideFile(Long.valueOf(id), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }

}
