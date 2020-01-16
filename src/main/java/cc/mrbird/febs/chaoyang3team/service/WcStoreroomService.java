package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcStoreroom;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.math.BigDecimal;

/**
 * @author CHExN
 */
public interface WcStoreroomService extends IService<WcStoreroom> {

    IPage<WcStoreroom> findWcStoreroomDetail(QueryRequest request, WcStoreroom wcStoreroom, ServletRequest servletRequest);

    IPage<WcStoreroom> findWcStoreroomDetail(QueryRequest request, WcStoreroom wcStoreroom);

    void deleteWcStoreroomsByWcId(String[] wcIds);

    void deleteWcStoreroomsByStoreroomId(String[] storeroomIds);

    void createWcStoreroom(String wcStoreroomStr, BigDecimal amountDist);

}
