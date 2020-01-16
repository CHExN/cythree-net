package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.domain.CarRepair;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.dao.CarRepairMapper;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.chaoyang3team.service.CarRepairService;
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
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("carRepairService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarRepairServiceImpl extends ServiceImpl<CarRepairMapper, CarRepair> implements CarRepairService {

    @Autowired
    private UserManager userManager;

    @Autowired
    private MessageService messageService;

    @Override
    public IPage<CarRepair> findRepairDetail(QueryRequest request, CarRepair carRepair) {
        try {
            Page<CarRepair> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findRepairDetail(page, carRepair);
        } catch (Exception e) {
            log.error("查询车辆报修申请异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createRepair(CarRepair carRepair, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        carRepair.setDeptId(user.getDeptId());
        carRepair.setDeptName(user.getDeptName());
        this.save(carRepair);
        // 发送信息到技安那边
        StringBuilder message = new StringBuilder();
        message.append(carRepair.getReporter()).append("提交了车号牌为 ").append(carRepair.getCarNum()).append(" 的车辆报修申请");
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                username,
                user.getDeptName(),
                "cars",
                null)
        );
        message.delete(0, message.length());
    }

    @Override
    @Transactional
    public void updateRepair(CarRepair carRepair) {
        this.baseMapper.updateById(carRepair);
    }

    @Override
    @Transactional
    public void deleteRepair(String[] repairIds) {
        List<String> list = Arrays.asList(repairIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
