package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.CarSendMapper;
import cc.mrbird.febs.chaoyang3team.domain.CarSend;
import cc.mrbird.febs.chaoyang3team.service.CarSendService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("carSendService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarSendServiceImpl extends ServiceImpl<CarSendMapper, CarSend> implements CarSendService {

    @Autowired
    private UserManager userManager;

    @Override
    public IPage<CarSend> findCarSend(QueryRequest request, CarSend carSend, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“技安”（id：76）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("76");
            if (!isRoleId) carSend.setDeptId(user.getDeptId());

            Page<CarSend> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findCarSendDetail(page, carSend);
        } catch (Exception e) {
            log.error("查询派车信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createCarSend(CarSend carSend, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        carSend.setUsername(username);
        carSend.setDeptId(user.getDeptId());
        carSend.setCreateTime(LocalDateTime.now());
        if (carSend.getCarId() != null) carSend.setStatus("1");
        this.save(carSend);
    }

    @Override
    @Transactional
    public void updateCarSend(CarSend carSend) {
        carSend.setModifyTime(LocalDateTime.now());
        if (carSend.getCarId() != null) carSend.setStatus("1");
        this.baseMapper.updateById(carSend);
    }

    @Override
    @Transactional
    public void deleteCarSend(String[] carSendIds) {
        List<String> list = Arrays.asList(carSendIds);
        this.baseMapper.deleteBatchIds(list);
    }

}
