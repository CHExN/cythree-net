package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StoreroomMapper;
import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.StoreroomOutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author CHExN
 */
@Slf4j
@Service("storeroomService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreroomServiceImpl extends ServiceImpl<StoreroomMapper, Storeroom> implements StoreroomService {

    @Autowired
    private StoreroomPutService storeroomPutService;
    @Autowired
    private StoreroomOutService storeroomOutService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<Storeroom> findStoreroomsDetail(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);
            // 如果拥有以下任意一个或复数权限，就代表只能看到这些物品的权限，搜索也是一样，搜只会出现这些的物质类别
            List<String> typeApplicationAuthorityList = new ArrayList<>();
            if (userPermissions.contains("storeroom:view1")) {
                typeApplicationAuthorityList.add("1");
            }
            if (userPermissions.contains("storeroom:view2")) {
                typeApplicationAuthorityList.add("2");
            }
            if (userPermissions.contains("storeroom:view3")) {
                typeApplicationAuthorityList.add("3");
            }
            if (userPermissions.contains("storeroom:view4")) {
                typeApplicationAuthorityList.add("4");
            }
            if (userPermissions.contains("storeroom:view5")) {
                typeApplicationAuthorityList.add("5");
            }
            if (userPermissions.contains("storeroom:view6")) {
                typeApplicationAuthorityList.add("6");
            }
            if (userPermissions.contains("storeroom:view7")) {
                typeApplicationAuthorityList.add("7");
            }
            if (userPermissions.contains("storeroom:view8")) {
                typeApplicationAuthorityList.add("8");
            }
            if (userPermissions.contains("storeroom:view9")) {
                typeApplicationAuthorityList.add("9");
            }
            storeroom.setTypeApplicationAuthority(String.join(",", typeApplicationAuthorityList));
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
            if (!(storeroom.getName() == null || storeroom.getName().isEmpty())) {
                storeroom.setNames(storeroom.getName().replace("，", ",").split(","));
            }
            return this.baseMapper.findStoreroomsDetail(page, storeroom);
        } catch (Exception e) {
            log.error("查询库房异常", e);
            return null;
        }
    }

    @Override
    public IPage<Storeroom> findStoreroomsItemDetails(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);
            // 如果拥有以下任意一个或复数权限，就代表只能看到这些物品的权限，搜索也是一样，搜只会出现这些的物质类别
            List<String> typeApplicationAuthorityList = new ArrayList<>();
            if (userPermissions.contains("storeroom:view1")) {
                typeApplicationAuthorityList.add("1");
            }
            if (userPermissions.contains("storeroom:view2")) {
                typeApplicationAuthorityList.add("2");
            }
            if (userPermissions.contains("storeroom:view3")) {
                typeApplicationAuthorityList.add("3");
            }
            if (userPermissions.contains("storeroom:view4")) {
                typeApplicationAuthorityList.add("4");
            }
            if (userPermissions.contains("storeroom:view5")) {
                typeApplicationAuthorityList.add("5");
            }
            if (userPermissions.contains("storeroom:view6")) {
                typeApplicationAuthorityList.add("6");
            }
            if (userPermissions.contains("storeroom:view7")) {
                typeApplicationAuthorityList.add("7");
            }
            if (userPermissions.contains("storeroom:view8")) {
                typeApplicationAuthorityList.add("8");
            }
            if (userPermissions.contains("storeroom:view9")) {
                typeApplicationAuthorityList.add("9");
            }
            storeroom.setTypeApplicationAuthority(String.join(",", typeApplicationAuthorityList));
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
            if (!(storeroom.getName() == null || storeroom.getName().isEmpty())) {
                storeroom.setNames(storeroom.getName().replace("，", ",").split(","));
            }
            return this.baseMapper.findStoreroomsItemDetails(page, storeroom);
        } catch (Exception e) {
            log.error("查询出入库物品明细异常", e);
            return null;
        }
    }


    @Override
    public IPage<Storeroom> getStoreroomsDist(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            // 通过用户名获取用户权限集合
            Set<String> userPermissions = this.userManager.getUserPermissions(username);

            // 如果拥有以下一个权限，就表示只能看到这一个分队的物资信息
            if (userPermissions.contains("staffOutside:viewClean")) {
                storeroom.setToDeptIds("28"); // 保洁分队
            } else if (userPermissions.contains("staffOutside:viewSouth")) {
                storeroom.setToDeptIds("26"); // 南分队
            } else if (userPermissions.contains("staffOutside:viewNorth")) {
                storeroom.setToDeptIds("27"); // 北分队
            } else if (userPermissions.contains("staffOutside:viewService")) {
                storeroom.setToDeptIds("29"); // 维修分队
            }

            // 如果拥有以下任意一个或复数权限，就代表只能看到这些物品的权限
            List<String> typeApplicationAuthorityList = new ArrayList<>();
            if (userPermissions.contains("storeroom:view1")) {
                typeApplicationAuthorityList.add("1");
            }
            if (userPermissions.contains("storeroom:view2")) {
                typeApplicationAuthorityList.add("2");
            }
            if (userPermissions.contains("storeroom:view3")) {
                typeApplicationAuthorityList.add("3");
            }
            if (userPermissions.contains("storeroom:view4")) {
                typeApplicationAuthorityList.add("4");
            }
            if (userPermissions.contains("storeroom:view5")) {
                typeApplicationAuthorityList.add("5");
            }
            if (userPermissions.contains("storeroom:view6")) {
                typeApplicationAuthorityList.add("6");
            }
            if (userPermissions.contains("storeroom:view7")) {
                typeApplicationAuthorityList.add("7");
            }
            if (userPermissions.contains("storeroom:view8")) {
                typeApplicationAuthorityList.add("8");
            }
            if (userPermissions.contains("storeroom:view9")) {
                typeApplicationAuthorityList.add("9");
            }
            storeroom.setTypeApplicationAuthority(String.join(",", typeApplicationAuthorityList));
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
            if (!(storeroom.getName() == null || storeroom.getName().isEmpty())) {
                storeroom.setNames(storeroom.getName().replace("，", ",").split(","));
            }
            if (!(storeroom.getIds() == null || storeroom.getIds().isEmpty())) {
                storeroom.setIdArr(storeroom.getIds().replace("，", ",").split(","));
            }
            return this.baseMapper.getStoreroomsDist(page, storeroom);
        } catch (Exception e) {
            log.error("查询物资分配库房异常", e);
            return null;
        }
    }

    @Override
    public IPage<Storeroom> findStorerooms(QueryRequest request, Storeroom storeroom) {
        try {
            LambdaQueryWrapper<Storeroom> queryWrapper = new LambdaQueryWrapper<Storeroom>().eq(Storeroom::getIsIn, "0");

            if (StringUtils.isNotBlank(storeroom.getTypeApplication())) {
                queryWrapper.eq(Storeroom::getTypeApplication, storeroom.getTypeApplication());
            }
            if (StringUtils.isNotBlank(storeroom.getName())) {
                queryWrapper.like(Storeroom::getName, storeroom.getName());
            }
            if (StringUtils.isNotBlank(storeroom.getReceipt())) {
                queryWrapper.like(Storeroom::getReceipt, storeroom.getReceipt());
            }
            if (StringUtils.isNotBlank(storeroom.getCreateTimeFrom()) && StringUtils.isNotBlank(storeroom.getCreateTimeTo())) {
                queryWrapper
                        .ge(Storeroom::getDate, storeroom.getCreateTimeFrom())
                        .le(Storeroom::getDate, storeroom.getCreateTimeTo());
            }

            Page<Storeroom> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("查询库房异常", e);
            return null;
        }
    }

    @Override
    public List<Storeroom> findStoreroomByIdAndParentId(String ids) {
        return baseMapper.findStoreroomByIdAndParentId(ids);
    }


    @Override
    @Transactional
    public void batchInsertStoreroom(List<Storeroom> list) {
        this.saveBatch(list);
    }

    @Override
    @Transactional
    public Long insertStoreroom(Storeroom storeroom) {
        this.baseMapper.insert(storeroom);
        return storeroom.getId();
    }

    @Override
    public void batchUpdateStoreroom(List<Storeroom> list) {
        this.updateBatchById(list);
    }

    @Override
    @Transactional
    public void updateStoreroom(Storeroom storeroom) {
        this.baseMapper.updateById(storeroom);
    }

    @Override
    @Transactional
    public Long updateStoreroomByParentId(Storeroom storeroom, String putOrOut) {
        if (putOrOut == null) {
            this.baseMapper.update(
                    storeroom,
                    Wrappers.<Storeroom>lambdaUpdate()
                            .eq(Storeroom::getParentId, storeroom.getParentId()));
        } else if (putOrOut.equals("1")) { // 更新入库数据
            this.baseMapper.update(
                    storeroom,
                    Wrappers.<Storeroom>lambdaUpdate()
                            .eq(Storeroom::getParentId, storeroom.getParentId())
                            .eq(Storeroom::getIsIn, "1"));
        } else if (putOrOut.equals("2")) { // 更新出库数据
            this.baseMapper.update(
                    storeroom,
                    Wrappers.<Storeroom>lambdaUpdate()
                            .eq(Storeroom::getParentId, storeroom.getParentId())
                            .eq(Storeroom::getIsIn, "2"));
        }
        return null;
    }

    @Override
    @Transactional
    public void updateStoreroomParentIdNULL(Long storeroomId) {
        this.baseMapper.update(
                null,
                Wrappers.<Storeroom>lambdaUpdate()
                        .set(Storeroom::getParentId, null) //把parentId设置成null
                        .eq(Storeroom::getId, storeroomId)
        );
    }

    @Override
    @Transactional
    public void deleteStorerooms(Long storeroomId) {
        this.baseMapper.deleteById(storeroomId);
    }

    @Override
    @Transactional
    public void deletePutStorerooms(String[] storeroomIds) {
        List<String> list = new ArrayList<>(Arrays.asList(storeroomIds));
        String ids = String.join(",", list);
        list.addAll(this.baseMapper.getParentIdByIds(ids));
        this.baseMapper.deleteBatchIds(list);
        this.storeroomPutService.deleteStoreroomPutsByStoreroomId(storeroomIds);
    }

    @Override
    @Transactional
    public void deleteOutStorerooms(String[] storeroomIds) {
        List<String> list = Arrays.asList(storeroomIds);
        this.baseMapper.deleteBatchIds(list);
        // 删除关系
        this.storeroomOutService.deleteStoreroomOutsByStoreroomId(storeroomIds);
    }

    @Override
    public List<Storeroom> getCanteenByDate(String date, String dateRangeFrom, String dateRangeTo) {
        return this.baseMapper.getCanteenByDate(date, dateRangeFrom, dateRangeTo);
    }

    @Override
    public Map<String, List<Storeroom>> getCanteenBySupplierClassification(String dateRangeFrom, String dateRangeTo, String day) {
        List<Storeroom> canteenBySupplierClassification = this.baseMapper.getCanteenBySupplierClassification(dateRangeFrom, dateRangeTo, 1);
        Map<String, BigDecimal> canteenBySupplierClassificationCount = new HashMap<>();
        this.baseMapper.getCanteenBySupplierClassification(dateRangeFrom, dateRangeTo, null)
                .forEach(i -> canteenBySupplierClassificationCount.put(i.getName(), i.getMoney()));
        // List<String> date = this.baseMapper.getDateRange(dateRangeFrom, day);
        Map<String, List<Storeroom>> canteenMap = new HashMap<>();
        canteenBySupplierClassification.forEach(i -> {
            if (canteenMap.containsKey(i.getName())) {
                canteenMap.get(i.getName()).add(i);
            } else {
                ArrayList<Storeroom> objects = Lists.newArrayList();
                objects.add(i);
                canteenMap.put(i.getName(), objects);
            }
        });
        for (List<Storeroom> storeroomList : canteenMap.values()) {
            Storeroom storeroom = new Storeroom();
            storeroom.setDate("合计");
            storeroom.setMoney(canteenBySupplierClassificationCount.get(storeroomList.get(0).getName()));
            storeroomList.add(storeroom);
        }
        /*for (List<Storeroom> storeroomList : canteenMap.values()) {
            String key = storeroomList.get(0).getName();
            List<String> storeroomDate = storeroomList.stream().map(Storeroom::getDate).collect(Collectors.toList());
            for (int i = 0; i < date.size(); i++) {
                if (!storeroomDate.contains(date.get(i))) {
                    Storeroom storeroom = new Storeroom();
                    storeroom.setDate(date.get(i));
                    storeroom.setMoney(BigDecimal.ZERO);
                    storeroomList.add(i, storeroom);
                }
                if ((i + 1) == date.size()) {
                    Storeroom storeroom = new Storeroom();
                    storeroom.setDate("合计");
                    storeroom.setMoney(canteenBySupplierClassificationCount.get(key));
                    storeroomList.add(storeroom);
                }
            }
        }*/
        return canteenMap;
    }

    @Override
    public List<Storeroom> getStoreroomOutItemByParentIdAndId(Storeroom storeroom) {
        return this.baseMapper.getStoreroomOutItemByParentIdAndId(storeroom);
    }

    @Override
    public Storeroom getStoreroomById(String storeroomId) {
        return this.baseMapper.selectById(storeroomId);
    }

    @Override
    public List<Storeroom> getStoreroomsByName(String storeroomName) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<Storeroom>()
                        .eq(Storeroom::getIsIn, 2) // 出库数据
                        .eq(Storeroom::getStatus, 0) // 未分配
                        .in(Storeroom::getTypeApplication, 1, 4) // 1保洁物品 4维修用品
                        .in(Storeroom::getToDeptId, 26, 27, 28, 29) // 北分队 南分队 维修分队 保洁分队
                        .eq(Storeroom::getName, storeroomName) // 名称相符
                        .orderByAsc(Storeroom::getId)
        );
    }

}
