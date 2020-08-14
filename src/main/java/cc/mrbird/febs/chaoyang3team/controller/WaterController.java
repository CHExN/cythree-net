package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Water;
import cc.mrbird.febs.chaoyang3team.domain.WaterImport;
import cc.mrbird.febs.chaoyang3team.service.WaterService;
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
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("water")
public class WaterController extends BaseController {

    private String message;

    @Autowired
    private WaterService waterService;
    @Autowired
    private WcService wcService;

    @GetMapping
    @RequiresPermissions("water:view")
    public Map<String, Object> waterList(QueryRequest request, Water water) {
        return getDataTable(this.waterService.findWaterDetail(request, water));
    }

    @Log("新增水费信息")
    @PostMapping
    @RequiresPermissions("water:add")
    public void addWater(@Valid Water water) throws FebsException {
        try {
            this.waterService.createWater(water);
        } catch (Exception e) {
            message = "新增水费信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改水费信息")
    @PutMapping
    @RequiresPermissions("water:update")
    public void updateWater(@Valid Water water) throws FebsException {
        try {
            this.waterService.updateWater(water);
        } catch (Exception e) {
            message = "修改水费信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除水费信息")
    @DeleteMapping("/{waterIds}")
    @RequiresPermissions("water:delete")
    public void deleteWaters(@NotBlank(message = "{required}") @PathVariable String waterIds) throws FebsException {
        try {
            String[] ids = waterIds.split(StringPool.COMMA);
            this.waterService.deleteWater(ids);
        } catch (Exception e) {
            message = "删除水费信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("water:export")
    public void export(QueryRequest request, Water water, HttpServletResponse response) throws FebsException {
        try {
            List<Water> waters = this.waterService.findWaterDetail(request, water).getRecords();
            ExcelKit.$Export(Water.class, response).downXlsx(waters, false);
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
        List<Water> list = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            Water water = new Water();
            water.setWcNum("公厕编号后四位" + (i + 1));
            water.setActualAmount(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            // water.setUnitPrice(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            water.setTapWaterFee(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            water.setWaterResourcesFee(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            water.setSewageFee(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            water.setTotalAmount(new BigDecimal(Math.random() * (100 - 20) + 20).setScale(2, BigDecimal.ROUND_DOWN));
            list.add(water);
        });
        // 构建模板
        ExcelKit.$Export(WaterImport.class, response).downXlsx(list, true);
    }

    /**
     * 导入Excel数据，并批量插入
     */
    @Log("导入水费信息Excel数据，并批量插入")
    @PostMapping("import")
    @RequiresPermissions("water:add")
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
            final List<Water> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(WaterImport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<WaterImport>() {

                @Override
                public void onSuccess(int sheetIndex, int rowIndex, WaterImport entity) {
                    // 数据校验成功时，加入集合
                    String wcNum = String.format("%04d", Integer.valueOf(entity.getWcNum().trim()));
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
                        Water water = new Water();
                        water.setYear(entity.getYear());
                        water.setMonth(entity.getMonth());
                        water.setWcId(wcId);
                        water.setWcNum(wcNum);
                        water.setActualAmount(entity.getActualAmount());
                        // water.setUnitPrice(entity.getUnitPrice());
                        water.setUnitPrice(entity.getTotalAmount().divide(entity.getActualAmount() ,4, BigDecimal.ROUND_HALF_UP));
                        water.setTapWaterFee(entity.getTapWaterFee());
                        water.setWaterResourcesFee(entity.getWaterResourcesFee());
                        water.setSewageFee(entity.getSewageFee());
                        water.setTotalAmount(entity.getTotalAmount());
                        waterService.createWater(water);
                        data.add(water);
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
