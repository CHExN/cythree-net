package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.*;
import cc.mrbird.febs.chaoyang3team.service.FireExtinguisherService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@RequestMapping("fireExtinguisher")
public class FireExtinguisherController extends BaseController {

    private String message;

    @Autowired
    private FireExtinguisherService fireExtinguisherService;
    @Autowired
    private WcService wcService;

    @GetMapping
    @RequiresPermissions("fireExtinguisher:view")
    public Map<String, Object> fireExtinguisherList(QueryRequest request, FireExtinguisher fireExtinguisher) {
        return getDataTable(this.fireExtinguisherService.findFireExtinguisherDetail(request, fireExtinguisher));
    }

    @Log("新增灭火器")
    @PostMapping
    @RequiresPermissions("fireExtinguisher:add")
    public void addFireExtinguisher(@Valid FireExtinguisher fireExtinguisher) throws FebsException {
        try {
            this.fireExtinguisherService.createFireExtinguisher(fireExtinguisher);
        } catch (Exception e) {
            message = "新增灭火器失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改灭火器")
    @PutMapping
    @RequiresPermissions("fireExtinguisher:update")
    public void updateFireExtinguisher(@Valid FireExtinguisher fireExtinguisher) throws FebsException {
        try {
            this.fireExtinguisherService.updateFireExtinguisher(fireExtinguisher);
        } catch (Exception e) {
            message = "修改灭火器失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除灭火器")
    @DeleteMapping("/{fireExtinguisherIds}")
    @RequiresPermissions("fireExtinguisher:delete")
    public void deleteFireExtinguishers(@NotBlank(message = "{required}") @PathVariable String fireExtinguisherIds) throws FebsException {
        try {
            String[] ids = fireExtinguisherIds.split(StringPool.COMMA);
            this.fireExtinguisherService.deleteFireExtinguisher(ids);
        } catch (Exception e) {
            message = "删除灭火器失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("fireExtinguisher:export")
    public void export(QueryRequest request, FireExtinguisher fireExtinguisher, HttpServletResponse response) throws FebsException {
        try {
            List<FireExtinguisher> fireExtinguishers = this.fireExtinguisherService.findFireExtinguisherDetail(request, fireExtinguisher).getRecords();
            ExcelKit.$Export(FireExtinguisher.class, response).downXlsx(fireExtinguishers, false);
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
        List<FireExtinguisherImport> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            FireExtinguisherImport fireExtinguisher = new FireExtinguisherImport();
            fireExtinguisher.setAssetName("灭火器" + (i + 1));
            list.add(fireExtinguisher);
        });
        // 构建模板
        ExcelKit.$Export(FireExtinguisherImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入灭火器明细Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("fireExtinguisher:add")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, Integer ifThree) throws FebsException {
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
            final List<FireExtinguisher> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            LocalDateTime now = LocalDateTime.now();
            ExcelKit.$Import(FireExtinguisherImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<FireExtinguisherImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, FireExtinguisherImport entity) {
                    // 数据校验成功时，加入集合
                    Long wcId = null;
                    if (!entity.getWcNum().equals("$EMPTY_CELL$")) {
                        wcId = wcService.getWcIdByWcNum(entity.getWcNum(), false);
                    }
                    if (!entity.getWcNum().equals("$EMPTY_CELL$") && wcId == null) {
                        List<ExcelErrorField> errorFields = new ArrayList<>();
                        errorFields.add(new ExcelErrorField(0, entity.getWcNum(), "公厕编号", "公厕编号不存在"));
                        onError(sheetIndex, rowIndex, errorFields);
                    } else {
                        FireExtinguisher fireExtinguisher = new FireExtinguisher();
                        fireExtinguisher.setWcNum(entity.getWcNum().equals("$EMPTY_CELL$") ? "" : entity.getWcNum());
                        fireExtinguisher.setAssetName(entity.getAssetName().equals("$EMPTY_CELL$") ? "" : entity.getAssetName());
                        fireExtinguisher.setBrandModel(entity.getBrandModel().equals("$EMPTY_CELL$") ? "" : entity.getBrandModel());
                        fireExtinguisher.setAllotmentDate1(entity.getAllotmentDate1().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getAllotmentDate1(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        fireExtinguisher.setAllotmentDate2(entity.getAllotmentDate2().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getAllotmentDate2(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        fireExtinguisher.setUser(entity.getUser().equals("$EMPTY_CELL$") ? "" : entity.getUser());
                        fireExtinguisher.setTestDate1(entity.getTestDate1().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getTestDate1(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        fireExtinguisher.setTestDate2(entity.getTestDate2().equals("$EMPTY_CELL$") ? null : LocalDate.parse(entity.getTestDate2(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        fireExtinguisher.setSpecification1(entity.getSpecification1().equals("$EMPTY_CELL$") ? "" : entity.getSpecification1());
                        fireExtinguisher.setSpecification2(entity.getSpecification2().equals("$EMPTY_CELL$") ? "" : entity.getSpecification2());
                        fireExtinguisher.setPlace(entity.getPlace().equals("$EMPTY_CELL$") ? "" : entity.getPlace());
                        fireExtinguisher.setRemark(entity.getRemark().equals("$EMPTY_CELL$") ? "" : entity.getRemark());
                        fireExtinguisher.setCreateTime(now);
                        data.add(fireExtinguisher);
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
                this.fireExtinguisherService.batchInsertFireExtinguisher(data);
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
