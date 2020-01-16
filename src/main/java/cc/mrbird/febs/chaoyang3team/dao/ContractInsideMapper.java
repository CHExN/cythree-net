package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ContractInside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface ContractInsideMapper extends BaseMapper<ContractInside> {

    IPage<ContractInside> findContractInsideDetail(Page<ContractInside> page, @Param("contractInside") ContractInside contractInside);

    ContractInside getContractInside(String staffInsideId);

    List<String> getStaffInsideIds(String contractInsideIdsStr);
}
