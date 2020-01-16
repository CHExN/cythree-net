package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.LetterFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface LetterFileService extends IService<LetterFile> {

    void deleteLetterFilesByLetterId(String[] letterIds);

    void deleteLetterFilesByFileId(String[] fileIds);

    List<String> findFileIdsByLetterIds(String[] letterIds);

    FebsResponse findFilesByLetterId(String letterId);

    void createLetterFile(LetterFile letterFile);

}
