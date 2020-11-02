package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcInspectionMapper;
import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcInspection;
import cc.mrbird.febs.chaoyang3team.domain.WcTemplate;
import cc.mrbird.febs.chaoyang3team.service.WcInspectionService;
import cc.mrbird.febs.chaoyang3team.service.WcService;
import cc.mrbird.febs.chaoyang3team.service.WcTemplateService;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CHExN
 */
@Slf4j
@Service("wcInspectionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcInspectionServiceImpl extends ServiceImpl<WcInspectionMapper, WcInspection> implements WcInspectionService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private WcService wcService;
    @Autowired
    private WcTemplateService wcTemplateService;

    @Override
    public Map<String, Object> findWcInspectionAndWcTemplateDetail(QueryRequest request, WcInspection wcInspection, int pageSize1, int pageNum1) {

        QueryRequest templateWcRequest = new QueryRequest();
        templateWcRequest.setPageNum(pageNum1);
        templateWcRequest.setPageSize(pageSize1);
        IPage<Wc> templateWcData = wcTemplateService.getTemplateWcList(templateWcRequest, wcInspection.getWcTemplateId());
        IPage<WcInspection> wcInspectionData = findWcInspectionDetail(request, wcInspection);


        templateWcData.setRecords(templateWcData.getRecords().stream()
                .filter(item -> {
                    List<Long> wcInspectionWcIdList = wcInspectionData.getRecords().stream().map(WcInspection::getWcId)
                            .collect(Collectors.toList());
                    if (wcInspectionWcIdList.contains(item.getWcId())) {
                        item.setStatus("green");
                    } else {
                        item.setStatus("red");
                    }
                    return true;
                })
                .collect(Collectors.toList()));


        Map<String, Object> data = new HashMap<>();
        data.put("wcInspectionList", wcInspectionData);
        data.put("templateWcList", templateWcData);

        return data;
    }

    private IPage<WcInspection> findWcInspectionDetail(QueryRequest request, WcInspection wcInspection) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            if (wcInspection.getDeleted() == null) wcInspection.setDeleted(0);
            return this.baseMapper.findWcInspectionDetail(page, wcInspection);
        } catch (Exception e) {
            log.error("查询公厕巡检异常", e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getWcInspectionAndWcLocation(QueryRequest request, ServletRequest servletRequest, WcInspection wcInspection, String longitude, String latitude, Integer radius, Integer length) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));

        wcInspection.setInspectionDateString(LocalDate.now().toString());
        wcInspection.setUsername(username);

        // 巡检员在小程序里，只能看到符合自己
        IPage<WcInspection> wcInspectionData = findWcInspectionDetail(request, wcInspection);
        List<Wc> wcListByPosition = wcService.findWcListByPosition(longitude, latitude, radius, length);
        // 获取差集
        List<Wc> resultList = wcListByPosition.stream()
                .filter(item -> !wcInspectionData.getRecords().stream().map(WcInspection::getWcId)
                        .collect(Collectors.toList()).contains(item.getWcId()))
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("wcInspectionList", wcInspectionData);
        data.put("wcList", resultList);
        return data;
    }

    @Override
    @Transactional
    public void createWcInspection(WcInspection wcInspection, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        wcInspection.setUsername(username);
        wcInspection.setDeptId(user.getDeptId());
        wcInspection.setCreateTime(LocalDateTime.now());
        wcInspection.setInspectionDate(LocalDateTime.now());
        this.save(wcInspection);
    }

    @Override
    @Transactional
    public void updateWcInspection(WcInspection wcInspection) {
        wcInspection.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(wcInspection);
    }

    @Override
    @Transactional
    public void deleteWcInspection(String[] wcInspectionIds) {
        List<String> ids = Arrays.asList(wcInspectionIds);
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public Map<String, Object> getWcInspectionLoadData(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        Set<String> userPermissions = this.userManager.getUserPermissions(username);

        Map<String, Object> data = new HashMap<>();
        // 查询所有username and 部门id and部门name
        // 如果没有查看全部的权限，就查看自己部门的信息
        Map<String, WcInspection> wcInspectionUsername = this.baseMapper.getWcInspectionUsername(
                userPermissions.contains("wcInspection:viewAll") ? null : user.getDeptId(), 0);
        data.put("username", wcInspectionUsername);
        // 查询所有 模板name and 模板id
        Map<String, WcTemplate> wcTemplate = this.wcTemplateService.getAllWcTemplate(0);
        data.put("template", wcTemplate);
        return data;
    }
}
