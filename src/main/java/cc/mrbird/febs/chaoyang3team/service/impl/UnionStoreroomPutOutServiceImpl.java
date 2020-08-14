package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.UnionStoreroomPutOutMapper;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomOut;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPut;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.*;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author CHExN
 */
@Slf4j
@Service("unionStoreroomPutOutService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UnionStoreroomPutOutServiceImpl extends ServiceImpl<UnionStoreroomPutOutMapper, UnionStoreroomPutOut> implements UnionStoreroomPutOutService {

    @Autowired
    private UnionStoreroomPutService storeroomPutService;
    @Autowired
    private UnionStoreroomOutService storeroomOutService;
    @Autowired
    private UnionStoreroomService storeroomService;
    @Autowired
    private PlanService planService;

    @Override
    public IPage<UnionStoreroomPutOut> findStoreroomPutDetail(QueryRequest request, UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        try {
            storeroomPutOut.setIsPut("1");
            Page<UnionStoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "date", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.findStoreroomPutDetail(page, storeroomPutOut);
        } catch (Exception e) {
            log.error("查询入库单信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<UnionStoreroomPutOut> findStoreroomOutDetail(QueryRequest request, UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        try {
            storeroomPutOut.setIsPut("2");
            Page<UnionStoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "date", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.findStoreroomOutDetail(page, storeroomPutOut);
        } catch (Exception e) {
            log.error("查询出库单信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createStoreroomPut(UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        storeroomPutOut.setCreateTime(LocalDateTime.now());
        // 操作账号
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        storeroomPutOut.setUsername(username);
        // 计算总额
        BigDecimal countMoney = BigDecimal.valueOf(0);
        List<Long> planIds = Lists.newArrayList();

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<UnionStoreroom> storeroomList = (List<UnionStoreroom>) JSONArray.toCollection(jsonArray, UnionStoreroom.class);
        for (UnionStoreroom storeroom : storeroomList) {
            if (storeroom.getId() != null) {
                planIds.add(storeroom.getId());
            }
            countMoney = countMoney.add((storeroom.getAmount().multiply(storeroom.getMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));

            storeroom.setDate(storeroomPutOut.getDate());
            storeroom.setTypeApplication(storeroomPutOut.getTypeApplication());
            storeroom.setSupplier(storeroomPutOut.getSupplier());
            storeroom.setIsIn("0"); // 库房数据

            // 数据要插两遍，这里插入的是库房的数据，返回插入成功后的id，与待会儿批量插入的入库数据进行绑定
            storeroom.setParentId(this.storeroomService.insertStoreroom(storeroom));
            storeroom.setIsIn("1"); // 入库数据
        }
        this.storeroomService.batchInsertStoreroom(storeroomList);
        this.planService.updatePlanStatus(StringUtils.join(planIds, ","));

        storeroomPutOut.setIsPut("1"); // 入库数据
        storeroomPutOut.setMoney(countMoney);
        this.baseMapper.insert(storeroomPutOut);

        // 插入 关系
        List<UnionStoreroomPut> storeroomPutList = new ArrayList<>();
        for (UnionStoreroom storeroom : storeroomList) {
            storeroomPutList.add(new UnionStoreroomPut(storeroomPutOut.getId(), storeroom.getId()));
        }
        this.storeroomPutService.batchInsertStoreroomPut(storeroomPutList);
    }


    @Override
    @Transactional
    public void createStoreroomOut(UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        storeroomPutOut.setCreateTime(LocalDateTime.now());
        // 操作账号
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        storeroomPutOut.setUsername(username);
        // 计算总额
        BigDecimal countMoney = BigDecimal.valueOf(0);

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<UnionStoreroom> storeroomList = (List<UnionStoreroom>) JSONArray.toCollection(jsonArray, UnionStoreroom.class);

        for (UnionStoreroom storeroom : storeroomList) {
            countMoney = countMoney.add((storeroom.getAmount().multiply(storeroom.getMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));

            // 库房现存物品数量 减去 出库时物品数量
            final BigDecimal RESULT = storeroom.getStoreroomCount().subtract(storeroom.getAmount());

            // 如果等于0 删除对应库存 否则 更新库存数量
//            if (RESULT.compareTo(BigDecimal.ZERO) == 0) {
//                this.storeroomService.deleteStorerooms(storeroom.getId());
//            } else {
            UnionStoreroom upStoreroom = new UnionStoreroom();
            upStoreroom.setId(storeroom.getId());
            upStoreroom.setAmount(RESULT);
            this.storeroomService.updateStoreroom(upStoreroom);
//            }
            // 要插入新数据 把旧的不需要的值设为null
            storeroom.setParentId(storeroom.getId());
            storeroom.setId(null);
            storeroom.setDate(storeroomPutOut.getDate());
            storeroom.setToDeptId(storeroomPutOut.getToDeptId()); // 出库到哪个部门id
            storeroom.setIsIn("2"); // 出库数据
            storeroom.setAmountDist(storeroom.getAmount());
        }
        this.storeroomService.batchInsertStoreroom(storeroomList);

        storeroomPutOut.setTypeApplication(storeroomList.get(0).getTypeApplication());
        storeroomPutOut.setSupplier(storeroomList.get(0).getSupplier());
        storeroomPutOut.setIsPut("2"); // 出库数据
        storeroomPutOut.setMoney(countMoney);
        this.baseMapper.insert(storeroomPutOut);

        // 插入 关系
        List<UnionStoreroomOut> storeroomOutList = new ArrayList<>();
        for (UnionStoreroom storeroom : storeroomList) {
            storeroomOutList.add(new UnionStoreroomOut(storeroomPutOut.getId(), storeroom.getId()));
        }
        this.storeroomOutService.batchInsertStoreroomOut(storeroomOutList);
    }

    @Override
    @Transactional
    public void updateStoreroom(UnionStoreroomPutOut storeroomPutOut) {
        storeroomPutOut.setModifyTime(LocalDateTime.now());

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<UnionStoreroom> storeroomPutOutList = (List<UnionStoreroom>) JSONArray.toCollection(jsonArray, UnionStoreroom.class);
        List<UnionStoreroom> storeroomList = new ArrayList<>();

        StringJoiner parentIdJoiner = new StringJoiner(",");
        storeroomPutOutList.forEach(s -> {
            // 记录所关联的库房id
            parentIdJoiner.add(s.getParentId().toString());

            // 库房数据
            UnionStoreroom storeroom = new UnionStoreroom();
            storeroom.setId(s.getParentId());
            storeroom.setName(s.getName());
            storeroom.setType(s.getType());
            storeroom.setUnit(s.getUnit());
            storeroom.setMoney(s.getMoney());
            storeroom.setReceipt(s.getReceipt());
            storeroom.setRemark(s.getRemark());
            storeroom.setTypeApplication(storeroomPutOut.getTypeApplication());
            storeroom.setSupplier(storeroomPutOut.getSupplier());

            // 如果修改的是入库数据，就把库房数据的日期也连同修改
            if (storeroomPutOut.getIsPut().equals("1")) {
                storeroom.setDate(storeroomPutOut.getDate());
            }
            storeroomList.add(storeroom);

            // 出入库库数据
            s.setAmount(null);
            s.setIsIn(null);
            s.setStatus(null);
            s.setAmountDist(null);
            s.setDate(null);
            s.setToDeptId(null);
            s.setTypeApplication(storeroomPutOut.getTypeApplication());
            s.setSupplier(storeroomPutOut.getSupplier());

            // 根据parentId更新出入库允许更新的项
            this.storeroomService.updateStoreroomByParentId(s, null);

            // 根据storeroom自身的id来更新与出入库单同步的项（日期、出库部门、物资类别、供应商）
            UnionStoreroom updateStoreroom = new UnionStoreroom();
            updateStoreroom.setId(s.getId());
            updateStoreroom.setDate(storeroomPutOut.getDate());
            updateStoreroom.setToDeptId(storeroomPutOut.getToDeptId());
            /*updateStoreroom.setTypeApplication(storeroomPutOut.getTypeApplication());
            updateStoreroom.setSupplier(storeroomPutOut.getSupplier());*/
            this.storeroomService.updateStoreroom(updateStoreroom);
        });

        // 批量更新对应的库房数据
        this.storeroomService.batchUpdateStoreroom(storeroomList);

        // 查找库房id相关的所有数据(库房数据与出入库数据)
        String parentIds = parentIdJoiner.toString();
        List<UnionStoreroom> storeroomByIdAndParentId = this.storeroomService.findStoreroomByIdAndParentId(parentIds);

        // 整合id
        StringJoiner idJoiner = new StringJoiner(",");
        storeroomByIdAndParentId.forEach(s -> idJoiner.add(s.getId() + ""));
        String ids = idJoiner.toString();

        //再查询所有被修改后的storeroom_put_out的id
        List<Long> storeroomPutIdList = baseMapper.selectStoreroomPutByStoreroomId(ids);
        List<Long> storeroomOutIdList = baseMapper.selectStoreroomOutByStoreroomId(ids);
        //根据storeroom_put_out的id从新计算总额
        storeroomPutIdList.forEach(id -> baseMapper.updateStoreroomPutOutMoney(id, "1"));
        storeroomOutIdList.forEach(id -> baseMapper.updateStoreroomPutOutMoney(id, "2"));

        this.updateById(storeroomPutOut);
    }

    @Override
    @Transactional
    public Map<String, Object> deleteStoreroomPuts(String[] storeroomPutIds) {
        Map<String, Object> data = new HashMap<>();
        String storeroomPutIdsStr = StringUtils.join(storeroomPutIds, ",");
        // 这里先根据入库单ids查询物资有没有被出库，只要有被出库的物资，那这条入库单就不能被删除，
        // 因为还存留着这条入库单的库房物资的出库记录，如果入库单被删，那出库单里的物资和库房里的物资是查询不到来源的，不符合规范
        List<UnionStoreroomPutOut> outRecords = this.baseMapper.whetherThereAreStoreroomOutRecords(storeroomPutIdsStr);
        if (!outRecords.isEmpty()) {
            data.put("status", 0);
            data.put("message", "删除失败");
            data.put("outRecords", outRecords);
            return data;
        }

        List<String> list = Arrays.asList(storeroomPutIds);
        // 删除入库单
        this.baseMapper.deleteBatchIds(list);
        // 根据入库单ids获取物资信息
        List<String> storeroomIdList = this.storeroomPutService.getStoreroomIdsByPutIds(storeroomPutIds);
        if (!storeroomIdList.isEmpty()) {
            // 删除物资和关系
            this.storeroomService.deletePutStorerooms(storeroomIdList.toArray(new String[0]));
        }
        data.put("status", 1);
        data.put("message", "删除成功");
        return data;
    }

    @Override
    @Transactional
    public void deleteStoreroomOuts(String[] storeroomOutIds) {
        List<String> list = Arrays.asList(storeroomOutIds);
        this.baseMapper.deleteBatchIds(list);
        List<String> storeroomIdList = this.storeroomOutService.getStoreroomIdsByOutIds(storeroomOutIds);
        // 这里根据获取到的出库单物资id 获取里面的物资信息，把相应要删除的出库物资的数量返还给库房物资
        String[] storeroomIds = storeroomIdList.toArray(new String[0]);
        if (!storeroomIdList.isEmpty()) {
            // 返还物资数量到库房里
            this.baseMapper.returnStoreroomAmountByStoreroomIds(StringUtils.join(storeroomIds, ","));
            // 删除出库单物资和关系
            this.storeroomService.deleteOutStorerooms(storeroomIds);
        }
    }

    @Override
    @Transactional
    public void inOut(UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        storeroomPutOut.setCreateTime(LocalDateTime.now());
        // 操作账号
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        storeroomPutOut.setUsername(username);
        // 计算总额
        BigDecimal countMoney = BigDecimal.valueOf(0);
        List<Long> planIds = Lists.newArrayList();

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<UnionStoreroom> storeroomList = (List<UnionStoreroom>) JSONArray.toCollection(jsonArray, UnionStoreroom.class);
        List<UnionStoreroom> putStoreroomList = (List<UnionStoreroom>) JSONArray.toCollection(jsonArray, UnionStoreroom.class);
        List<UnionStoreroom> outStoreroomList = (List<UnionStoreroom>) JSONArray.toCollection(jsonArray, UnionStoreroom.class);

        // 先弄库房
        storeroomList.forEach(storeroom -> { // 入库房
            if (storeroom.getId() != null) {
                planIds.add(storeroom.getId());
            }
            storeroom.setAmount(BigDecimal.valueOf(0));
            storeroom.setDate(storeroomPutOut.getDate());
            storeroom.setTypeApplication(storeroomPutOut.getTypeApplication());
            storeroom.setSupplier(storeroomPutOut.getSupplier());
            storeroom.setIsIn("0"); // 库房数据
        });
        this.storeroomService.batchInsertStoreroom(storeroomList);
        this.planService.updatePlanStatus(StringUtils.join(planIds, ","));

        for (int i = 0; i < putStoreroomList.size(); i++) { // 入库
            countMoney = countMoney.add((putStoreroomList.get(i).getAmount().multiply(putStoreroomList.get(i).getMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));

            putStoreroomList.get(i).setParentId(storeroomList.get(i).getId());
            putStoreroomList.get(i).setDate(storeroomPutOut.getDate());
            putStoreroomList.get(i).setTypeApplication(storeroomPutOut.getTypeApplication());
            putStoreroomList.get(i).setSupplier(storeroomPutOut.getSupplier());
            putStoreroomList.get(i).setIsIn("1"); // 入库数据
        }
        for (int i = 0; i < outStoreroomList.size(); i++) { // 出库
            outStoreroomList.get(i).setParentId(storeroomList.get(i).getId());
            outStoreroomList.get(i).setToDeptId(storeroomPutOut.getToDeptId());
            outStoreroomList.get(i).setDate(storeroomPutOut.getDate());
            outStoreroomList.get(i).setTypeApplication(storeroomPutOut.getTypeApplication());
            outStoreroomList.get(i).setSupplier(storeroomPutOut.getSupplier());
            outStoreroomList.get(i).setIsIn("2"); // 出库数据
            outStoreroomList.get(i).setAmountDist(outStoreroomList.get(i).getAmount());
        }
        this.storeroomService.batchInsertStoreroom(putStoreroomList);
        this.storeroomService.batchInsertStoreroom(outStoreroomList);

        storeroomPutOut.setMoney(countMoney);
        storeroomPutOut.setIsPut("2"); // 出库数据
        this.baseMapper.insert(storeroomPutOut);
        // 插入 关系
        List<UnionStoreroomOut> storeroomOutList = new ArrayList<>();
        for (UnionStoreroom storeroom : outStoreroomList) {
            storeroomOutList.add(new UnionStoreroomOut(storeroomPutOut.getId(), storeroom.getId()));
        }

        storeroomPutOut.setToDeptId(null);
        storeroomPutOut.setIsPut("1"); // 入库数据
        this.baseMapper.insert(storeroomPutOut);
        // 插入 关系
        List<UnionStoreroomPut> storeroomPutList = new ArrayList<>();
        for (UnionStoreroom storeroom : putStoreroomList) {
            storeroomPutList.add(new UnionStoreroomPut(storeroomPutOut.getId(), storeroom.getId()));
        }

        this.storeroomOutService.batchInsertStoreroomOut(storeroomOutList);
        this.storeroomPutService.batchInsertStoreroomPut(storeroomPutList);
    }

}
