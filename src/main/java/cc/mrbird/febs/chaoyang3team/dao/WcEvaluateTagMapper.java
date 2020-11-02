package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcEvaluateTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcEvaluateTagMapper extends BaseMapper<WcEvaluateTag> {

    List<WcEvaluateTag> getWcEvaluateTagList(@Param("deleted")String deleted);

}
