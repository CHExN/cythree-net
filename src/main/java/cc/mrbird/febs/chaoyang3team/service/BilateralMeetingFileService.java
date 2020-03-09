package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.BilateralMeetingFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface BilateralMeetingFileService extends IService<BilateralMeetingFile> {

    void deleteBilateralMeetingFilesByBilateralMeetingId(String[] bilateralMeetingIds);

    void deleteBilateralMeetingFilesByFileId(String[] fileIds);

    List<String> findFileIdsByBilateralMeetingIds(String[] bilateralMeetingIds);

    FebsResponse findFilesByBilateralMeetingId(String bilateralMeetingId);

    void createBilateralMeetingFile(BilateralMeetingFile bilateralMeetingFile);
}
