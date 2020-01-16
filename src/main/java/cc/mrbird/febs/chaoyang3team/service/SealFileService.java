package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.SealFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface SealFileService extends IService<SealFile> {

    void deleteSealFilesBySealId(String[] sealIds);

    void deleteSealFilesByFileId(String[] fileIds);

    List<String> findFileIdsBySealIds(String[] sealIds);

    FebsResponse findFilesBySealId(String sealId);

    void createSealFile(SealFile sealFile);

}
