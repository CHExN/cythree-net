package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcStoreroomMapper;
import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.UnitConversion;
import cc.mrbird.febs.chaoyang3team.domain.WcStoreroom;
import cc.mrbird.febs.chaoyang3team.service.StoreroomService;
import cc.mrbird.febs.chaoyang3team.service.UnitConversionService;
import cc.mrbird.febs.chaoyang3team.service.WcStoreroomService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author CHExN
 */
@Slf4j
@Service("wcStoreroomService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcStoreroomServiceImpl extends ServiceImpl<WcStoreroomMapper, WcStoreroom> implements WcStoreroomService {

    @Autowired
    private StoreroomService storeroomService;
    @Autowired
    private UnitConversionService unitConversionService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<WcStoreroom> findWcStoreroomDetail(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);

            // 如果没有wcStoreroom:viewAll权限，就只能看自己的
            if (!userPermissions.contains("wcStoreroom:viewAll")) {
                wcStoreroom.setUsername(username);
            }

            Page<WcStoreroom> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return this.baseMapper.findWcStoreroomDetail(page, wcStoreroom);
        } catch (Exception e) {
            log.error("查询分配数据异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteWcStoreroomsByWcId(String[] wcIds) {
        List<String> list = Arrays.asList(wcIds);
        baseMapper.delete(new LambdaQueryWrapper<WcStoreroom>().in(WcStoreroom::getWcId, list));
    }

    @Override
    @Transactional
    public void deleteWcStoreroomsByStoreroomId(String[] storeroomIds) {
        List<String> list = Arrays.asList(storeroomIds);
        baseMapper.delete(new LambdaQueryWrapper<WcStoreroom>().in(WcStoreroom::getStoreroomId, list));
    }

    @Override
    @Transactional
    public void createWcStoreroom(String wcStoreroomStr, BigDecimal amountDist, ServletRequest servletRequest) {
        // 获取当前操作用户的账号名称
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        // 转换格式
        JSONArray jsonArray = JSONArray.fromObject(wcStoreroomStr);
        List<WcStoreroom> wcStoreroomList = (List<WcStoreroom>) JSONArray.toCollection(jsonArray, WcStoreroom.class);
        LocalDateTime now = LocalDateTime.now();

        for (WcStoreroom wcStoreroom : wcStoreroomList) {
            wcStoreroom.setYear(DateUtil.formatFullTime(now, "yyyy"));
            wcStoreroom.setMonth(DateUtil.formatFullTime(now, "MM"));
            wcStoreroom.setDay(DateUtil.formatFullTime(now, "dd"));
            wcStoreroom.setUsername(username);
        }
        this.batchInsertWcStoreroom(wcStoreroomList);

        Storeroom storeroom = new Storeroom();
        storeroom.setId(wcStoreroomList.get(0).getStoreroomId());
        // 根据物品id查询单位转换
        UnitConversion unitConversion = this.unitConversionService.findById(storeroom.getId().toString());

        // 判断是否有单位转换
        if (unitConversion == null) {
            storeroom.setAmountDist(amountDist);
            if (amountDist.compareTo(BigDecimal.ZERO) == 0) {
                storeroom.setStatus("1");
            }
            this.storeroomService.updateStoreroom(storeroom);
        } else {
            unitConversion.setAmountDist(amountDist);
            this.unitConversionService.saveOrUpdateUnitConversion(unitConversion);
            if (amountDist.compareTo(BigDecimal.ZERO) == 0) {
                storeroom.setStatus("1");
                this.storeroomService.updateStoreroom(storeroom);
            }
        }
    }

    @Override
    @Transactional
    public void batchInsertWcStoreroom(List<WcStoreroom> wcStoreroomList) {
        this.saveBatch(wcStoreroomList);
    }

}
