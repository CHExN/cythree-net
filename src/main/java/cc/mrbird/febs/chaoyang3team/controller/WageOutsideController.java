package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.domain.WageOutside;
import cc.mrbird.febs.chaoyang3team.domain.WageOutsideImport;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.chaoyang3team.service.WageOutsideService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@RequestMapping("wageOutside")
public class WageOutsideController extends BaseController {

    private String message;

    @Autowired
    private WageOutsideService wageOutsideService;
    @Autowired
    private StaffOutsideService staffOutsideService;

    @GetMapping("oneInfo")
    @RequiresPermissions("wageOutside:view")
    public List<WageOutside> getWageOutsideInfoList(WageOutside wageOutside) {
        return this.wageOutsideService.getWageOutsideInfoList(wageOutside);
    }

    @GetMapping
    @RequiresPermissions("wageOutside:view")
    public Map<String, Object> wageOutsideList(QueryRequest request, WageOutside wageOutside) {
        return getDataTable(this.wageOutsideService.findWageOutsideDetail(request, wageOutside));
    }

    @Log("新增编外工资信息")
    @PostMapping
    @RequiresPermissions("wageOutside:add")
    public void addWageOutside(@Valid WageOutside wageOutside) throws FebsException {
        try {
            this.wageOutsideService.createWageOutside(wageOutside);
        } catch (Exception e) {
            message = "新增编外工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外工资信息")
    @PutMapping
    @RequiresPermissions("wageOutside:update")
    public void updateWageOutside(@Valid WageOutside wageOutside) throws FebsException {
        try {
            this.wageOutsideService.updateWageOutside(wageOutside);
        } catch (Exception e) {
            message = "修改编外工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外工资信息")
    @DeleteMapping("/{wageOutsideIds}")
    @RequiresPermissions("wageOutside:delete")
    public void deleteWageOutsides(@NotBlank(message = "{required}") @PathVariable String wageOutsideIds) throws FebsException {
        try {
            String[] ids = wageOutsideIds.split(StringPool.COMMA);
            this.wageOutsideService.deleteWageOutside(ids);
        } catch (Exception e) {
            message = "删除编外工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除本月全部编外工资信息")
    @DeleteMapping("deleteAll")
    @RequiresPermissions("wageOutside:delete")
    public void deleteAllWageOutsides(String year, String month) throws FebsException {
        try {
            this.wageOutsideService.deleteAllWageOutside(year, month);
        } catch (Exception e) {
            message = "删除本月全部编外工资信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("wageOutside:export")
    public void export(QueryRequest request, WageOutside wageOutside, HttpServletResponse response) throws FebsException {
        try {

            IPage<WageOutside> wageOutsideDetail = this.wageOutsideService.findWageOutsideDetail(request, wageOutside);
            if (wageOutsideDetail != null) {
                List<WageOutside> wageOutsides = wageOutsideDetail.getRecords();
                ExcelKit.$Export(WageOutside.class, response).downXlsx(wageOutsides, false);
            }
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 生成 Excel导入模板
     */
    @PostMapping("template")
    public void generateImportTemplate(HttpServletResponse response) {
        // 构建数据
        List<WageOutsideImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            WageOutsideImport wageOutside = new WageOutsideImport();
            wageOutside.setStaffName("此项不用必须填写，只是作为参照使用");
            wageOutside.setStaffIdCard("证照号码" + (i + 1));
            wageOutside.setCurrentIncome("0");
            wageOutside.setPostAllowance("0");
            wageOutside.setSanitationAllowance("0");
            wageOutside.setDangerousSubsidy("0");
            wageOutside.setPerformanceBonus("0");
            wageOutside.setOvertimePay("0");
            wageOutside.setHolidayCosts("0");
            wageOutside.setPayable("不用填写，自动计算");
            wageOutside.setBasicPensionIp("0");
            wageOutside.setUnemploymentIp("0");
            wageOutside.setBasicMedicalIp("0");
            wageOutside.setTaxDeduction("0");
            wageOutside.setMembership("0");
            wageOutside.setHousingFund("0");
            wageOutside.setSickLeave("0");
            wageOutside.setRealWage("不用填写，自动计算");
            wageOutside.setEmptyColumn01("0");
            wageOutside.setEmptyColumn02("0");
            wageOutside.setEmptyColumn03("0");
            wageOutside.setEmptyColumn04("0");
            wageOutside.setEmptyColumn05("0");
            wageOutside.setEmptyColumn06("0");
            wageOutside.setEmptyColumn07("0");
            wageOutside.setEmptyColumn08("0");
            wageOutside.setEmptyColumn09("0");
            wageOutside.setEmptyColumn10("0");
            list.add(wageOutside);
        });
        // 构建模板
        ExcelKit.$Export(WageOutsideImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入编外工资信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("wageOutside:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, String date) throws FebsException {
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
            final List<WageOutside> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            String[] dateArr = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            BigDecimal zero = BigDecimal.valueOf(0);
            ExcelKit.$Import(WageOutsideImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<WageOutsideImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, WageOutsideImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getStaffIdCard().trim());
                    if (staffOutside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getStaffIdCard().trim(),
                                "证照号码",
                                "查询不到 [" + entity.getStaffName() + "] 这个编外人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        WageOutside wageOutside = new WageOutside();
                        wageOutside.setStaffId(staffOutside.getStaffId());
                        wageOutside.setStaffName(staffOutside.getName());
                        wageOutside.setStaffIdCard(entity.getStaffIdCard().trim());

                        wageOutside.setCurrentIncome(entity.getCurrentIncome().equals("$EMPTY_CELL$") || entity.getCurrentIncome().equals("") ? zero : new BigDecimal(entity.getCurrentIncome()));
                        wageOutside.setPostAllowance(entity.getPostAllowance().equals("$EMPTY_CELL$") || entity.getPostAllowance().equals("") ? zero : new BigDecimal(entity.getPostAllowance()));
                        wageOutside.setSanitationAllowance(entity.getSanitationAllowance().equals("$EMPTY_CELL$") || entity.getSanitationAllowance().equals("") ? zero : new BigDecimal(entity.getSanitationAllowance()));
                        wageOutside.setDangerousSubsidy(entity.getDangerousSubsidy().equals("$EMPTY_CELL$") || entity.getDangerousSubsidy().equals("") ? zero : new BigDecimal(entity.getDangerousSubsidy()));
                        wageOutside.setPerformanceBonus(entity.getPerformanceBonus().equals("$EMPTY_CELL$") || entity.getPerformanceBonus().equals("") ? zero : new BigDecimal(entity.getPerformanceBonus()));
                        wageOutside.setOvertimePay(entity.getOvertimePay().equals("$EMPTY_CELL$") || entity.getOvertimePay().equals("") ? zero : new BigDecimal(entity.getOvertimePay()));
                        wageOutside.setHolidayCosts(entity.getHolidayCosts().equals("$EMPTY_CELL$") || entity.getHolidayCosts().equals("") ? zero : new BigDecimal(entity.getHolidayCosts()));
                        wageOutside.setEmptyColumn01(entity.getEmptyColumn01().equals("$EMPTY_CELL$") || entity.getEmptyColumn01().equals("") ? zero : new BigDecimal(entity.getEmptyColumn01()));
                        wageOutside.setEmptyColumn02(entity.getEmptyColumn02().equals("$EMPTY_CELL$") || entity.getEmptyColumn02().equals("") ? zero : new BigDecimal(entity.getEmptyColumn02()));
                        wageOutside.setEmptyColumn03(entity.getEmptyColumn03().equals("$EMPTY_CELL$") || entity.getEmptyColumn03().equals("") ? zero : new BigDecimal(entity.getEmptyColumn03()));
                        wageOutside.setEmptyColumn04(entity.getEmptyColumn04().equals("$EMPTY_CELL$") || entity.getEmptyColumn04().equals("") ? zero : new BigDecimal(entity.getEmptyColumn04()));
                        wageOutside.setEmptyColumn05(entity.getEmptyColumn05().equals("$EMPTY_CELL$") || entity.getEmptyColumn05().equals("") ? zero : new BigDecimal(entity.getEmptyColumn05()));
                        // 应发工资
                        wageOutside.setPayable(wageOutside.getCurrentIncome()
                                .add(wageOutside.getPostAllowance())
                                .add(wageOutside.getSanitationAllowance())
                                .add(wageOutside.getDangerousSubsidy())
                                .add(wageOutside.getPerformanceBonus())
                                .add(wageOutside.getOvertimePay())
                                .add(wageOutside.getHolidayCosts())
                                .add(wageOutside.getEmptyColumn01())
                                .add(wageOutside.getEmptyColumn02())
                                .add(wageOutside.getEmptyColumn03())
                                .add(wageOutside.getEmptyColumn04())
                                .add(wageOutside.getEmptyColumn05())
                        );
                        System.out.println("应发工资");
                        System.out.println(wageOutside.getPayable());

                        wageOutside.setBasicPensionIp(entity.getBasicPensionIp().equals("$EMPTY_CELL$") || entity.getBasicPensionIp().equals("") ? zero : new BigDecimal(entity.getBasicPensionIp()));
                        wageOutside.setUnemploymentIp(entity.getUnemploymentIp().equals("$EMPTY_CELL$") || entity.getUnemploymentIp().equals("") ? zero : new BigDecimal(entity.getUnemploymentIp()));
                        wageOutside.setBasicMedicalIp(entity.getBasicMedicalIp().equals("$EMPTY_CELL$") || entity.getBasicMedicalIp().equals("") ? zero : new BigDecimal(entity.getBasicMedicalIp()));
                        wageOutside.setTaxDeduction(entity.getTaxDeduction().equals("$EMPTY_CELL$") || entity.getTaxDeduction().equals("") ? zero : new BigDecimal(entity.getTaxDeduction()));
                        wageOutside.setMembership(entity.getMembership().equals("$EMPTY_CELL$") || entity.getMembership().equals("") ? zero : new BigDecimal(entity.getMembership()));
                        wageOutside.setHousingFund(entity.getHousingFund().equals("$EMPTY_CELL$") || entity.getHousingFund().equals("") ? zero : new BigDecimal(entity.getHousingFund()));
                        wageOutside.setSickLeave(entity.getSickLeave().equals("$EMPTY_CELL$") || entity.getSickLeave().equals("") ? zero : new BigDecimal(entity.getSickLeave()));
                        wageOutside.setEmptyColumn06(entity.getEmptyColumn06().equals("$EMPTY_CELL$") || entity.getEmptyColumn06().equals("") ? zero : new BigDecimal(entity.getEmptyColumn06()));
                        wageOutside.setEmptyColumn07(entity.getEmptyColumn07().equals("$EMPTY_CELL$") || entity.getEmptyColumn07().equals("") ? zero : new BigDecimal(entity.getEmptyColumn07()));
                        wageOutside.setEmptyColumn08(entity.getEmptyColumn08().equals("$EMPTY_CELL$") || entity.getEmptyColumn08().equals("") ? zero : new BigDecimal(entity.getEmptyColumn08()));
                        wageOutside.setEmptyColumn09(entity.getEmptyColumn09().equals("$EMPTY_CELL$") || entity.getEmptyColumn09().equals("") ? zero : new BigDecimal(entity.getEmptyColumn09()));
                        wageOutside.setEmptyColumn10(entity.getEmptyColumn10().equals("$EMPTY_CELL$") || entity.getEmptyColumn10().equals("") ? zero : new BigDecimal(entity.getEmptyColumn10()));
                        // 实发工资
                        wageOutside.setRealWage(wageOutside.getPayable()
                                .subtract(wageOutside.getBasicPensionIp())
                                .subtract(wageOutside.getUnemploymentIp())
                                .subtract(wageOutside.getBasicMedicalIp())
                                .subtract(wageOutside.getTaxDeduction())
                                .subtract(wageOutside.getMembership())
                                .subtract(wageOutside.getHousingFund())
                                .subtract(wageOutside.getSickLeave())
                                .subtract(wageOutside.getEmptyColumn06())
                                .subtract(wageOutside.getEmptyColumn07())
                                .subtract(wageOutside.getEmptyColumn08())
                                .subtract(wageOutside.getEmptyColumn09())
                                .subtract(wageOutside.getEmptyColumn10())
                        );
                        System.out.println("实发工资");
                        System.out.println(wageOutside.getRealWage());
                        System.out.println(wageOutside);

                        wageOutside.setCreateTime(now.toString());
                        wageOutside.setYear(dateArr[0]);
                        wageOutside.setMonth(dateArr[1]);
                        data.add(wageOutside);
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
                this.wageOutsideService.batchInsertWageOutside(data);
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
