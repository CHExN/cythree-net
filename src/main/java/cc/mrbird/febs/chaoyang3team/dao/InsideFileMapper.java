package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.InsideFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface InsideFileMapper extends BaseMapper<InsideFile> {

    /**
     * 根据编内人员ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByInsideIds(@Param("ids") String ids);

    /**
     * 根据编内人员ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByInsideId(@Param("id") String id);

}
