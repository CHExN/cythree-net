package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Wc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
public interface WcMapper extends BaseMapper<Wc> {

    /**
     * 转为导出excel做的查询，绑定字典类的数据已经查询好了
     *
     * @return excel导出数据
     */
    IPage<Wc> findWcDetailExcel(Page page, @Param("wc") Wc wc);


    IPage<Wc> findWcDetail(Page page, @Param("wc") Wc wc);

    /**
     * 获取搜索列表的分队名称
     *
     * @return 分队名称s
     */
    List<String> findWcOwns();

    IPage<Wc> getWcName(Page page, @Param("wc") Wc wc);

    /**
     * 按年月查询公厕每月成本核算台账
     * @param year 年份
     * @param month 月份
     * @return 公厕每月成本核算台账
     */
    List<Wc> findWcCostAccount(@Param("year")String year, @Param("month")String month);

    /**
     * 按年查询公厕上半年成本核算台账 (1~6)
     * @param year 年份
     * @return 公厕上半年成本核算台账
     */
    List<Wc> findWcCostAccountByFirstHalf(@Param("year")String year);

    /**
     * 按年查询公厕下半年成本核算台账 (7~12)
     * @param year 年份
     * @return 公厕下半年成本核算台账
     */
    List<Wc> findWcCostAccountBySecondHalf(@Param("year")String year);

    /**
     * 查询公厕信息by公厕编号
     * @param wcNum 公厕编号
     * @return 公厕信息
     */
    Long getWcIdByWcNum(String wcNum);
    Long getWcIdByWaterNum(String waterNum);
    Long getWcIdByElectricityNum(String electricityNum);

    /**
     * 根据年 获取所有月份的公厕消耗
     * @param year 年份
     * @return 所有月份的公厕消耗
     */
    List<Map<String, Object>> findAllMonthWcConsumptionByYear(String year);

    /**
     * 根据年 获取所有分队前7消耗量排名
     * @param year 年份
     * @return 所有分队前7消耗量排名
     */
    List<Map<String, Object>> findAllOwnWcConsumptionByYear(String year);

}
