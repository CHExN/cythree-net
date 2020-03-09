package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.BilateralMeeting;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface BilateralMeetingService extends IService<BilateralMeeting> {

    IPage<BilateralMeeting> findBilateralMeetingDetail(QueryRequest request, BilateralMeeting bilateralMeeting, ServletRequest servletRequest);

    void createBilateralMeeting(BilateralMeeting bilateralMeeting, ServletRequest request);

    void updateBilateralMeeting(BilateralMeeting bilateralMeeting);

    void updateBilateralMeetingOpinion(BilateralMeeting bilateralMeeting);

    void deleteBilateralMeetings(String[] bilateralMeetingIds);

    void deleteBilateralMeetingsFile(String[] fileIds);

    FebsResponse uploadBilateralMeetingFile(MultipartFile file, String bilateralMeetingId) throws Exception;
}
