package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPutOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomPutOutMapper extends BaseMapper<UnionStoreroomPutOut> {

    IPage<UnionStoreroomPutOut> findStoreroomPutDetail(Page page, @Param("sPut") UnionStoreroomPutOut storeroomPutOut);

    IPage<UnionStoreroomPutOut> findStoreroomOutDetail(Page page, @Param("sOut") UnionStoreroomPutOut storeroomPutOut);

    List<Long> selectStoreroomPutByStoreroomId(String storeroomIds);

    List<Long> selectStoreroomOutByStoreroomId(String storeroomIds);

    void updateStoreroomPutOutMoney(@Param("id")Long id, @Param("isPut")String isPut);

    /**
     * 根据入库单ids查询相应的出库记录信息
     * @param storeroomPutIdsStr 入库单ids
     * @return 出库记录信息
     */
    List<UnionStoreroomPutOut> whetherThereAreStoreroomOutRecords(String storeroomPutIdsStr);

    /**
     * 根据出库单里面的物资ids把物资数量返还到库房里
     * @param storeroomIdsStr 出库单的物资ids
     */
    void returnStoreroomAmountByStoreroomIds(String storeroomIdsStr);
}
