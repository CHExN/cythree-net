package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StaffSendMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.SendFile;
import cc.mrbird.febs.chaoyang3team.domain.StaffSend;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.SendFileService;
import cc.mrbird.febs.chaoyang3team.service.StaffSendService;
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
@Service("staffSendService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StaffSendServiceImpl extends ServiceImpl<StaffSendMapper, StaffSend> implements StaffSendService {

    @Autowired
    private FileService fileService;
    @Autowired
    private SendFileService sendFileService;

    @Override
    public IPage<StaffSend> findStaffSendDetail(QueryRequest request, StaffSend staffSend) {
        try {
            Page<StaffSend> page = new Page<>();
            SortUtil.handlePageSort(request, page, "IS_LEAVE ASC, sortNum", FebsConstant.ORDER_ASC, false);
            IPage<StaffSend> staffSendDetail = this.baseMapper.findStaffSendDetail(page, staffSend);
            staffSendDetail.getRecords().forEach(this::addAgeAddYearByBirthDate);
            return staffSendDetail;
        } catch (Exception e) {
            log.error("查询劳务派遣人员信息异常", e);
            return null;
        }
    }

    @Override
    public StaffSend getStaffSendByStaffId(String staffId) {
        return addAgeAddYearByBirthDate(this.baseMapper.getStaffSendByStaffId(staffId));
    }

    @Override
    public StaffSend getStaffSendByIdNum(String idNum) {
        return addAgeAddYearByBirthDate(this.baseMapper.getStaffSendByIdNum(idNum));
    }

    private StaffSend addAgeAddYearByBirthDate(StaffSend staffSend) {
        if (staffSend == null) return null;
        staffSend.setAge(CalculationUtil.getAge(staffSend.getBirthDate()));
        return staffSend;
    }

    @Override
    @Transactional
    public void createStaffSend(StaffSend staffSend) {
        staffSend.setCreateTime(LocalDateTime.now());
        if (StringUtils.isBlank(staffSend.getTransferDate())) {
            staffSend.setAddDate(LocalDate.now().toString());
        } else {
            staffSend.setAddDate(staffSend.getTransferDate());
        }
        if (staffSend.getSortNum() == null) staffSend.setSortNum("9999");
        this.save(staffSend);
        this.baseMapper.updateStaffSendSortNum();
    }

    @Override
    @Transactional
    public void updateStaffSend(StaffSend staffSend) {
        staffSend.setModifyTime(LocalDateTime.now());
        String leaveDate = staffSend.getLeaveDate();
        if (staffSend.getIsLeave().equals("0")) {
            staffSend.setLeaveDate(null);
        }
        this.baseMapper.update(
                staffSend,
                Wrappers.<StaffSend>lambdaUpdate() // 这里set是因为如果用默认的update，null值是不会更新的，set的话，不管你是什么都会更新
                        .set(StaffSend::getLeaveDate, staffSend.getLeaveDate())
                        .eq(StaffSend::getStaffId, staffSend.getStaffId()));
        if (staffSend.getSortNum() != null || staffSend.getLeaveDate() != null || staffSend.getLeaveDate() != leaveDate) {
            // 更新序号
            this.baseMapper.updateStaffSendSortNum();
            this.baseMapper.updateStaffSendLeaveSortNum();
        }
    }

    @Override
    @Transactional
    public void deleteStaffSend(String[] staffSendIds, Integer deleted) {
        if (deleted == 0) {
            List<String> list = Arrays.asList(staffSendIds);
            this.baseMapper.deleteBatchIds(list);
        } else if (deleted == 1) {
            this.baseMapper.deleteStaffSendTrue(StringUtils.join(staffSendIds, ","));
            // 根据编外人员id查找对应照片id
            List<String> fileIdList = this.sendFileService.findFileIdsBySendIds(staffSendIds);
            if (!fileIdList.isEmpty()) {
                String[] fileIds = fileIdList.toArray(new String[0]);
                this.deleteStaffSendsFile(fileIds);
            }
        }
        // 更新序号
        this.baseMapper.updateStaffSendSortNum();
        this.baseMapper.updateStaffSendLeaveSortNum();
    }

    @Override
    public List<String> getBankCardAttribution() {
        return this.baseMapper.getBankCardAttribution();
    }

    @Override
    public List<String> getCompany() {
        return this.baseMapper.getCompany();
    }

    @Override
    public StaffSend getStaffIdByIdNum(String idNum) {
        return baseMapper.getStaffIdByIdNum(idNum);
    }

    @Override
    @Transactional
    public void restoreStaffSend(String staffSendIds) {
        this.baseMapper.restoreStaffSend(staffSendIds);
        // 更新序号
        this.baseMapper.updateStaffSendSortNum();
        this.baseMapper.updateStaffSendLeaveSortNum();
    }

    /*@Override
    public void togetherRestoreStaffSend(String staffSendIds) {
        this.restoreStaffSend(staffSendIds);
    }*/

    @Override
    @Transactional
    public void deleteStaffSendsFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.sendFileService.deleteSendFilesByFileId(fileIds);
    }

    @Override
    public FebsResponse uploadStaffSendPhoto(MultipartFile file, String id) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.sendFileService.createSendFile(new SendFile(Long.valueOf(id), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }
}
