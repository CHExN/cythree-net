package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.InsuranceInside;
import cc.mrbird.febs.chaoyang3team.domain.InsuranceOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface InsuranceOutsideMapper extends BaseMapper<InsuranceOutside> {

    IPage<InsuranceOutside> findInsuranceOutsideDetail(Page<InsuranceOutside> page, @Param("insuranceOutside") InsuranceOutside insuranceOutside);

    IPage<InsuranceOutside> findInsuranceOutsideSimplify(Page<InsuranceOutside> page, @Param("insuranceOutside") InsuranceOutside insuranceOutside);

    InsuranceInside getInsuranceOutsideByIdNum(String idNum);

}
