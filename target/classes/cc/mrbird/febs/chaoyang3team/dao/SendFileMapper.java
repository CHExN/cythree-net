package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.SendFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface SendFileMapper extends BaseMapper<SendFile> {

    /**
     * 根据劳务派遣ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsBySendIds(@Param("ids") String ids);

    /**
     * 根据劳务派遣ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesBySendId(@Param("id") String id);

}
