package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.WcStoreroom;
import cc.mrbird.febs.chaoyang3team.service.WcStoreroomService;
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
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("wcStoreroom")
public class WcStoreroomController extends BaseController {

    @Autowired
    private WcStoreroomService wcStoreroomService;

    @GetMapping
    @RequiresPermissions("wcStoreroom:view")
    public Map<String, Object> WcList(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest) {
//        return getDataTable(this.wcStoreroomService.findWcStoreroomDetail(request, wcStoreroom, servletRequest));
        return getDataTable(this.wcStoreroomService.findWcStoreroomDetail(request, wcStoreroom));
    }

}
