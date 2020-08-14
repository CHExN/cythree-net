package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.UnionStoreroomMapper;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.service.StoreroomOutService;
import cc.mrbird.febs.chaoyang3team.service.StoreroomPutService;
import cc.mrbird.febs.chaoyang3team.service.UnionStoreroomService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
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

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("unionStoreroomService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UnionStoreroomServiceImpl extends ServiceImpl<UnionStoreroomMapper, UnionStoreroom> implements UnionStoreroomService {

    @Autowired
    private StoreroomPutService storeroomPutService;
    @Autowired
    private StoreroomOutService storeroomOutService;

    @Override
    public IPage<UnionStoreroom> findStoreroomsDetail(QueryRequest request, UnionStoreroom storeroom, ServletRequest servletRequest) {
        try {
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
    public IPage<UnionStoreroom> findStoreroomsItemDetails(QueryRequest request, UnionStoreroom storeroom, ServletRequest servletRequest) {
        try {
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
    public IPage<UnionStoreroom> findStorerooms(QueryRequest request, UnionStoreroom storeroom) {
        try {
            LambdaQueryWrapper<UnionStoreroom> queryWrapper = new LambdaQueryWrapper<UnionStoreroom>().eq(UnionStoreroom::getIsIn, "0");

            if (StringUtils.isNotBlank(storeroom.getTypeApplication())) {
                queryWrapper.eq(UnionStoreroom::getTypeApplication, storeroom.getTypeApplication());
            }
            if (StringUtils.isNotBlank(storeroom.getName())) {
                queryWrapper.like(UnionStoreroom::getName, storeroom.getName());
            }
            if (StringUtils.isNotBlank(storeroom.getReceipt())) {
                queryWrapper.like(UnionStoreroom::getReceipt, storeroom.getReceipt());
            }
            if (StringUtils.isNotBlank(storeroom.getCreateTimeFrom()) && StringUtils.isNotBlank(storeroom.getCreateTimeTo())) {
                queryWrapper
                        .ge(UnionStoreroom::getDate, storeroom.getCreateTimeFrom())
                        .le(UnionStoreroom::getDate, storeroom.getCreateTimeTo());
            }

            Page<UnionStoreroom> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("查询库房异常", e);
            return null;
        }
    }

    @Override
    public List<UnionStoreroom> findStoreroomByIdAndParentId(String ids) {
        return baseMapper.findStoreroomByIdAndParentId(ids);
    }


    @Override
    @Transactional
    public void batchInsertStoreroom(List<UnionStoreroom> list) {
        this.saveBatch(list);
    }

    @Override
    @Transactional
    public Long insertStoreroom(UnionStoreroom storeroom) {
        this.baseMapper.insert(storeroom);
        return storeroom.getId();
    }

    @Override
    public void batchUpdateStoreroom(List<UnionStoreroom> list) {
        this.updateBatchById(list);
    }

    @Override
    @Transactional
    public void updateStoreroom(UnionStoreroom storeroom) {
        this.baseMapper.updateById(storeroom);
    }

    @Override
    @Transactional
    public Long updateStoreroomByParentId(UnionStoreroom storeroom, String putOrOut) {
        if (putOrOut == null) {
            this.baseMapper.update(
                    storeroom,
                    Wrappers.<UnionStoreroom>lambdaUpdate()
                            .eq(UnionStoreroom::getParentId, storeroom.getParentId()));
        } else if (putOrOut.equals("1")) { // 更新入库数据
            this.baseMapper.update(
                    storeroom,
                    Wrappers.<UnionStoreroom>lambdaUpdate()
                            .eq(UnionStoreroom::getParentId, storeroom.getParentId())
                            .eq(UnionStoreroom::getIsIn, "1"));
        } else if (putOrOut.equals("2")) { // 更新出库数据
            this.baseMapper.update(
                    storeroom,
                    Wrappers.<UnionStoreroom>lambdaUpdate()
                            .eq(UnionStoreroom::getParentId, storeroom.getParentId())
                            .eq(UnionStoreroom::getIsIn, "2"));
        }
        return null;
    }

    @Override
    @Transactional
    public void updateStoreroomParentIdNULL(Long storeroomId) {
        this.baseMapper.update(
                null,
                Wrappers.<UnionStoreroom>lambdaUpdate()
                        .set(UnionStoreroom::getParentId, null) //把parentId设置成null
                        .eq(UnionStoreroom::getId, storeroomId)
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
    public List<UnionStoreroom> getStoreroomOutItemByParentIdAndId(UnionStoreroom storeroom) {
        return this.baseMapper.getStoreroomOutItemByParentIdAndId(storeroom);
    }

    @Override
    public UnionStoreroom getStoreroomById(String storeroomId) {
        return this.baseMapper.selectById(storeroomId);
    }

    @Override
    public List<UnionStoreroom> getStoreroomsByName(String storeroomName) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<UnionStoreroom>()
                        .eq(UnionStoreroom::getIsIn, 2) // 出库数据
                        .eq(UnionStoreroom::getStatus, 0) // 未分配
                        .in(UnionStoreroom::getTypeApplication, 1, 4) // 1保洁物品 4维修用品
                        .in(UnionStoreroom::getToDeptId, 26, 27, 28, 29) // 北分队 南分队 维修分队 保洁分队
                        .eq(UnionStoreroom::getName, storeroomName) // 名称相符
                        .orderByAsc(UnionStoreroom::getId)
        );
    }

}
