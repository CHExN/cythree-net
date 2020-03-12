package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author CHExN
 */
public interface StoreroomService extends IService<Storeroom> {

    /**
     * 库存详情
     *
     * @param request   分页信息
     * @param storeroom 查询条件
     * @return 库存详情数据
     */
    IPage<Storeroom> findStoreroomsDetail(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest);

    /**
     * 出入库物品明细
     *
     * @param request   分页信息
     * @param storeroom 查询条件
     * @return 出入库物品明细数据
     */
    IPage<Storeroom> findStoreroomsItemDetails(QueryRequest request, Storeroom storeroom, ServletRequest servletRequest);

    IPage<Storeroom> getStoreroomsDist(QueryRequest request, Storeroom storeroom);

    IPage<Storeroom> findStorerooms(QueryRequest request, Storeroom storeroom);

    List<Storeroom> findStoreroomByIdAndParentId(String ids);

    void batchInsertStoreroom(List<Storeroom> list);

    Long insertStoreroom(Storeroom storeroom);

    void batchUpdateStoreroom(List<Storeroom> list);

    void updateStoreroom(Storeroom storeroom);

    Long updateStoreroomByParentId(Storeroom storeroom, String putOrOut);

    void updateStoreroomParentIdNULL(Long storeroomId);

    void deleteStorerooms(Long storeroomId);

    void deletePutStorerooms(String[] storeroomIds);

    void deleteOutStorerooms(String[] storeroomIds);

    List<Storeroom> getOfficeSuppliesByDate(String date);

    List<Storeroom> getCanteenByDate(String date);

    List<Storeroom> getStoreroomOutItemByParentIdAndId(Storeroom storeroom);

}
