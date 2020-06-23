package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.StaffSend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface StaffSendMapper extends BaseMapper<StaffSend> {

    IPage<StaffSend> findStaffSendDetail(Page<StaffSend> page, @Param("staffSend") StaffSend staffSend);

    StaffSend getStaffSendByStaffId(String staffId);

    StaffSend getStaffSendByIdNum(String idNum);

    List<String> getBankCardAttribution();

    List<String> getCompany();

    StaffSend getStaffIdByIdNum(String idNum);

    void deleteStaffSendTrue(String staffSendIdsStr);

    void restoreStaffSend(String staffSendIds);

    /**
     * 更新编内在职人员序号
     */
    void updateStaffSendSortNum();

    /**
     * 更新编内非在职人员序号
     */
    void updateStaffSendLeaveSortNum();

}
