package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.SendFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface SendFileService extends IService<SendFile> {

    void deleteSendFilesBySendId(String[] ids);

    void deleteSendFilesByFileId(String[] ids);

    List<String> findFileIdsBySendIds(String[] ids);

    FebsResponse findFilesBySendId(String id);

    void createSendFile(SendFile sendFile);

}
