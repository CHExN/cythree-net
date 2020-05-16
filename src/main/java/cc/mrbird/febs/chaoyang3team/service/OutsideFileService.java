package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.OutsideFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface OutsideFileService extends IService<OutsideFile> {

    void deleteOutsideFilesByOutsideId(String[] ids);

    void deleteOutsideFilesByFileId(String[] ids);

    List<String> findFileIdsByOutsideIds(String[] ids);

    FebsResponse findFilesByOutsideId(String id);

    void createOutsideFile(OutsideFile outsideFile);

}
