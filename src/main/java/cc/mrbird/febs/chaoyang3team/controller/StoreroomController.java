package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.service.StoreroomService;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("storeroom")
public class StoreroomController extends BaseController {

    @Autowired
    private StoreroomService storeroomService;

    @GetMapping
    @RequiresPermissions("storeroom:view")
    public Map<String, Object> storeroomList(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        return getDataTable(this.storeroomService.findStoreroomsDetail(request, storeroom, servletRequest));
    }

    @GetMapping("itemDetails")
    @RequiresPermissions("storeroomItemDetails:view")
    public Map<String, Object> findStoreroomsItemDetails(QueryRequest request, Storeroom storeroom) {
        return getDataTable(this.storeroomService.findStoreroomsItemDetails(request, storeroom));
    }

    @GetMapping("storeroomOutItem")
    @RequiresPermissions("storeroomItemDetails:view")
    public List<Storeroom> getStoreroomOutItem(Storeroom storeroom) {
        return storeroomService.getStoreroomOutItemByParentIdAndId(storeroom);
    }

    @GetMapping("storeroomDist")
    @RequiresPermissions("storeroomDist:view")
    public Map<String, Object> storeroomDistList(QueryRequest request, Storeroom storeroom) {
        return getDataTable(this.storeroomService.getStoreroomsDist(request, storeroom));
    }

    @GetMapping("officeSupplies")
    @RequiresPermissions("officeSupplies:export")
    public List<Storeroom> getOfficeSupplies(String date) {
        return storeroomService.getOfficeSuppliesByDate(date);
    }

    @GetMapping("canteen")
    @RequiresPermissions("canteen:export")
    public List<Storeroom> getCanteenByDate(String date) {
        return storeroomService.getCanteenByDate(date);
    }

}
