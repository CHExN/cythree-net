package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.StaffSend;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author CHExN
 */
public interface StaffSendService extends IService<StaffSend> {

    IPage<StaffSend> findStaffSendDetail(QueryRequest request, StaffSend staffSend);

    StaffSend getStaffSendByStaffId(String staffId);

    StaffSend getStaffSendByIdNum(String idNum);

    void createStaffSend(StaffSend staffSend);

    void updateStaffSend(StaffSend staffSend);

    void deleteStaffSend(String[] staffSendIds, Integer deleted);

    /**
     * 获取银行卡归属
     * @return 现有的无重复银行卡归属列
     */
    List<String> getBankCardAttribution();

    /**
     * 获取公司
     * @return 现有的无重复公司列
     */
    List<String> getCompany();

    StaffSend getStaffIdByIdNum(String idNum);

    void restoreStaffSend(String staffSendIds);

//    void togetherRestoreStaffSend(String staffSendIds);

    void deleteStaffSendsFile(String[] fileIds);

    FebsResponse uploadStaffSendPhoto(MultipartFile file, String id) throws Exception;
}
