package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface StaffInsideMapper extends BaseMapper<StaffInside> {

    IPage<StaffInside> findStaffInsideDetail(Page<StaffInside> page, @Param("staffInside") StaffInside staffInside);

    IPage<StaffInside> findStaffInsideSimplify(Page<StaffInside> page, @Param("staffInside") StaffInside staffInside);

    StaffInside getStaffInside(String staffId);

    List<String> getContractInsideIds(String staffInsideIdsStr);

    List<String> getTechnicalType();

    List<String> getEntryStatus();

    List<String> getPostLevel();

    StaffInside getStaffIdByIdNum(String idNum);

    List<StaffInside> getIncreaseOrDecreaseStaffInside(@Param("staffInside") StaffInside staffInside);

}
