package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
public interface StoreroomPutMapper extends BaseMapper<StoreroomPut> {

    /**
     * 根据入库记录ID查找对应库房ID
     *
     * @return 库房ID
     */
    List<String> getStoreroomIdsByStoreroomPutIds(@Param("StoreroomPutIds") String StoreroomPutIds);

    /**
     * 根据入库记录ID查找对应库房详情
     *
     * @return 库房详情
     */
    List<Storeroom> getStoreroomsByStoreroomPutId(@Param("StoreroomPutId") String StoreroomPutId);

    /**
     * 入库类别占比
     *
     * @param date 年月
     * @return 入库类别占比
     */
    List<Map<String, Object>> findStoreroomPutTypeApplicationProportion(String date);

}
