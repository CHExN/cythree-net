package cc.mrbird.febs.chaoyang3team.controller;


import cc.mrbird.febs.chaoyang3team.domain.CarSend;
import cc.mrbird.febs.chaoyang3team.service.CarSendService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("carSend")
public class CarSendController extends BaseController {

    private String message;

    @Autowired
    private CarSendService carSendService;

    @GetMapping
    @RequiresPermissions("carSend:view")
    public Map<String, Object> carSendList(QueryRequest request, CarSend carSend, ServletRequest servletRequest) {
        return getDataTable(this.carSendService.findCarSend(request, carSend, servletRequest));
    }

    @Log("新增派车")
    @PostMapping
    @RequiresPermissions("carSend:add")
    public void addCarSend(@Valid CarSend carSend, ServletRequest servletRequest) throws FebsException {
        try {
            this.carSendService.createCarSend(carSend, servletRequest);
        } catch (Exception e) {
            message = "新增派车失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改派车")
    @PutMapping
    @RequiresPermissions("carSend:update")
    public void updateCarSend(@Valid CarSend carSend) throws FebsException {
        try {
            this.carSendService.updateCarSend(carSend);
        } catch (Exception e) {
            message = "修改派车失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除派车")
    @DeleteMapping("/{carSendIds}")
    @RequiresPermissions("carSend:delete")
    public void deleteCarSends(@NotBlank(message = "{required}") @PathVariable String carSendIds) throws FebsException {
        try {
            String[] ids = carSendIds.split(StringPool.COMMA);
            this.carSendService.deleteCarSend(ids);
        } catch (Exception e) {
            message = "删除派车失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
