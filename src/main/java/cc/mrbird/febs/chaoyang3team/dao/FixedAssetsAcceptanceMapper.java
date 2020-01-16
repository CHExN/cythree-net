package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.FixedAssetsAcceptance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface FixedAssetsAcceptanceMapper extends BaseMapper<FixedAssetsAcceptance> {

    IPage<FixedAssetsAcceptance> findFixedAssetsAcceptanceDetail(
            Page page, @Param("faa") FixedAssetsAcceptance fixedAssetsAcceptance);

}
