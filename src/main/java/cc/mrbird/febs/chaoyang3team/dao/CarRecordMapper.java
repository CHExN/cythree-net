package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.CarRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHExN
 */
public interface CarRecordMapper extends BaseMapper<CarRecord> {

    IPage<CarRecord> findCarRecordDetail(Page page, @Param("carRecord") CarRecord carRecord);

    /**
     * 转为导出excel做的查询，绑定字典类的数据已经查询好了
     *
     * @return excel导出数据
     */
    IPage<CarRecord> findCarRecordDetailExcel(Page page, @Param("carRecord") CarRecord carRecord);

}
