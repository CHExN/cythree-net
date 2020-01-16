package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface FileMapper extends BaseMapper<File> {

    String findFileIdByFileName(@Param("fileName") String fileName);

}
