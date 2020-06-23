package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.domain.TaxOutside;
import cc.mrbird.febs.chaoyang3team.domain.TaxOutsideImport;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
import cc.mrbird.febs.chaoyang3team.service.TaxOutsideService;
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
@RequestMapping("taxOutside")
public class TaxOutsideController extends BaseController {

    private String message;

    @Autowired
    private TaxOutsideService taxOutsideService;
    @Autowired
    private StaffOutsideService staffOutsideService;

    @GetMapping("oneInfo")
    @RequiresPermissions("taxOutside:view")
    public List<TaxOutside> getTaxOutsideInfoList(TaxOutside taxOutside) {
        return this.taxOutsideService.getTaxOutsideInfoList(taxOutside);
    }

    @GetMapping
    @RequiresPermissions("taxOutside:view")
    public Map<String, Object> taxOutsideList(QueryRequest request, TaxOutside taxOutside) {
        return getDataTable(this.taxOutsideService.findTaxOutsideDetail(request, taxOutside));
    }

    @Log("新增编外个税信息")
    @PostMapping
    @RequiresPermissions("taxOutside:add")
    public void addTaxOutside(@Valid TaxOutside taxOutside) throws FebsException {
        try {
            this.taxOutsideService.createTaxOutside(taxOutside);
        } catch (Exception e) {
            message = "新增编外个税信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外个税信息")
    @PutMapping
    @RequiresPermissions("taxOutside:update")
    public void updateTaxOutside(@Valid TaxOutside taxOutside) throws FebsException {
        try {
            this.taxOutsideService.updateTaxOutside(taxOutside);
        } catch (Exception e) {
            message = "修改编外个税信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外个税信息")
    @DeleteMapping("/{taxOutsideIds}")
    @RequiresPermissions("taxOutside:delete")
    public void deleteTaxOutsides(@NotBlank(message = "{required}") @PathVariable String taxOutsideIds) throws FebsException {
        try {
            String[] ids = taxOutsideIds.split(StringPool.COMMA);
            this.taxOutsideService.deleteTaxOutside(ids);
        } catch (Exception e) {
            message = "删除编外个税信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("taxOutside:export")
    public void export(QueryRequest request, TaxOutside taxOutside, HttpServletResponse response) throws FebsException {
        try {
            IPage<TaxOutside> taxOutsideDetail = this.taxOutsideService.findTaxOutsideDetail(request, taxOutside);
            if (taxOutsideDetail != null) {
                List<TaxOutside> taxOutsides = taxOutsideDetail.getRecords();
                ExcelKit.$Export(TaxOutside.class, response).downXlsx(taxOutsides, false);
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
        List<TaxOutsideImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            TaxOutsideImport taxOutside = new TaxOutsideImport();
            taxOutside.setName("此项不用必须填写，只是作为参照使用");
            taxOutside.setIdCardType("居民身份证");
            taxOutside.setStaffIdCard("证照号码" + (i + 1));
            taxOutside.setCurrentIncome("0");
            taxOutside.setCurrentTaxFreeIncome("0");
            taxOutside.setBasicPensionIp("0");
            taxOutside.setBasicMedicalIp("0");
            taxOutside.setUnemploymentIp("0");
            taxOutside.setHousingFund("0");
            taxOutside.setCumulativeChildE("0");
            taxOutside.setCumulativeContinuingE("0");
            taxOutside.setCumulativeHomeLoanInterest("0");
            taxOutside.setCumulativeHousingRent("0");
            taxOutside.setCumulativeElderlySupport("0");
            taxOutside.setCorporateAnnuity("0");
            taxOutside.setCommercialHealthInsurance("0");
            taxOutside.setTaxExtensionPensionInsurance("0");
            taxOutside.setOther("0");
            taxOutside.setAllowanceForDeduction("0");
            taxOutside.setTaxDeduction("0");
            list.add(taxOutside);
        });
        // 构建模板
        ExcelKit.$Export(TaxOutsideImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入编外个税信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("taxOutside:add")
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
            final List<TaxOutside> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            String dateArr[] = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            BigDecimal zero = BigDecimal.valueOf(0);
            ExcelKit.$Import(TaxOutsideImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<TaxOutsideImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, TaxOutsideImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getStaffIdCard().trim());
                    if (staffOutside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getStaffIdCard().trim(),
                                "证照号码",
                                "查询不到 [" + entity.getName() + "] 这个编外人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        TaxOutside taxOutside = new TaxOutside();
                        taxOutside.setStaffId(staffOutside.getStaffId());
                        taxOutside.setStaffName(staffOutside.getName());
                        taxOutside.setIdCardType(entity.getIdCardType());
                        taxOutside.setStaffIdCard(entity.getStaffIdCard().trim());
                        taxOutside.setCurrentIncome(entity.getCurrentIncome().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCurrentIncome()));
                        taxOutside.setCurrentTaxFreeIncome(entity.getCurrentTaxFreeIncome().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCurrentTaxFreeIncome()));
                        taxOutside.setBasicPensionIp(entity.getBasicPensionIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getBasicPensionIp()));
                        taxOutside.setBasicMedicalIp(entity.getBasicMedicalIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getBasicMedicalIp()));
                        taxOutside.setUnemploymentIp(entity.getUnemploymentIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getUnemploymentIp()));
                        taxOutside.setHousingFund(entity.getHousingFund().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getHousingFund()));
                        taxOutside.setCumulativeChildE(entity.getCumulativeChildE().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeChildE()));
                        taxOutside.setCumulativeContinuingE(entity.getCumulativeContinuingE().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeContinuingE()));
                        taxOutside.setCumulativeHomeLoanInterest(entity.getCumulativeHomeLoanInterest().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeHomeLoanInterest()));
                        taxOutside.setCumulativeHousingRent(entity.getCumulativeHousingRent().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeHousingRent()));
                        taxOutside.setCumulativeElderlySupport(entity.getCumulativeElderlySupport().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeElderlySupport()));
                        taxOutside.setCorporateAnnuity(entity.getCorporateAnnuity().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCorporateAnnuity()));
                        taxOutside.setCommercialHealthInsurance(entity.getCommercialHealthInsurance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCommercialHealthInsurance()));
                        taxOutside.setTaxExtensionPensionInsurance(entity.getTaxExtensionPensionInsurance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getTaxExtensionPensionInsurance()));
                        taxOutside.setOther(entity.getOther().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getOther()));
                        taxOutside.setAllowanceForDeduction(entity.getAllowanceForDeduction().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getAllowanceForDeduction()));
                        taxOutside.setTaxDeduction(entity.getTaxDeduction().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getTaxDeduction()));
                        taxOutside.setRemark(entity.getRemark().equals("$EMPTY_CELL$") ? "" : entity.getRemark());
                        taxOutside.setCreateTime(now);
                        taxOutside.setYear(dateArr[0]);
                        taxOutside.setMonth(dateArr[1]);
                        taxOutside.setFinalWage(taxOutside.getCurrentIncome()
                                .subtract(taxOutside.getCurrentTaxFreeIncome())
                                .subtract(taxOutside.getBasicPensionIp())
                                .subtract(taxOutside.getBasicMedicalIp())
                                .subtract(taxOutside.getUnemploymentIp())
                                .subtract(taxOutside.getHousingFund())
                                .subtract(taxOutside.getCumulativeChildE())
                                .subtract(taxOutside.getCumulativeContinuingE())
                                .subtract(taxOutside.getCumulativeHomeLoanInterest())
                                .subtract(taxOutside.getCumulativeHousingRent())
                                .subtract(taxOutside.getCumulativeElderlySupport())
                                .subtract(taxOutside.getCorporateAnnuity())
                                .subtract(taxOutside.getCommercialHealthInsurance())
                                .subtract(taxOutside.getTaxExtensionPensionInsurance())
                                .subtract(taxOutside.getOther())
                                .subtract(taxOutside.getAllowanceForDeduction())
                                .subtract(taxOutside.getTaxDeduction())
                                .subtract(BigDecimal.valueOf(5000))
                        );
                        data.add(taxOutside);
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
                this.taxOutsideService.batchInsertTaxOutside(data);
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
