package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcStoreroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcStoreroomMapper extends BaseMapper<WcStoreroom> {

    IPage<WcStoreroom> findWcStoreroomDetail(Page page, @Param("wcStoreroom") WcStoreroom wcStoreroom);

    List<String> getDeleteWcStoreroomIds(@Param("wcStoreroom") WcStoreroom wcStoreroom);

}
