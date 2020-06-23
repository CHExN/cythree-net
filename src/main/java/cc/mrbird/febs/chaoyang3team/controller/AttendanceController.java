package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Attendance;
import cc.mrbird.febs.chaoyang3team.domain.AttendanceImport;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.AttendanceService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("attendance")
public class AttendanceController extends BaseController {

    private String message;

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private StaffOutsideService staffOutsideService;
    @Autowired
    private UserManager userManager;

    @GetMapping
    @RequiresPermissions("attendance:view")
    public Map<String, Object> attendanceList(QueryRequest request, Attendance attendance) {
        return getDataTable(this.attendanceService.findAttendanceDetail(request, attendance));
    }

    @Log("新增考勤信息")
    @PostMapping
    @RequiresPermissions("attendance:add")
    public void addAttendance(@Valid Attendance attendance, ServletRequest request) throws FebsException {
        try {
            this.attendanceService.createAttendance(attendance, request);
        } catch (Exception e) {
            message = "新增考勤信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改考勤信息")
    @PutMapping
    @RequiresPermissions("attendance:update")
    public void updateAttendance(@Valid Attendance attendance) throws FebsException {
        try {
            this.attendanceService.updateAttendance(attendance);
        } catch (Exception e) {
            message = "修改考勤信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除考勤信息")
    @DeleteMapping("/{attendanceIds}")
    @RequiresPermissions("attendance:delete")
    public void deleteAttendances(@NotBlank(message = "{required}") @PathVariable String attendanceIds) throws FebsException {
        try {
            String[] ids = attendanceIds.split(StringPool.COMMA);
            this.attendanceService.deleteAttendance(ids);
        } catch (Exception e) {
            message = "删除考勤信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("考勤汇总报表")
    @GetMapping("report")
    @RequiresPermissions("attendance:export")
    public List<Attendance> AttendanceReport(String date) {
        return this.attendanceService.getAttendanceReport(date);
    }

    /**
     * 生成 Excel导入模板
     */
    @PostMapping("template")
    public void generateImportTemplate(HttpServletResponse response) {
        // 构建数据
        List<AttendanceImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            AttendanceImport attendance = new AttendanceImport();
            attendance.setStaffIdCard("身份证号码" + (i + 1));
            attendance.setDayWork(26);
            attendance.setNightWork(0);
            attendance.setDoublePlus(0);
            attendance.setHolidayPlus(0);
            attendance.setHour15(0);
            attendance.setHour20(0);
            attendance.setPublicHoliday(4);
            attendance.setWorkingLeave(0);
            attendance.setSickLeave(0);
            attendance.setThingLeave(0);
            attendance.setMarriageLeave(0);
            attendance.setFuneralLeave(0);
            attendance.setWorkInjury(0);
            attendance.setLateAndLeaveEarly(0);
            attendance.setUsuallyDuty(0);
            attendance.setWeekendDuty(0);
            attendance.setHolidayWatch(0);
            attendance.setDeduction(0);
            attendance.setTotalAttendanceDays(30);
            list.add(attendance);
        });
        // 构建模板
        ExcelKit.$Export(AttendanceImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入考勤信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("attendance:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, String startDate, String endDate, ServletRequest request) throws FebsException {
        try {
            if (file.isEmpty()) {
                throw new FebsException("导入数据为空");
            }
            String filename = file.getOriginalFilename();
            if (!StringUtils.endsWith(filename, ".xlsx")) {
                throw new FebsException("只支持.xlsx类型文件导入");
            }
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<Attendance> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);
            final Long deptId = user.getDeptId();
            ExcelKit.$Import(AttendanceImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<AttendanceImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, AttendanceImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getStaffIdCard());
                    if (staffOutside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(0, entity.getStaffIdCard(), "编外人员身份证号", "身份证号不存在"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        Attendance attendance = new Attendance();
                        attendance.setStaffId(staffOutside.getStaffId());
                        attendance.setDeptId(deptId);
                        try {
                            attendance.setStartDate(fmt.parse(startDate));
                            attendance.setEndDate(fmt.parse(endDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        attendance.setDayWork(entity.getDayWork());
                        attendance.setNightWork(entity.getNightWork());
                        attendance.setDoublePlus(entity.getDoublePlus());
                        attendance.setHolidayPlus(entity.getHolidayPlus());
                        attendance.setHour15(entity.getHour15());
                        attendance.setHour20(entity.getHour20());
                        attendance.setPublicHoliday(entity.getPublicHoliday());
                        attendance.setWorkingLeave(entity.getWorkingLeave());
                        attendance.setSickLeave(entity.getSickLeave());
                        attendance.setThingLeave(entity.getThingLeave());
                        attendance.setMarriageLeave(entity.getMarriageLeave());
                        attendance.setFuneralLeave(entity.getFuneralLeave());
                        attendance.setWorkInjury(entity.getWorkInjury());
                        attendance.setLateAndLeaveEarly(entity.getLateAndLeaveEarly());
                        attendance.setUsuallyDuty(entity.getUsuallyDuty());
                        attendance.setWeekendDuty(entity.getWeekendDuty());
                        attendance.setHolidayWatch(entity.getHolidayWatch());
                        attendance.setDeduction(entity.getDeduction());
                        attendance.setTotalAttendanceDays(entity.getTotalAttendanceDays());
                        attendance.setRemark(entity.getRemark().equals("$EMPTY_CELL$")?"":entity.getRemark());
                        attendance.setStaffName(staffOutside.getName());
                        data.add(attendance);
                    }
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.attendanceService.batchInsertAttendance(data);
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(message);
        }
    }

}
