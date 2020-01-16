package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Insurance;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceImport;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.chaoyang3team.service.InsuranceInsideService;
import cc.mrbird.febs.chaoyang3team.service.InsuranceOutsideService;
import cc.mrbird.febs.chaoyang3team.service.InsuranceService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
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
@RequestMapping("insurance")
public class InsuranceController extends BaseController {
    private String message;

    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceInsideService insuranceInsideService;
    @Autowired
    private InsuranceOutsideService insuranceOutsideService;

    @GetMapping("insuranceSummary")
    @RequiresPermissions("insurance:export")
    public List<Insurance> insuranceSummary(Insurance insurance) {
        return this.insuranceService.findInsuranceSummary(insurance);
    }

    @GetMapping
    @RequiresPermissions("insurance:view")
    public Map<String, Object> insuranceList(QueryRequest request, Insurance insurance) {
        return getDataTable(this.insuranceService.findInsuranceDetail(request, insurance));
    }

    @Log("新增保险信息")
    @PostMapping
    @RequiresPermissions("insurance:add")
    public void addInsurance(@Valid Insurance insurance) throws FebsException {
        try {
            this.insuranceService.createInsurance(insurance);
        } catch (Exception e) {
            message = "新增保险信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改保险信息")
    @PutMapping
    @RequiresPermissions("insurance:update")
    public void updateInsurance(@Valid Insurance insurance) throws FebsException {
        try {
            this.insuranceService.updateInsurance(insurance);
        } catch (Exception e) {
            message = "修改保险信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除保险信息")
    @DeleteMapping("/{insuranceIds}")
    @RequiresPermissions("insurance:delete")
    public void deleteInsurances(@NotBlank(message = "{required}") @PathVariable String insuranceIds) throws FebsException {
        try {
            String[] ids = insuranceIds.split(StringPool.COMMA);
            this.insuranceService.deleteInsurance(ids);
        } catch (Exception e) {
            message = "删除保险信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("insurance:export")
    public void export(QueryRequest request, Insurance insurance, HttpServletResponse response) throws FebsException {
        try {
            List<Insurance> insurances = this.insuranceService.findInsuranceDetail(request, insurance).getRecords();
            ExcelKit.$Export(Insurance.class, response).downXlsx(insurances, false);
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
        List<InsuranceImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            InsuranceImport insurance = new InsuranceImport();
            insurance.setInsideOrOutside(0);
            insurance.setIdNum("证照号码" + (i + 1));
            insurance.setMedicalMutualAidIndividual("0");
            list.add(insurance);
        });
        // 构建模板
        ExcelKit.$Export(InsuranceImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入保险信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("insurance:add")
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
            final List<Insurance> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            String dateArr[] = date.split("-");
            LocalDateTime now = LocalDateTime.now();
            ExcelKit.$Import(InsuranceImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<InsuranceImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, InsuranceImport entity) {
                    // 数据校验成功时，加入集合
                    InsuranceInside insuranceInside;
                    if (entity.getInsideOrOutside() == 0) {
                        insuranceInside = insuranceInsideService.getInsuranceInsideByIdNum(entity.getIdNum());
                    } else {
                        insuranceInside = insuranceOutsideService.getInsuranceOutsideByIdNum(entity.getIdNum());
                    }
                    if (insuranceInside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getIdNum(),
                                "证照号码",
                                "查询不到此编" + (entity.getInsideOrOutside() == 1 ? "内" : "外") + "人员的保险信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        Insurance insurance = new Insurance();
                        insurance.setName(insuranceInside.getName());
                        insurance.setInsideOrOutside(entity.getInsideOrOutside() + "");
                        insurance.setInsuranceId(insuranceInside.getId());
                        // 养老保险 个人
                        if (entity.getPensionInsuranceIndividual().equals("$EMPTY_CELL$")) { // 养老保险基数 * 0.08
                            insurance.setPensionInsuranceIndividual(insuranceInside.getPensionInsuranceBase().multiply(new BigDecimal("0.08")));
                        } else {
                            insurance.setPensionInsuranceIndividual(new BigDecimal(entity.getPensionInsuranceIndividual()));
                        }
                        // 医疗保险 个人
                        if (entity.getMedicalInsuranceIndividual().equals("$EMPTY_CELL$")) { // 医疗保险基数 * 0.02
                            insurance.setMedicalInsuranceIndividual(insuranceInside.getMedicalInsuranceBase().multiply(new BigDecimal("0.02")));
                        } else {
                            insurance.setMedicalInsuranceIndividual(new BigDecimal(entity.getMedicalInsuranceIndividual()));
                        }

                        // 医疗互助 个人
                        insurance.setMedicalMutualAidIndividual(new BigDecimal(entity.getMedicalMutualAidIndividual()));

                        // 养老保险 单位
                        if (entity.getPensionInsuranceUnit().equals("$EMPTY_CELL$")) { // 养老保险基数 * 0.16
                            insurance.setPensionInsuranceUnit(insuranceInside.getPensionInsuranceBase().multiply(new BigDecimal("0.16")));
                        } else {
                            insurance.setPensionInsuranceUnit(new BigDecimal(entity.getPensionInsuranceUnit()));
                        }

                        // 失业保险 单位
                        if (entity.getUnemploymentInsuranceUnit().equals("$EMPTY_CELL$")) { // 养老保险基数 * 0.008
                            insurance.setUnemploymentInsuranceUnit(insuranceInside.getPensionInsuranceBase().multiply(new BigDecimal("0.008")));
                        } else {
                            insurance.setUnemploymentInsuranceUnit(new BigDecimal(entity.getUnemploymentInsuranceUnit()));
                        }

                        // 工伤保险 单位
                        if (entity.getWorkInjuryInsuranceUnit().equals("$EMPTY_CELL$")) { // 工伤保险基数 * 0.002
                            insurance.setWorkInjuryInsuranceUnit(insuranceInside.getWorkInjuryInsuranceBase().multiply(new BigDecimal("0.002")));
                        } else {
                            insurance.setWorkInjuryInsuranceUnit(new BigDecimal(entity.getWorkInjuryInsuranceUnit()));
                        }

                        // 生育保险 单位
                        if (entity.getMaternityInsuranceUnit().equals("$EMPTY_CELL$")) { // 医疗保险基数 * 0.008
                            insurance.setMaternityInsuranceUnit(insuranceInside.getMedicalInsuranceBase().multiply(new BigDecimal("0.008")));
                        } else {
                            insurance.setMaternityInsuranceUnit(new BigDecimal(entity.getMaternityInsuranceUnit()));
                        }

                        // 医疗保险 单位
                        if (entity.getMedicalInsuranceUnit().equals("$EMPTY_CELL$")) { // 医疗保险基数 * 0.09
                            insurance.setMedicalInsuranceUnit(insuranceInside.getMedicalInsuranceBase().multiply(new BigDecimal("0.09")));
                        } else {
                            insurance.setMedicalInsuranceUnit(new BigDecimal(entity.getMedicalInsuranceUnit()));
                        }

                        // 医疗互助 单位
                        if (entity.getMedicalMutualAidUnit().equals("$EMPTY_CELL$")) { // 医疗保险基数 * 0.01
                            insurance.setMedicalMutualAidUnit(insuranceInside.getMedicalInsuranceBase().multiply(new BigDecimal("0.01")));
                        } else {
                            insurance.setMedicalMutualAidUnit(new BigDecimal(entity.getMedicalMutualAidUnit()));
                        }

                        // 编外没有的三项数据
                        if (entity.getInsideOrOutside() == 0) {
                            // 职业年金 个人
                            if (entity.getCorporateAnnuityIndividual().equals("$EMPTY_CELL$")) { // 养老保险基数 * 0.04
                                insurance.setCorporateAnnuityIndividual(insuranceInside.getPensionInsuranceBase().multiply(new BigDecimal("0.04")));
                            } else {
                                insurance.setCorporateAnnuityIndividual(new BigDecimal(entity.getCorporateAnnuityIndividual()));
                            }
                            // 职业年金 单位
                            if (entity.getCorporateAnnuityUnit().equals("$EMPTY_CELL$")) { // 养老保险基数 * 0.08
                                insurance.setCorporateAnnuityUnit(insuranceInside.getPensionInsuranceBase().multiply(new BigDecimal("0.08")));
                            } else {
                                insurance.setCorporateAnnuityUnit(new BigDecimal(entity.getCorporateAnnuityUnit()));
                            }
                            // 公疗补充 单位
                            if (entity.getPublicTherapySupplementUnit().equals("$EMPTY_CELL$")) { // 医疗保险基数 * 0.03
                                insurance.setPublicTherapySupplementUnit(insuranceInside.getMedicalInsuranceBase().multiply(new BigDecimal("0.03")));
                            } else {
                                insurance.setPublicTherapySupplementUnit(new BigDecimal(entity.getPublicTherapySupplementUnit()));
                            }
                        }

                        // 城镇户口的扣,农村户口的不扣
                        if (StringUtils.isNotBlank(insuranceInside.getHouseholdRegistrationType()) && (insuranceInside.getHouseholdRegistrationType().equals("1") || insuranceInside.getHouseholdRegistrationType().equals("3"))) {
                            // 失业保险 个人
                            if (entity.getUnemploymentInsuranceIndividual().equals("$EMPTY_CELL$")) { // 工伤保险基数 * 0.002
                                insurance.setUnemploymentInsuranceIndividual(insuranceInside.getWorkInjuryInsuranceBase().multiply(new BigDecimal("0.002")));
                            } else {
                                insurance.setUnemploymentInsuranceIndividual(new BigDecimal(entity.getUnemploymentInsuranceIndividual()));
                            }
                        }

                        insurance.setCreateTime(now);
                        insurance.setYear(dateArr[0]);
                        insurance.setMonth(dateArr[1]);
                        data.add(insurance);
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
                this.insuranceService.batchInsertInsurance(data);
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
