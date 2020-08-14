package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Electricity;
import cc.mrbird.febs.chaoyang3team.domain.ElectricityImport;
import cc.mrbird.febs.chaoyang3team.service.ElectricityService;
import cc.mrbird.febs.chaoyang3team.service.WcService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("electricity")
public class ElectricityController extends BaseController {

    private String message;

    @Autowired
    private ElectricityService electricityService;
    @Autowired
    private WcService wcService;

    @GetMapping
    @RequiresPermissions("electricity:view")
    public Map<String, Object> electricityList(QueryRequest request, Electricity electricity) {
        return getDataTable(this.electricityService.findElectricityDetail(request, electricity));
    }

    @Log("新增电费信息")
    @PostMapping
    @RequiresPermissions("electricity:add")
    public void addElectricity(@Valid Electricity electricity) throws FebsException {
        try {
            this.electricityService.createElectricity(electricity);
        } catch (Exception e) {
            message = "新增电费信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改电费信息")
    @PutMapping
    @RequiresPermissions("electricity:update")
    public void updateElectricity(@Valid Electricity electricity) throws FebsException {
        try {
            this.electricityService.updateElectricity(electricity);
        } catch (Exception e) {
            message = "修改电费信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除电费信息")
    @DeleteMapping("/{electricityIds}")
    @RequiresPermissions("electricity:delete")
    public void deleteElectricitys(@NotBlank(message = "{required}") @PathVariable String electricityIds) throws FebsException {
        try {
            String[] ids = electricityIds.split(StringPool.COMMA);
            this.electricityService.deleteElectricity(ids);
        } catch (Exception e) {
            message = "删除电费信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("electricity:export")
    public void export(QueryRequest request, Electricity electricity, HttpServletResponse response) throws FebsException {
        try {
            List<Electricity> electricitys = this.electricityService.findElectricityDetail(request, electricity).getRecords();
            ExcelKit.$Export(Electricity.class, response).downXlsx(electricitys, false);
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
        List<Electricity> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            Electricity electricity = new Electricity();
            electricity.setRecDate(new Date());
            electricity.setWcNum("公厕编号后四位" + (i + 1));
            electricity.setActualAmount(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            electricity.setUnitPrice(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            electricity.setTotalAmount(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            electricity.setType("购电方式" + (i + 1));
            list.add(electricity);
        });
        // 构建模板
        ExcelKit.$Export(ElectricityImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入电费信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("electricity:add")
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
            final List<Electricity> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(ElectricityImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<ElectricityImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, ElectricityImport entity) {
                    // 数据校验成功时，加入集合
                    String wcNum = String.format("%04d", Integer.valueOf(entity.getWcNum().trim()));
                    System.out.println(wcNum);
                    Long wcId = wcService.getWcIdByWcNum(wcNum, true);
                    if (wcId == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(
                                1,
                                wcNum,
                                "公厕编号后四位",
                                "查询不到 “公厕编号” 后四位为「" + wcNum + "」的这个公厕"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        Electricity electricity = new Electricity();
                        electricity.setRecDate(entity.getRecDate());
                        electricity.setYear(entity.getYear());
                        electricity.setMonth(entity.getMonth());
                        electricity.setWcId(wcId);
                        electricity.setWcNum(wcNum);
                        electricity.setActualAmount(entity.getActualAmount());
                        electricity.setUnitPrice(entity.getUnitPrice());
                        electricity.setTotalAmount(entity.getTotalAmount());
                        electricity.setType(entity.getType());
                        electricityService.createElectricity(electricity);
                        data.add(electricity);
                    }
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
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
