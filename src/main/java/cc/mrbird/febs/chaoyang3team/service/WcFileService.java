package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcFile;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcFileService extends IService<WcFile> {

    void deleteWcFilesByWcId(String[] wcIds);

    void deleteWcFilesByFileId(String[] fileIds);

    List<String> findFileIdsByWcIds(String[] wcIds);

    FebsResponse findFilesByWcId(String wcId);

    void createWcFile(WcFile wcFile);

}
