package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface StoreroomOutMapper extends BaseMapper<StoreroomOut> {

    /**
     * 根据出库记录ID查找对应库房ID
     *
     * @return 库房ID
     */
    List<String> getStoreroomIdsByStoreroomOutIds(@Param("StoreroomOutIds") String StoreroomOutIds);

    /**
     * 根据出库记录ID查找对应库房详情
     *
     * @return 库房详情
     */
    List<Storeroom> getStoreroomsByStoreroomOutId(@Param("StoreroomOutId") String StoreroomOutId);

}
