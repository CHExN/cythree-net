package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.LetterFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface LetterFileMapper extends BaseMapper<LetterFile> {

    /**
     * 根据介绍信ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByLetterIds(@Param("letterIds") String letterIds);

    /**
     * 根据介绍信ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByLetterId(@Param("letterId") String letterId);

}
