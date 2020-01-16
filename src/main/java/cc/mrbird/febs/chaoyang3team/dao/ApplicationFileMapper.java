package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.ApplicationFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface ApplicationFileMapper extends BaseMapper<ApplicationFile> {

    /**
     * 根据采购申请ID查找对应文件ID
     *
     * @return 文件ID
     */
    List<String> findFileIdsByApplicationIds(@Param("ids") String ids);

    /**
     * 根据采购申请ID查找对应文件详情
     *
     * @return 文件详情
     */
    List<File> findFilesByApplicationId(@Param("id") String id);

}
