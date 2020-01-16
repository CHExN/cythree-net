package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StoreroomMapper;
import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.StoreroomOutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public IPage<Storeroom> findStoreroomsDetail(QueryRequest request, Storeroom storeroom) {
        try {
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.findStoreroomsDetail(page, storeroom);
        } catch (Exception e) {
            log.error("查询库房异常", e);
            return null;
        }
    }

    @Override
    public IPage<Storeroom> findStoreroomsItemDetails(QueryRequest request, Storeroom storeroom) {
        try {
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.findStoreroomsItemDetails(page, storeroom);
        } catch (Exception e) {
            log.error("查询出入库物品明细异常", e);
            return null;
        }
    }


    @Override
    public IPage<Storeroom> getStoreroomsDist(QueryRequest request, Storeroom storeroom) {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        User user = userManager.getUser(JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication"))));
//        storeroom.setToDeptId(user.getDeptId());

//        storeroom.setToDeptIds("26,27,28,29"); // 南分队，北分队，保洁分队，维修分队

        try {
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
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
        this.storeroomOutService.deleteStoreroomOutsByStoreroomId(storeroomIds);
    }

    @Override
    public List<Storeroom> getOfficeSuppliesByDate(String date) {
        return this.baseMapper.getOfficeSuppliesByDate(date);
    }

    @Override
    public List<Storeroom> getCanteenByDate(String date) {
        return this.baseMapper.getCanteenByDate(date);
    }

    @Override
    public List<Storeroom> getStoreroomOutItemByParentIdAndId(Storeroom storeroom) {
        return this.baseMapper.getStoreroomOutItemByParentIdAndId(storeroom);
    }

}
