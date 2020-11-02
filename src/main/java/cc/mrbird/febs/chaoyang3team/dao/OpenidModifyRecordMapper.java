package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.OpenidModifyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface OpenidModifyRecordMapper extends BaseMapper<OpenidModifyRecord> {

    IPage<OpenidModifyRecord> findOpenidModifyRecordDetail(Page page, @Param("omr") OpenidModifyRecord openidModifyRecord);

    Integer getModifyCountByUsername(String username, String createTimeFrom);

}
