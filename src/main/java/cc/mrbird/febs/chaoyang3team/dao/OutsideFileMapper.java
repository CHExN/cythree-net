package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.OutsideFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface OutsideFileMapper extends BaseMapper<OutsideFile> {

    /**
     * 根据编外ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByOutsideIds(@Param("ids") String ids);

    /**
     * 根据编外ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByOutsideId(@Param("id") String id);

}
