package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.ApplicationFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface ApplicationFileService extends IService<ApplicationFile> {

    void deleteApplicationFilesByApplicationId(String[] ids);

    void deleteApplicationFilesByFileId(String[] ids);

    List<String> findFileIdsByApplicationIds(String[] ids);

    FebsResponse findFilesByApplicationId(String id);

    void createApplicationFile(ApplicationFile applicationFile);

}
