package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.AttendanceMapper;
import cc.mrbird.febs.chaoyang3team.domain.Attendance;
import cc.mrbird.febs.chaoyang3team.domain.AttendanceFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.service.AttendanceFileService;
import cc.mrbird.febs.chaoyang3team.service.AttendanceService;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author CHExN
 */
@Slf4j
@Service("attendanceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private FileService fileService;
    @Autowired
    private AttendanceFileService attendanceFileService;

    @Override
    public IPage<Attendance> findAttendanceDetail(QueryRequest request, Attendance attendance, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);
            // 如果没有查看全部的权限，就查看自己部门的信息
            if (!userPermissions.contains("attendance:viewAll")) {
                attendance.setDeptId(user.getDeptId());
            }
            Page<Attendance> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findAttendanceDetail(page, attendance);
        } catch (Exception e) {
            log.error("查询考勤信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createAttendance(Attendance attendance, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        attendance.setDeptId(user.getDeptId());
        this.save(attendance);
    }

    @Override
    @Transactional
    public void batchInsertAttendance(List<Attendance> attendances) {
        this.saveBatch(attendances);
    }

    @Override
    @Transactional
    public void updateAttendance(Attendance attendance) {
        this.baseMapper.updateById(attendance);
    }

    @Override
    @Transactional
    public void deleteAttendance(String[] attendanceIds) {
        List<String> ids = Arrays.asList(attendanceIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Attendance> getAttendanceReport(String date) {
        return this.baseMapper.getAttendanceReport(date);
    }

    @Override
    public void deleteAttendanceFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.attendanceFileService.deleteAttendanceFilesByFileId(fileIds);
    }

    @Override
    public FebsResponse uploadAttendanceImage(MultipartFile file, String year, String month) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.attendanceFileService.createAttendanceFile(new AttendanceFile(year, month, fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }
}
