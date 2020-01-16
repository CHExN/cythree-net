package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.SealFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface SealFileMapper extends BaseMapper<SealFile> {

    /**
     * 根据印章ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsBySealIds(@Param("sealIds") String sealIds);

    /**
     * 根据印章ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesBySealId(@Param("sealId") String sealId);

}
