package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Insurance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface InsuranceMapper extends BaseMapper<Insurance> {

    IPage<Insurance> findInsuranceDetail(Page page, @Param("insurance") Insurance insurance);

    List<Insurance> findInsuranceSummary(@Param("insurance") Insurance insurance);

}
