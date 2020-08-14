package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Compared;
import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.chaoyang3team.service.ComparedService;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("compared")
public class ComparedController extends BaseController {

    @Autowired
    private ComparedService comparedService;

    @GetMapping
    @RequiresPermissions("compared:view")
    public List<Compared> comparedList(Compared compared) {
        return this.comparedService.getComparedList(compared);
    }

    @PostMapping("to")
    @RequiresPermissions("compared:view")
    public Map<String, Object> comparedList(QueryRequest request, String c1, String c2) {
        return getDataTable(this.comparedService.compared(request, c1, c2));
    }

    @PostMapping("excel")
    public void export(QueryRequest request, String c1, String c2, HttpServletResponse response) throws FebsException {
        try {
            List<Map<String, Object>> staff = this.comparedService.compared(request, c1, c2).getRecords();
            System.out.println(c1);
            String tableName = c1.substring(c1.indexOf("tableName") + 12, c1.indexOf("tableCnName") - 3);
            System.out.println(c1.indexOf("tableName"));
            System.out.println(c1.indexOf("tableCnName"));
            System.out.println(tableName);
            if (tableName.equals("cy_staff_inside")) {
                ExcelKit.$Export(StaffInside.class, response).downXlsx(staff, false);
            } else if (tableName.equals("cy_staff_outside")) {
                ExcelKit.$Export(StaffOutside.class, response).downXlsx(staff, false);
            }
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
