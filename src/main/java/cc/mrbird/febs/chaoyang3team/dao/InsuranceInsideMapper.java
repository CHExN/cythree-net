package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface InsuranceInsideMapper extends BaseMapper<InsuranceInside> {

    IPage<InsuranceInside> findInsuranceInsideDetail(Page<InsuranceInside> page, @Param("insuranceInside") InsuranceInside insuranceInside);

    IPage<InsuranceInside> findInsuranceInsideSimplify(Page<InsuranceInside> page, @Param("insuranceInside") InsuranceInside insuranceInside);

    InsuranceInside getInsuranceInsideByIdNum(String idNum);

}
