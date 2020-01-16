package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.InsuranceOutside;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceOutsideImport;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.InsuranceOutsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffOutsideService;
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
@RequestMapping("insuranceOutside")
public class InsuranceOutsideController extends BaseController {

    private String message;

    @Autowired
    private InsuranceOutsideService insuranceOutsideService;
    @Autowired
    private StaffOutsideService staffOutsideService;

    @GetMapping("insuranceOutsideSimplify")
    @RequiresPermissions("insuranceOutside:view")
    public Map<String, Object> insuranceOutsideSimplify(QueryRequest request, InsuranceOutside insuranceOutside) {
        return getDataTable(this.insuranceOutsideService.findInsuranceOutsideSimplify(request, insuranceOutside));
    }

    @GetMapping
    @RequiresPermissions("insuranceOutside:view")
    public Map<String, Object> insuranceOutsideList(QueryRequest request, InsuranceOutside insuranceOutside) {
        return getDataTable(this.insuranceOutsideService.findInsuranceOutsideDetail(request, insuranceOutside));
    }

    @Log("新增编外保险人员信息")
    @PostMapping
    @RequiresPermissions("insuranceOutside:add")
    public void addInsuranceOutside(@Valid InsuranceOutside insuranceOutside) throws FebsException {
        try {
            this.insuranceOutsideService.createInsuranceOutside(insuranceOutside);
        } catch (Exception e) {
            message = "新增编外保险人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编外保险人员信息")
    @PutMapping
    @RequiresPermissions("insuranceOutside:update")
    public void updateInsuranceOutside(@Valid InsuranceOutside insuranceOutside) throws FebsException {
        try {
            this.insuranceOutsideService.updateInsuranceOutside(insuranceOutside);
        } catch (Exception e) {
            message = "修改编外保险人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编外保险人员信息")
    @DeleteMapping("/{insuranceOutsideIds}")
    @RequiresPermissions("insuranceOutside:delete")
    public void deleteInsuranceOutsides(@NotBlank(message = "{required}") @PathVariable String insuranceOutsideIds) throws FebsException {
        try {
            String[] ids = insuranceOutsideIds.split(StringPool.COMMA);
            this.insuranceOutsideService.deleteInsuranceOutside(ids);
        } catch (Exception e) {
            message = "删除编外保险人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("insuranceOutside:export")
    public void export(QueryRequest request, InsuranceOutside insuranceOutside, HttpServletResponse response) throws FebsException {
        try {
            List<InsuranceOutside> insuranceOutsides = this.insuranceOutsideService.findInsuranceOutsideDetail(request, insuranceOutside).getRecords();
            ExcelKit.$Export(InsuranceOutside.class, response).downXlsx(insuranceOutsides, false);
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
        List<InsuranceOutsideImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            InsuranceOutsideImport insuranceOutside = new InsuranceOutsideImport();
            insuranceOutside.setIdNum("身份证号" + (i + 1));
            insuranceOutside.setPensionInsuranceBase("0");
            insuranceOutside.setWorkInjuryInsuranceBase("0");
            insuranceOutside.setMedicalInsuranceBase("0");
            insuranceOutside.setAccountAddress("户口地址" + (i + 1));
            insuranceOutside.setAccountPostalCode("户口地址邮编" + (i + 1));
            insuranceOutside.setCurrentAddress("现居住地址" + (i + 1));
            insuranceOutside.setCurrentPostalCode("现居住地址邮编" + (i + 1));
            insuranceOutside.setBankCardNumber("银行卡号" + (i + 1));
            insuranceOutside.setSocialSecurityHospital("社保医院" + (i + 1));
            insuranceOutside.setNewTransfer("0");
            list.add(insuranceOutside);
        });
        // 构建模板
        ExcelKit.$Export(InsuranceOutsideImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入工资信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("insuranceOutside:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file) throws FebsException {
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
            final List<InsuranceOutside> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            LocalDateTime now = LocalDateTime.now();
            ExcelKit.$Import(InsuranceOutsideImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<InsuranceOutsideImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, InsuranceOutsideImport entity) {
                    // 数据校验成功时，加入集合
                    StaffOutside staffOutside = staffOutsideService.getStaffIdByIdNum(entity.getIdNum());
                    if (staffOutside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getIdNum(),
                                "身份证号",
                                "查询不到此编内人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        InsuranceOutside insuranceOutside = new InsuranceOutside();
                        insuranceOutside.setStaffId(staffOutside.getStaffId());
                        insuranceOutside.setName(staffOutside.getName());
                        insuranceOutside.setIdNum(entity.getIdNum());
                        insuranceOutside.setNewTransfer(entity.getNewTransfer());
                        insuranceOutside.setPensionInsuranceBase(new BigDecimal(entity.getPensionInsuranceBase()));
                        insuranceOutside.setWorkInjuryInsuranceBase(new BigDecimal(entity.getWorkInjuryInsuranceBase()));
                        insuranceOutside.setMedicalInsuranceBase(new BigDecimal(entity.getMedicalInsuranceBase()));
                        insuranceOutside.setAccountAddress(entity.getAccountAddress().equals("$EMPTY_CELL$") ? "" : entity.getAccountAddress());
                        insuranceOutside.setAccountPostalCode(entity.getAccountPostalCode().equals("$EMPTY_CELL$") ? "" : entity.getAccountPostalCode());
                        insuranceOutside.setCurrentAddress(entity.getCurrentAddress().equals("$EMPTY_CELL$") ? "" : entity.getCurrentAddress());
                        insuranceOutside.setCurrentPostalCode(entity.getCurrentPostalCode().equals("$EMPTY_CELL$") ? "" : entity.getCurrentPostalCode());
                        insuranceOutside.setBankCardNumber(entity.getBankCardNumber().equals("$EMPTY_CELL$") ? "" : entity.getBankCardNumber());
                        insuranceOutside.setSocialSecurityHospital(entity.getSocialSecurityHospital().equals("$EMPTY_CELL$") ? "" : entity.getSocialSecurityHospital());
                        insuranceOutside.setCreateTime(now);
                        data.add(insuranceOutside);
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
                this.insuranceOutsideService.batchInsertInsuranceOutside(data);
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
