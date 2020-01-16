package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface StaffOutsideMapper extends BaseMapper<StaffOutside> {

    IPage<StaffOutside> findStaffOutsideDetail(Page<StaffOutside> page, @Param("staffOutside") StaffOutside staffOutside);

    IPage<StaffOutside> findStaffOutsideSimplify(Page<StaffOutside> page, @Param("staffOutside") StaffOutside staffOutside);

    StaffOutside getStaffOutsideByStaffId(String staffId);

    StaffOutside getStaffOutsideByIdNum(String idNum);

    List<String> getContractOutsideIds(String staffOutsideIdsStr);

    List<String> getTechnicalType();

    List<String> getPost();

    List<String> getTeam();

    StaffOutside getStaffIdByIdNum(String idNum);
}
