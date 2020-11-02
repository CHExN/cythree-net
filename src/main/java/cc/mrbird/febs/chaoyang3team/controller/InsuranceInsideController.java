package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceInsideImport;
import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.service.InsuranceInsideService;
import cc.mrbird.febs.chaoyang3team.service.StaffInsideService;
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
@RequestMapping("insuranceInside")
public class InsuranceInsideController extends BaseController {

    private String message;

    @Autowired
    private InsuranceInsideService insuranceInsideService;
    @Autowired
    private StaffInsideService staffInsideService;

    @GetMapping("insuranceInsideSimplify")
    @RequiresPermissions("insuranceInside:view")
    public Map<String, Object> insuranceInsideSimplify(QueryRequest request, InsuranceInside insuranceInside) {
        return getDataTable(this.insuranceInsideService.findInsuranceInsideSimplify(request, insuranceInside));
    }

    @GetMapping
    @RequiresPermissions("insuranceInside:view")
    public Map<String, Object> insuranceInsideList(QueryRequest request, InsuranceInside insuranceInside) {
        return getDataTable(this.insuranceInsideService.findInsuranceInsideDetail(request, insuranceInside));
    }

    @Log("新增编内保险人员信息")
    @PostMapping
    @RequiresPermissions("insuranceInside:add")
    public void addInsuranceInside(@Valid InsuranceInside insuranceInside) throws FebsException {
        try {
            this.insuranceInsideService.createInsuranceInside(insuranceInside);
        } catch (Exception e) {
            message = "新增编内保险人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改编内保险人员信息")
    @PutMapping
    @RequiresPermissions("insuranceInside:update")
    public void updateInsuranceInside(@Valid InsuranceInside insuranceInside) throws FebsException {
        try {
            this.insuranceInsideService.updateInsuranceInside(insuranceInside);
        } catch (Exception e) {
            message = "修改编内保险人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除编内保险人员信息")
    @DeleteMapping("/{insuranceInsideIds}")
    @RequiresPermissions("insuranceInside:delete")
    public void deleteInsuranceInsides(@NotBlank(message = "{required}") @PathVariable String insuranceInsideIds) throws FebsException {
        try {
            String[] ids = insuranceInsideIds.split(StringPool.COMMA);
            this.insuranceInsideService.deleteInsuranceInside(ids);
        } catch (Exception e) {
            message = "删除编内保险人员信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("insuranceInside:export")
    public void export(QueryRequest request, InsuranceInside insuranceInside, HttpServletResponse response) throws FebsException {
        try {
            List<InsuranceInside> insuranceInsides = this.insuranceInsideService.findInsuranceInsideDetail(request, insuranceInside).getRecords();
            ExcelKit.$Export(InsuranceInside.class, response).downXlsx(insuranceInsides, false);
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
        List<InsuranceInsideImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            InsuranceInsideImport insuranceInside = new InsuranceInsideImport();
            insuranceInside.setIdNum("身份证号" + (i + 1));
            insuranceInside.setPensionInsuranceBase("0");
            insuranceInside.setWorkInjuryInsuranceBase("0");
            insuranceInside.setMedicalInsuranceBase("0");
            insuranceInside.setAccountAddress("户口地址" + (i + 1));
            insuranceInside.setAccountPostalCode("户口地址邮编" + (i + 1));
            insuranceInside.setCurrentAddress("现居住地址" + (i + 1));
            insuranceInside.setCurrentPostalCode("现居住地址邮编" + (i + 1));
            insuranceInside.setBankCardNumber("银行卡号" + (i + 1));
            insuranceInside.setSocialSecurityHospital("社保医院" + (i + 1));
            insuranceInside.setNewTransfer("0");
            list.add(insuranceInside);
        });
        // 构建模板
        ExcelKit.$Export(InsuranceInsideImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入工资信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("insuranceInside:add")
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
            final List<InsuranceInside> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            LocalDateTime now = LocalDateTime.now();
            ExcelKit.$Import(InsuranceInsideImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<InsuranceInsideImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, InsuranceInsideImport entity) {
                    // 数据校验成功时，加入集合
                    StaffInside staffInside = staffInsideService.getStaffIdByIdNum(entity.getIdNum());
                    if (staffInside == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                0,
                                entity.getIdNum(),
                                "身份证号",
                                "查询不到此编内人员的信息"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        InsuranceInside insuranceInside = new InsuranceInside();
                        insuranceInside.setStaffId(staffInside.getStaffId());
                        insuranceInside.setName(staffInside.getName());
                        insuranceInside.setIdNum(entity.getIdNum());
                        insuranceInside.setNewTransfer(entity.getNewTransfer());
                        insuranceInside.setPensionInsuranceBase(new BigDecimal(entity.getPensionInsuranceBase()));
                        insuranceInside.setWorkInjuryInsuranceBase(new BigDecimal(entity.getWorkInjuryInsuranceBase()));
                        insuranceInside.setMedicalInsuranceBase(new BigDecimal(entity.getMedicalInsuranceBase()));
                        insuranceInside.setAccountAddress(entity.getAccountAddress().equals("$EMPTY_CELL$") ? "" : entity.getAccountAddress());
                        insuranceInside.setAccountPostalCode(entity.getAccountPostalCode().equals("$EMPTY_CELL$") ? "" : entity.getAccountPostalCode());
                        insuranceInside.setCurrentAddress(entity.getCurrentAddress().equals("$EMPTY_CELL$") ? "" : entity.getCurrentAddress());
                        insuranceInside.setCurrentPostalCode(entity.getCurrentPostalCode().equals("$EMPTY_CELL$") ? "" : entity.getCurrentPostalCode());
                        insuranceInside.setBankCardNumber(entity.getBankCardNumber().equals("$EMPTY_CELL$") ? "" : entity.getBankCardNumber());
                        insuranceInside.setSocialSecurityHospital(entity.getSocialSecurityHospital().equals("$EMPTY_CELL$") ? "" : entity.getSocialSecurityHospital());
                        insuranceInside.setCreateTime(now);
                        data.add(insuranceInside);
                    }
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量插入
                this.insuranceInsideService.batchInsertInsuranceInside(data);
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
