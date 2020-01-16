package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ContractOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface ContractOutsideMapper extends BaseMapper<ContractOutside> {

    IPage<ContractOutside> findContractOutsideDetail(Page<ContractOutside> page, @Param("contractOutside") ContractOutside contractOutside);

    ContractOutside getContractOutside(String idNum);

    List<String> getStaffOutsideIds(String contractOutsideIdsStr);
}
