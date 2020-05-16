package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.StaffInside;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author CHExN
 */
public interface StaffInsideService extends IService<StaffInside> {

    IPage<StaffInside> findStaffInsideDetail(QueryRequest request, StaffInside staffInside);

    IPage<StaffInside> findStaffInsideSimplify(QueryRequest request, StaffInside staffInside);

    StaffInside getStaffInsideByIdNum(String idNum);

    StaffInside getStaffInsideByStaffId(String staffId);

    void createStaffInside(StaffInside staffInside);

    void updateStaffInside(StaffInside staffInside);

    void deleteStaffInside(String[] staffInsideIds, Integer deleted);

    void deleteStaffInsideAndContractInside(String[] staffInsideIds, Integer deleted);

    /**
     * 获取岗位类别（技术专业类别）
     * @return 现有的无重复岗位类别（技术专业类别）列
     */
    List<String> getTechnicalType();

    /**
     * 入职状态
     * @return 现有的无重复入职状态列
     */
    List<String> getEntryStatus();

    /**
     * 岗位级别
     * @return 现有的无重复入职状态列
     */
    List<String> getPostLevel();

    StaffInside getStaffIdByIdNum(String idNum);

    List<StaffInside> getIncreaseOrDecreaseStaffInside(StaffInside staffInside);

    void restoreStaffInside(String staffInsideIds);

    void togetherRestoreStaffInside(String staffInsideIds);

    void deleteStaffInsidesFile(String[] fileIds);

    FebsResponse uploadStaffInsidePhoto(MultipartFile file, String id) throws Exception;

}
