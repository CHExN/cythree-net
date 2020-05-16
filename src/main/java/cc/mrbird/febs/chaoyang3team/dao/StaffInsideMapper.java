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

    StaffInside getStaffInsideByIdNum(String idNum);

    StaffInside getStaffInsideByStaffId(String staffId);

    List<String> getContractInsideIds(String staffInsideIdsStr);

    List<String> getTechnicalType();

    List<String> getEntryStatus();

    List<String> getPostLevel();

    StaffInside getStaffIdByIdNum(String idNum);

    /**
     * 更新编内在职人员序号
     */
    void updateStaffInsideSortNum();

    /**
     * 更新编内非在职人员序号
     */
    void updateStaffInsideLeaveSortNum();

    /**
     * 编内人员增加减少报表信息查询
     * @param staffInside 查询参数
     * @return 报表信息
     */
    List<StaffInside> getIncreaseOrDecreaseStaffInside(@Param("staffInside") StaffInside staffInside);

    void deleteStaffInsideTrue(String staffInsideIdsStr);

    void restoreStaffInside(String staffInsideIds);

}
