package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.WcComplaintImport;
import cc.mrbird.febs.chaoyang3team.domain.WcEvaluate;
import cc.mrbird.febs.chaoyang3team.domain.WcEvaluateImport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcEvaluateMapper extends BaseMapper<WcEvaluate> {

    IPage<WcEvaluate> findWcEvaluateDetail(Page page, @Param("wcEvaluate") WcEvaluate wcEvaluate);

    List<String> getFileIdsByWcEvaluateIds(@Param("ids")String ids);

    List<WcEvaluateImport> findWcEvaluateExcel(@Param("wcEvaluate") WcEvaluate wcEvaluate);

    List<WcComplaintImport> findWcComplaintExcel(@Param("wcEvaluate") WcEvaluate wcEvaluate);

}
