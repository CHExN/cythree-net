package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Compared;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author CHExN
 */
public interface ComparedMapper extends BaseMapper<Compared> {

    IPage<Map<String, Object>> compared(Page<Object> page, @Param("c1") Compared c1, @Param("c2") Compared c2);

}
