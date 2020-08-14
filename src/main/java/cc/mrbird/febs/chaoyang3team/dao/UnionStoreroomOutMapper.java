package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomOutMapper extends BaseMapper<UnionStoreroomOut> {
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
    List<UnionStoreroom> getStoreroomsByStoreroomOutId(@Param("StoreroomOutId") String StoreroomOutId);
}
