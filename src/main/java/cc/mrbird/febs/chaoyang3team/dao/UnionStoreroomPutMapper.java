package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomPutMapper extends BaseMapper<UnionStoreroomPut> {
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
    List<UnionStoreroom> getStoreroomsByStoreroomPutId(@Param("StoreroomPutId") String StoreroomPutId);
}
