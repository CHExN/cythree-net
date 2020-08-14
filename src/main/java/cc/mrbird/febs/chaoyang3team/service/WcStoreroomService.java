package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcStoreroom;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author CHExN
 */
public interface WcStoreroomService extends IService<WcStoreroom> {

    IPage<WcStoreroom> findWcStoreroomDetail(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest);

    void deleteWcStorerooms(String[] wcStoreroomIds);

    void deleteAllWcStorerooms(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest);

    /**
     * 根据公厕ids删除所有分配记录
     * 此删除没有进行物资归还操作，慎用
     * @param wcIds 公厕ids
     */
    void deleteWcStoreroomsByWcId(String[] wcIds);

    /**
     * 根据物资ids删除所有分配记录
     * 此删除没有进行物资归还操作，慎用
     * @param storeroomIds 物资ids
     */
    void deleteWcStoreroomsByStoreroomId(String[] storeroomIds);

    void createWcStoreroom(String wcStoreroomStr, BigDecimal amountDist, ServletRequest servletRequest);

    void batchInsertWcStoreroom(List<WcStoreroom> wcStoreroomList);

    List<String> getDeleteWcStoreroomIds(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest);

}
