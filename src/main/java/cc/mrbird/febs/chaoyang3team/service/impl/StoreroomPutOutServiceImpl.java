package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.StoreroomPutOutMapper;
import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomOut;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPut;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.chaoyang3team.service.*;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.manager.UserManager;
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
import java.util.*;

/**
 * @author CHExN
 */
@Slf4j
@Service("storeroomPutOutService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreroomPutOutServiceImpl extends ServiceImpl<StoreroomPutOutMapper, StoreroomPutOut> implements StoreroomPutOutService {

    @Autowired
    private StoreroomPutService storeroomPutService;
    @Autowired
    private StoreroomOutService storeroomOutService;
    @Autowired
    private StoreroomService storeroomService;
    @Autowired
    private WcStoreroomService wcStoreroomService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<StoreroomPutOut> findStoreroomPutDetail(QueryRequest request, StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
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
        storeroomPutOut.setTypeApplicationAuthority(String.join(",", typeApplicationAuthorityList));
        try {
            storeroomPutOut.setIsPut("1");
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "date", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.findStoreroomPutDetail(page, storeroomPutOut);
        } catch (Exception e) {
            log.error("查询入库单信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<StoreroomPutOut> findStoreroomOutDetail(QueryRequest request, StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
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
        storeroomPutOut.setTypeApplicationAuthority(String.join(",", typeApplicationAuthorityList));
        try {
            storeroomPutOut.setIsPut("2");
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "date", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.findStoreroomOutDetail(page, storeroomPutOut);
        } catch (Exception e) {
            log.error("查询出库单信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<StoreroomPutOut> getStoreroomOutSimplify(QueryRequest request, StoreroomPutOut storeroomPutOut) {
        try {
            Page<StoreroomPutOut> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, true);
            return this.baseMapper.getStoreroomOutSimplify(page, storeroomPutOut);
        } catch (Exception e) {
            log.error("查询出库单信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createStoreroomPut(StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        storeroomPutOut.setCreateTime(LocalDateTime.now());
        // 操作账号
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        storeroomPutOut.setUsername(username);
        // 计算总额
        BigDecimal countMoney = BigDecimal.valueOf(0);

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<Storeroom> storeroomList = (List<Storeroom>) JSONArray.toCollection(jsonArray, Storeroom.class);
        for (Storeroom storeroom : storeroomList) {
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

        storeroomPutOut.setIsPut("1"); // 入库数据
        storeroomPutOut.setMoney(countMoney);
        this.baseMapper.insert(storeroomPutOut);

        // 插入 关系
        List<StoreroomPut> storeroomPutList = new ArrayList<>();
        for (Storeroom storeroom : storeroomList) {
            storeroomPutList.add(new StoreroomPut(storeroomPutOut.getId(), storeroom.getId()));
        }
        this.storeroomPutService.batchInsertStoreroomPut(storeroomPutList);
    }


    @Override
    @Transactional
    public void createStoreroomOut(StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        storeroomPutOut.setCreateTime(LocalDateTime.now());
        // 操作账号
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        storeroomPutOut.setUsername(username);
        // 计算总额
        BigDecimal countMoney = BigDecimal.valueOf(0);

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<Storeroom> storeroomList = (List<Storeroom>) JSONArray.toCollection(jsonArray, Storeroom.class);

        for (Storeroom storeroom : storeroomList) {
            countMoney = countMoney.add((storeroom.getAmount().multiply(storeroom.getMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));

            // 库房现存物品数量 减去 出库时物品数量
            final BigDecimal RESULT = storeroom.getStoreroomCount().subtract(storeroom.getAmount());

            // 如果等于0 删除对应库存 否则 更新库存数量
//            if (RESULT.compareTo(BigDecimal.ZERO) == 0) {
//                this.storeroomService.deleteStorerooms(storeroom.getId());
//            } else {
            Storeroom upStoreroom = new Storeroom();
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
        List<StoreroomOut> storeroomOutList = new ArrayList<>();
        for (Storeroom storeroom : storeroomList) {
            storeroomOutList.add(new StoreroomOut(storeroomPutOut.getId(), storeroom.getId()));
        }
        this.storeroomOutService.batchInsertStoreroomOut(storeroomOutList);
    }

    @Override
    @Transactional
    public void updateStoreroom(StoreroomPutOut storeroomPutOut) {
        storeroomPutOut.setModifyTime(LocalDateTime.now());

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<Storeroom> storeroomPutList = (List<Storeroom>) JSONArray.toCollection(jsonArray, Storeroom.class);
        List<Storeroom> storeroomList = new ArrayList<>();

        StringJoiner parentIdJoiner = new StringJoiner(",");
        storeroomPutList.forEach(s -> {
            // 记录所关联的库房id
            parentIdJoiner.add(s.getParentId() + "");

            // 库房数据
            Storeroom storeroom = new Storeroom();
            storeroom.setId(s.getParentId());
            storeroom.setName(s.getName());
            storeroom.setType(s.getType());
            storeroom.setUnit(s.getUnit());
            storeroom.setMoney(s.getMoney());
            storeroom.setReceipt(s.getReceipt());
            storeroom.setRemark(s.getRemark());
            // 如果修改的是入库数据，就把库房数据的日期和供应商也连同修改
            if (storeroomPutOut.getIsPut().equals("1")) {
                storeroom.setDate(storeroomPutOut.getDate());
//                storeroom.setSupplier(storeroomPutOut.getSupplier());
            }
            storeroomList.add(storeroom);

            // 出入库库数据
            s.setAmount(null);
            s.setIsIn(null);
            s.setStatus(null);
            s.setAmountDist(null);
            s.setDate(storeroomPutOut.getDate());
            s.setToDeptId(storeroomPutOut.getToDeptId());
            // 根据parentId更新出入库允许更新的项
            this.storeroomService.updateStoreroomByParentId(s, storeroomPutOut.getIsPut());
            s.setDate(null);
            s.setToDeptId(null);
            this.storeroomService.updateStoreroomByParentId(s, storeroomPutOut.getIsPut().equals("1") ? "2" : "1"); // 如果上面是1那这里就是2，否则反之
        });
        // 批量更新对应的库房数据
        this.storeroomService.batchUpdateStoreroom(storeroomList);

        // 查找库房id相关的所有数据(库房数据与出入库数据)
        String parentIds = parentIdJoiner.toString();
        List<Storeroom> storeroomByIdAndParentId = this.storeroomService.findStoreroomByIdAndParentId(parentIds);

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
    public void deleteStoreroomPuts(String[] StoreroomPutIds) {
        List<String> list = Arrays.asList(StoreroomPutIds);
        this.baseMapper.deleteBatchIds(list);
        List<String> storeroomIdList = this.storeroomPutService.getStoreroomIdsByPutIds(StoreroomPutIds);
        if (!storeroomIdList.isEmpty()) {
            this.storeroomService.deletePutStorerooms(storeroomIdList.toArray(new String[0]));
        }
    }

    @Override
    @Transactional
    public void deleteStoreroomOuts(String[] StoreroomOutIds) {
        List<String> list = Arrays.asList(StoreroomOutIds);
        this.baseMapper.deleteBatchIds(list);
        List<String> storeroomIdList = this.storeroomOutService.getStoreroomIdsByOutIds(StoreroomOutIds);
        if (!storeroomIdList.isEmpty()) {
            this.storeroomService.deleteOutStorerooms(storeroomIdList.toArray(new String[0]));
            this.wcStoreroomService.deleteWcStoreroomsByStoreroomId(storeroomIdList.toArray(new String[0]));
        }
    }

    @Override
    @Transactional
    public void inOut(StoreroomPutOut storeroomPutOut, ServletRequest servletRequest) {
        storeroomPutOut.setCreateTime(LocalDateTime.now());
        // 操作账号
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        storeroomPutOut.setUsername(username);
        // 计算总额
        BigDecimal countMoney = BigDecimal.valueOf(0);

        JSONArray jsonArray = JSONArray.fromObject(storeroomPutOut.getStoreroomList());
        List<Storeroom> storeroomList = (List<Storeroom>) JSONArray.toCollection(jsonArray, Storeroom.class);
        List<Storeroom> putStoreroomList = (List<Storeroom>) JSONArray.toCollection(jsonArray, Storeroom.class);
        List<Storeroom> outStoreroomList = (List<Storeroom>) JSONArray.toCollection(jsonArray, Storeroom.class);

        // 先弄库房
        storeroomList.forEach(storeroom -> { // 入库房
            storeroom.setAmount(BigDecimal.valueOf(0));
            storeroom.setDate(storeroomPutOut.getDate());
            storeroom.setTypeApplication(storeroomPutOut.getTypeApplication());
            storeroom.setSupplier(storeroomPutOut.getSupplier());
            storeroom.setIsIn("0"); // 库房数据
        });
        this.storeroomService.batchInsertStoreroom(storeroomList);
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
        List<StoreroomOut> storeroomOutList = new ArrayList<>();
        for (Storeroom storeroom : outStoreroomList) {
            storeroomOutList.add(new StoreroomOut(storeroomPutOut.getId(), storeroom.getId()));
        }

        storeroomPutOut.setToDeptId(null);
        storeroomPutOut.setIsPut("1"); // 入库数据
        this.baseMapper.insert(storeroomPutOut);
        // 插入 关系
        List<StoreroomPut> storeroomPutList = new ArrayList<>();
        for (Storeroom storeroom : putStoreroomList) {
            storeroomPutList.add(new StoreroomPut(storeroomPutOut.getId(), storeroom.getId()));
        }

        this.storeroomOutService.batchInsertStoreroomOut(storeroomOutList);
        this.storeroomPutService.batchInsertStoreroomPut(storeroomPutList);
    }
}
