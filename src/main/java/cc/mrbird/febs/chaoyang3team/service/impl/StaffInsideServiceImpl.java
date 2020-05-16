package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StaffInsideMapper;
import cc.mrbird.febs.chaoyang3team.domain.InsideFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.service.InsideFileService;
import cc.mrbird.febs.chaoyang3team.service.ContractInsideService;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.CalculationUtil;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import cc.mrbird.febs.common.utils.SortUtil;
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

import java.time.LocalDate;
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
    @Autowired
    private FileService fileService;
    @Autowired
    private InsideFileService insideFileService;

    @Override
    public IPage<StaffInside> findStaffInsideDetail(QueryRequest request, StaffInside staffInside) {
        try {
            Page<StaffInside> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sortNum", FebsConstant.ORDER_ASC, false);
            IPage<StaffInside> staffInsideDetail = this.baseMapper.findStaffInsideDetail(page, staffInside);
            staffInsideDetail.getRecords().forEach(this::addAgeAddYearByBirthDate);
            return staffInsideDetail;
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
        return addAgeAddYearByBirthDate(this.baseMapper.getStaffInsideByIdNum(idNum));
    }

    @Override
    public StaffInside getStaffInsideByStaffId(String staffId) {
        return addAgeAddYearByBirthDate(this.baseMapper.getStaffInsideByStaffId(staffId));
    }

    private StaffInside addAgeAddYearByBirthDate(StaffInside staffInside) {
        staffInside.setAge(CalculationUtil.getAge(staffInside.getBirthDate()));
        if (staffInside.getGender().equals("1")) {
            staffInside.setRetirementAge(60);
            staffInside.setRetirementDate(CalculationUtil.addYear(staffInside.getBirthDate(), 60));
        } else if (staffInside.getGender().equals("0")) {
            if (staffInside.getPost().equals("0") || staffInside.getPost().equals("3")) {
                staffInside.setRetirementAge(55);
                staffInside.setRetirementDate(CalculationUtil.addYear(staffInside.getBirthDate(), 55));
            } else if (staffInside.getPost().equals("1") || staffInside.getPost().equals("2")) {
                staffInside.setRetirementAge(50);
                staffInside.setRetirementDate(CalculationUtil.addYear(staffInside.getBirthDate(), 50));
            }
        }
        return staffInside;
    }


    @Override
    @Transactional
    public void createStaffInside(StaffInside staffInside) {
        staffInside.setCreateTime(LocalDateTime.now());
        if (StringUtils.isBlank(staffInside.getTransferDate())) {
            staffInside.setAddDate(LocalDate.now().toString());
        } else {
            staffInside.setAddDate(staffInside.getTransferDate());
        }
        if (staffInside.getSortNum() == null) staffInside.setSortNum("9999");
        this.save(staffInside);
        this.baseMapper.updateStaffInsideSortNum();
    }

    @Override
    @Transactional
    public void updateStaffInside(StaffInside staffInside) {
        staffInside.setModifyTime(LocalDateTime.now());
        String leaveDate = staffInside.getLeaveDate();
        if (staffInside.getIsLeave().equals("0")) {
            staffInside.setLeaveDate(null);
        }
        this.baseMapper.update(
                staffInside,
                Wrappers.<StaffInside>lambdaUpdate()
                        .set(StaffInside::getLeaveDate, staffInside.getLeaveDate())
                        .eq(StaffInside::getStaffId, staffInside.getStaffId()));
        if (staffInside.getSortNum() != null || staffInside.getLeaveDate() != null || staffInside.getLeaveDate() != leaveDate) {
            // 更新序号
            this.baseMapper.updateStaffInsideSortNum();
            this.baseMapper.updateStaffInsideLeaveSortNum();
        }
    }

    @Override
    @Transactional
    public void deleteStaffInside(String[] staffInsideIds, Integer deleted) {
        if (deleted == 0) {
            List<String> list = Arrays.asList(staffInsideIds);
            this.baseMapper.deleteBatchIds(list);
        } else if (deleted == 1) {
            this.baseMapper.deleteStaffInsideTrue(StringUtils.join(staffInsideIds, ","));
            // 根据编内人员id查找对应照片id
            List<String> fileIdList = this.insideFileService.findFileIdsByInsideIds(staffInsideIds);
            if (!fileIdList.isEmpty()) {
                String[] fileIds = fileIdList.toArray(new String[0]);
                this.deleteStaffInsidesFile(fileIds);
            }
        }
        // 更新序号
        this.baseMapper.updateStaffInsideSortNum();
        this.baseMapper.updateStaffInsideLeaveSortNum();
    }

    @Override
    @Transactional
    public void deleteStaffInsideAndContractInside(String[] staffInsideIds, Integer deleted) {
        // 逗号合并记录有staffInsideId的数组
        String staffInsideIdsStr = StringUtils.join(staffInsideIds, ',');
        // 查找ContractInsideIds
        List<String> contractInsideIdList = this.baseMapper.getContractInsideIds(staffInsideIdsStr);
        // 删除人员
        this.deleteStaffInside(staffInsideIds, deleted);
        // 删除合同
        if (!contractInsideIdList.isEmpty()) {
            // 这里 原先是(String[]) contractInsideIdList.toArray()
            // 会报错java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.String;
            // 正确写法如下
            this.contractInsideService.deleteContractInside(contractInsideIdList.toArray(new String[0]), deleted);
        }
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

    @Override
    @Transactional
    public void restoreStaffInside(String staffInsideIds) {
        this.baseMapper.restoreStaffInside(staffInsideIds);
        // 更新序号
        this.baseMapper.updateStaffInsideSortNum();
        this.baseMapper.updateStaffInsideLeaveSortNum();
    }

    @Override
    @Transactional
    public void togetherRestoreStaffInside(String staffInsideIds) {
        // 查找contractInsideIds
        List<String> contractInsideIdList = this.baseMapper.getContractInsideIds(staffInsideIds);
        // 恢复人员
        this.restoreStaffInside(staffInsideIds);
        // 恢复合同
        if (!contractInsideIdList.isEmpty()) {
            this.contractInsideService.restoreContractInside(StringUtils.join(contractInsideIdList, ","));
        }
    }

    @Override
    @Transactional
    public void deleteStaffInsidesFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.insideFileService.deleteInsideFilesByFileId(fileIds);
    }

    @Override
    public FebsResponse uploadStaffInsidePhoto(MultipartFile file, String id) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.insideFileService.createInsideFile(new InsideFile(Long.valueOf(id), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }

}
