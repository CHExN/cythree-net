package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface StoreroomPutOutMapper extends BaseMapper<StoreroomPutOut> {

    IPage<StoreroomPutOut> findStoreroomPutDetail(Page page, @Param("sPut") StoreroomPutOut storeroomPutOut);

    IPage<StoreroomPutOut> findStoreroomOutDetail(Page page, @Param("sOut") StoreroomPutOut storeroomPutOut);

    List<Long> selectStoreroomPutByStoreroomId(String storeroomIds);

    List<Long> selectStoreroomOutByStoreroomId(String storeroomIds);

    void updateStoreroomPutOutMoney(@Param("id")Long id, @Param("isPut")String isPut);

    /**
     * 查询出库信息，默认条件是只查固定资产
     * @param page 分页信息
     * @param storeroomPutOut 查询条件
     * @return 查询到的出库信息
     */
    IPage<StoreroomPutOut> getStoreroomOutSimplify(Page page, @Param("sOut") StoreroomPutOut storeroomPutOut);


    /**
     * 根据入库单ids查询相应的出库记录信息
     * @param storeroomPutIdsStr 入库单ids
     * @return 出库记录信息
     */
    List<StoreroomPutOut> whetherThereAreStoreroomOutRecords(String storeroomPutIdsStr);

    /**
     * 根据入库单ids更新相应采购申请单状态为“待入库”
     * @param storeroomPutIdsStr
     */
    void updateApplicationProcessByPutId(String storeroomPutIdsStr);

    /**
     * 根据入库单ids更新相应采购申请单内采购物资状态为“未入库”
     * @param storeroomPutIdsStr
     */
    void updatePlanStatusByPutId(String storeroomPutIdsStr);

    /**
     * 根据出库单里面的物资ids把物资数量返还到库房里
     * @param storeroomIdsStr 出库单的物资ids
     */
    void returnStoreroomAmountByStoreroomIds(String storeroomIdsStr);

}
