package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询编外在职人员各各分队的人数
     *
     * @return 各分队人数集合
     */
    Map<String, String> getStaffOutsideTypeCount(String isLeave);

    void updateStaffOutsideSwapSort(
            @Param("sortNum1") Long sortNum1,
            @Param("sortNum2") Long sortNum2,
            @Param("beSortNum1") Long beSortNum1,
            @Param("beSortNum2") Long beSortNum2,
            @Param("isLeave") String isLeave);

    /**
     * 更新编外在职人员总序号
     */
    void updateStaffOutsideSortNum1();

    /**
     * 更新编外在职人员分队序号
     */
    void updateStaffOutsideSortNum2(String type);


    /**
     * 更新编外非在职人员总序号
     */
    void updateStaffOutsideLeaveSortNum1();

    /**
     * 更新编外非在职人员分队序号
     */
    void updateStaffOutsideLeaveSortNum2(String type);

    /**
     * 编外人员增加减少报表信息查询
     *
     * @param staffOutside 查询参数
     * @return 报表信息
     */
    List<StaffOutside> getIncreaseOrDecreaseStaffOutside(@Param("staffOutside") StaffOutside staffOutside);

    void deleteStaffOutsideTrue(String staffOutsideIdsStr);

    void restoreStaffOutside(String staffOutsideIds);
}
