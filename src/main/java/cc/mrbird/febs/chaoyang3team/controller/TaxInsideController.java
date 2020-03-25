package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.TaxInside;
import cc.mrbird.febs.chaoyang3team.domain.TaxInsideImport;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
import cc.mrbird.febs.chaoyang3team.service.TaxInsideService;
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
@RequestMapping("taxInside")
public class TaxInsideController extends BaseController {

    private String message;

    @Autowired
    private TaxInsideService taxInsideService;
    @Autowired
    private StaffInsideService staffInsideService;

    @GetMapping("oneInfo")
    @RequiresPermissions("taxInside:view")
    public List<TaxInside> getTaxInsideInfoList(TaxInside taxInside) {
        return this.taxInsideService.getTaxInsideInfoList(taxInside);
    }

    @GetMapping
    @RequiresPermissions("taxInside:view")
    public Map<String, Object> TaxInsideList(QueryRequest request, TaxInside taxInside) {
        return getDataTable(this.taxInsideService.findTaxInsideDetail(request, taxInside));
    }

    @Log("新增编内个税信息")
    @PostMapping
    @RequiresPermissions("taxInside:add")
    public void addTaxInside(@Valid TaxInside taxInside) throws FebsException {
        try {
            this.taxInsideService.createTaxInside(taxInside);
        } catch (Exception e) {
            message = "新增编内个税信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编内个税信息")
    @PutMapping
    @RequiresPermissions("taxInside:update")
    public void updateTaxInside(@Valid TaxInside taxInside) throws FebsException {
        try {
            this.taxInsideService.updateTaxInside(taxInside);
        } catch (Exception e) {
            message = "修改编内个税信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编内个税信息")
    @DeleteMapping("/{taxInsideIds}")
    @RequiresPermissions("taxInside:delete")
    public void deleteTaxInsides(@NotBlank(message = "{required}") @PathVariable String taxInsideIds) throws FebsException {
        try {
            String[] ids = taxInsideIds.split(StringPool.COMMA);
            this.taxInsideService.deleteTaxInside(ids);
        } catch (Exception e) {
            message = "删除编内个税信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("taxInside:export")
    public void export(QueryRequest request, TaxInside taxInside, HttpServletResponse response) throws FebsException {
        try {
            IPage<TaxInside> taxInsideDetail = this.taxInsideService.findTaxInsideDetail(request, taxInside);
            if (taxInsideDetail != null) {
                List<TaxInside> taxInsides = taxInsideDetail.getRecords();
                ExcelKit.$Export(TaxInside.class, response).downXlsx(taxInsides, false);
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
        List<TaxInsideImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            TaxInsideImport taxInside = new TaxInsideImport();
            taxInside.setName("此项不用必须填写，只是作为参照使用");
            taxInside.setIdCardType("居民身份证");
            taxInside.setStaffIdCard("证照号码" + (i + 1));
            taxInside.setCurrentIncome("0");
            taxInside.setCurrentTaxFreeIncome("0");
            taxInside.setBasicPensionIp("0");
            taxInside.setBasicMedicalIp("0");
            taxInside.setUnemploymentIp("0");
            taxInside.setHousingFund("0");
            taxInside.setCumulativeChildE("0");
            taxInside.setCumulativeContinuingE("0");
            taxInside.setCumulativeHomeLoanInterest("0");
            taxInside.setCumulativeHousingRent("0");
            taxInside.setCumulativeElderlySupport("0");
            taxInside.setCorporateAnnuity("0");
            taxInside.setCommercialHealthInsurance("0");
            taxInside.setTaxExtensionPensionInsurance("0");
            taxInside.setOther("0");
            taxInside.setAllowanceForDeduction("0");
            taxInside.setTaxDeduction("0");
            list.add(taxInside);
        });
        // 构建模板
        ExcelKit.$Export(TaxInsideImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入编内个税信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("taxInside:add")
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
            final List<TaxInside> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            String dateArr[] = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            BigDecimal zero = BigDecimal.valueOf(0);
            ExcelKit.$Import(TaxInsideImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<TaxInsideImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, TaxInsideImport entity) {
                    // 数据校验成功时，加入集合
                    StaffInside staffInside = staffInsideService.getStaffIdByIdNum(entity.getStaffIdCard().trim());
                    if (staffInside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getStaffIdCard().trim(),
                                "证照号码",
                                "查询不到 [" + entity.getName() + "] 这个编内人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        TaxInside taxInside = new TaxInside();
                        taxInside.setStaffId(staffInside.getStaffId());
                        taxInside.setStaffName(staffInside.getName());
                        taxInside.setIdCardType(entity.getIdCardType());
                        taxInside.setStaffIdCard(entity.getStaffIdCard().trim());
                        taxInside.setCurrentIncome(entity.getCurrentIncome().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCurrentIncome()));
                        taxInside.setCurrentTaxFreeIncome(entity.getCurrentTaxFreeIncome().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCurrentTaxFreeIncome()));
                        taxInside.setBasicPensionIp(entity.getBasicPensionIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getBasicPensionIp()));
                        taxInside.setBasicMedicalIp(entity.getBasicMedicalIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getBasicMedicalIp()));
                        taxInside.setUnemploymentIp(entity.getUnemploymentIp().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getUnemploymentIp()));
                        taxInside.setHousingFund(entity.getHousingFund().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getHousingFund()));
                        taxInside.setCumulativeChildE(entity.getCumulativeChildE().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeChildE()));
                        taxInside.setCumulativeContinuingE(entity.getCumulativeContinuingE().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeContinuingE()));
                        taxInside.setCumulativeHomeLoanInterest(entity.getCumulativeHomeLoanInterest().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeHomeLoanInterest()));
                        taxInside.setCumulativeHousingRent(entity.getCumulativeHousingRent().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeHousingRent()));
                        taxInside.setCumulativeElderlySupport(entity.getCumulativeElderlySupport().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCumulativeElderlySupport()));
                        taxInside.setCorporateAnnuity(entity.getCorporateAnnuity().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCorporateAnnuity()));
                        taxInside.setCommercialHealthInsurance(entity.getCommercialHealthInsurance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getCommercialHealthInsurance()));
                        taxInside.setTaxExtensionPensionInsurance(entity.getTaxExtensionPensionInsurance().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getTaxExtensionPensionInsurance()));
                        taxInside.setOther(entity.getOther().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getOther()));
                        taxInside.setAllowanceForDeduction(entity.getAllowanceForDeduction().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getAllowanceForDeduction()));
                        taxInside.setTaxDeduction(entity.getTaxDeduction().equals("$EMPTY_CELL$") ? zero : new BigDecimal(entity.getTaxDeduction()));
                        taxInside.setRemark(entity.getRemark().equals("$EMPTY_CELL$") ? "" : entity.getRemark());
                        taxInside.setCreateTime(now);
                        taxInside.setYear(dateArr[0]);
                        taxInside.setMonth(dateArr[1]);
                        taxInside.setFinalWage(taxInside.getCurrentIncome()
                                .subtract(taxInside.getCurrentTaxFreeIncome())
                                .subtract(taxInside.getBasicPensionIp())
                                .subtract(taxInside.getBasicMedicalIp())
                                .subtract(taxInside.getUnemploymentIp())
                                .subtract(taxInside.getHousingFund())
                                .subtract(taxInside.getCumulativeChildE())
                                .subtract(taxInside.getCumulativeContinuingE())
                                .subtract(taxInside.getCumulativeHomeLoanInterest())
                                .subtract(taxInside.getCumulativeHousingRent())
                                .subtract(taxInside.getCumulativeElderlySupport())
                                .subtract(taxInside.getCorporateAnnuity())
                                .subtract(taxInside.getCommercialHealthInsurance())
                                .subtract(taxInside.getTaxExtensionPensionInsurance())
                                .subtract(taxInside.getOther())
                                .subtract(taxInside.getAllowanceForDeduction())
                                .subtract(taxInside.getTaxDeduction())
                                .subtract(BigDecimal.valueOf(5000))
                        );
                        data.add(taxInside);
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
                this.taxInsideService.batchInsertTaxInside(data);
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
