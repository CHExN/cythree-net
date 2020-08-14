package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomService;
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
@RequestMapping("unionStoreroom")
public class UnionStoreroomController extends BaseController {

    @Autowired
    private UnionStoreroomService storeroomService;

    @GetMapping
    @RequiresPermissions("unionStoreroom:view")
    public Map<String, Object> storeroomList(QueryRequest request, UnionStoreroom storeroom, ServletRequest servletRequest) {
        return getDataTable(this.storeroomService.findStoreroomsDetail(request, storeroom, servletRequest));
    }

    @GetMapping("itemDetails")
    @RequiresPermissions("unionStoreroomItemDetails:view")
    public Map<String, Object> findStoreroomsItemDetails(QueryRequest request, UnionStoreroom storeroom, ServletRequest servletRequest) {
        return getDataTable(this.storeroomService.findStoreroomsItemDetails(request, storeroom, servletRequest));
    }

    @GetMapping("storeroomOutItem")
    @RequiresPermissions("unionStoreroomItemDetails:view")
    public List<UnionStoreroom> getStoreroomOutItem(UnionStoreroom storeroom) {
        return storeroomService.getStoreroomOutItemByParentIdAndId(storeroom);
    }

}
