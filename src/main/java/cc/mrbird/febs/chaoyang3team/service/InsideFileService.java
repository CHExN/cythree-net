package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.InsideFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface InsideFileService extends IService<InsideFile> {

    void deleteInsideFilesByInsideId(String[] ids);

    void deleteInsideFilesByFileId(String[] ids);

    List<String> findFileIdsByInsideIds(String[] ids);

    FebsResponse findFilesByInsideId(String id);

    void createInsideFile(InsideFile insideFile);

}
