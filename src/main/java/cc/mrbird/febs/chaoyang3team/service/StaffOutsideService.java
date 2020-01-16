package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.StaffOutside;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author CHExN
 */
public interface StaffOutsideService extends IService<StaffOutside> {

    IPage<StaffOutside> findStaffOutsideDetail(QueryRequest request, StaffOutside staffOutside, ServletRequest servletRequest);

    IPage<StaffOutside> findStaffOutsideSimplify(QueryRequest request, StaffOutside staffOutside);

    StaffOutside getStaffOutsideByStaffId(String staffId);

    StaffOutside getStaffOutsideByIdNum(String idNum);

    void createStaffOutside(StaffOutside staffOutside);

    void updateStaffOutside(StaffOutside staffOutside);

    void deleteStaffOutside(String[] staffOutsideIds);

    void deleteStaffOutsideAndContractOutside(String[] staffOutsideIds);

    /**
     * 获取岗位类别（技术专业类别）
     * @return 现有的无重复岗位类别（技术专业类别）列
     */
    List<String> getTechnicalType();

    /**
     * 岗位
     * @return 现有的无重复岗位列
     */
    List<String> getPost();

    /**
     * 分队
     * @return 现有的无重复分队列
     */
    List<String> getTeam();

    StaffOutside getStaffIdByIdNum(String idNum);
}
