package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.WcFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcFileMapper extends BaseMapper<WcFile> {

    /**
     * 根据公厕ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByWcIds(@Param("wcIds") String wcIds);

    /**
     * 根据公厕ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByWcId(@Param("wcId") String wcId);
}
