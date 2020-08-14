package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomService extends IService<UnionStoreroom> {

    /**
     * 库存详情
     *
     * @param request   分页信息
     * @param storeroom 查询条件
     * @return 库存详情数据
     */
    IPage<UnionStoreroom> findStoreroomsDetail(QueryRequest request, UnionStoreroom storeroom, ServletRequest servletRequest);

    /**
     * 出入库物品明细
     *
     * @param request   分页信息
     * @param storeroom 查询条件
     * @return 出入库物品明细数据
     */
    IPage<UnionStoreroom> findStoreroomsItemDetails(QueryRequest request, UnionStoreroom storeroom, ServletRequest servletRequest);

    IPage<UnionStoreroom> findStorerooms(QueryRequest request, UnionStoreroom storeroom);

    List<UnionStoreroom> findStoreroomByIdAndParentId(String ids);

    void batchInsertStoreroom(List<UnionStoreroom> list);

    Long insertStoreroom(UnionStoreroom storeroom);

    void batchUpdateStoreroom(List<UnionStoreroom> list);

    void updateStoreroom(UnionStoreroom storeroom);

    Long updateStoreroomByParentId(UnionStoreroom storeroom, String putOrOut);

    void updateStoreroomParentIdNULL(Long storeroomId);

    void deleteStorerooms(Long storeroomId);

    void deletePutStorerooms(String[] storeroomIds);

    void deleteOutStorerooms(String[] storeroomIds);

    List<UnionStoreroom> getStoreroomOutItemByParentIdAndId(UnionStoreroom storeroom);

    UnionStoreroom getStoreroomById(String storeroomId);

    List<UnionStoreroom> getStoreroomsByName(String storeroomName);

}
