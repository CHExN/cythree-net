package cc.mrbird.febs.chaoyang3team.dao;

import cc.mrbird.febs.chaoyang3team.domain.Car;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author CHExN
 */
public interface CarMapper extends BaseMapper<Car> {

    IPage<Car> findCarDetail(Page page, @Param("car") Car car);

    /**
     * 加油模块需要的“卡号”与“车牌”列 和 车辆报修申请模块需要的车牌选择列
     * @return 仅有“卡号”与“车牌”“颜色”列的Car对象集
     */
    List<Car> getCarSimplify();

    /**
     * 获取不重复的车辆种类列数据
     *
     * @return 车辆用途s
     */
    List<String> getCarKind();

    /**
     * 获取不重复的车辆用途列数据
     *
     * @return 车辆用途s
     */
    List<String> getCarUse();

}
