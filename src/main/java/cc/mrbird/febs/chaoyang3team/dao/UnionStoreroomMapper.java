package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomMapper extends BaseMapper<UnionStoreroom> {

    IPage<UnionStoreroom> findStoreroomsDetail(Page page, @Param("storeroom") UnionStoreroom storeroom);

    IPage<UnionStoreroom> findStoreroomsItemDetails(Page page, @Param("storeroom") UnionStoreroom storeroom);

    List<String> getParentIdByIds(@Param("storeroomIds") String storeroomIds);

    List<UnionStoreroom> findStoreroomByIdAndParentId(String ids);

    /**
     * 根据库房id(ParentId) 与入库物品id(Id) 查询出库的物品信息
     *
     * @param storeroom 查询条件
     * @return 出库物品信息
     */
    List<UnionStoreroom> getStoreroomOutItemByParentIdAndId(UnionStoreroom storeroom);
}
