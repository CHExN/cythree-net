package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.CondolencesMapper;
import cc.mrbird.febs.chaoyang3team.domain.Condolences;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.service.CondolencesService;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("condolencesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CondolencesServiceImpl extends ServiceImpl<CondolencesMapper, Condolences> implements CondolencesService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageService messageService;

    @Override
    public IPage<Condolences> findCondolencesDetail(QueryRequest request, Condolences condolences, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“工会”（id：75）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("75");
            if (!isRoleId) condolences.setDeptId(user.getDeptId());

            Page<Condolences> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findCondolencesDetail(page, condolences, isRoleId);
        } catch (Exception e) {
            log.error("查询职工慰问登记信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createCondolences(Condolences condolences, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        condolences.setUsername(username);
        condolences.setDeptId(user.getDeptId());
        condolences.setDate(LocalDate.now());
        this.save(condolences);
    }

    @Override
    @Transactional
    public void updateCondolences(Condolences condolences) {
        this.baseMapper.updateById(condolences);
        if (condolences.getStatus() != null ) {
            String name = condolences.getName();
            StringBuilder message = new StringBuilder();if (condolences.getStatus().equals("2")) {
                message.append("职工姓名为 ").append(name).append(" 的职工慰问登记已通过");
            } else if (condolences.getStatus().equals("3")) {
                message.append("职工姓名为 ").append(name).append(" 的职工慰问登记未通过");
            }
            this.messageService.oneToOne(new Message(
                    null,
                    null,
                    message.toString(),
                    "bot",
                    "系统",
                    condolences.getUsername(),
                    null)
            );
        }
    }

    @Override
    @Transactional
    public void deleteCondolences(String[] condolencesIds) {
        List<String> list = Arrays.asList(condolencesIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
