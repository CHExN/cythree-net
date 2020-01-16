package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.CarRefuelMapper;
import cc.mrbird.febs.chaoyang3team.domain.CarRefuel;
import cc.mrbird.febs.chaoyang3team.service.CarRefuelService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("carRefuelService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarRefuelServiceImpl extends ServiceImpl<CarRefuelMapper, CarRefuel> implements CarRefuelService {

    @Override
    public IPage<CarRefuel> findCarRefuel(QueryRequest request, CarRefuel carRefuel) {
        try {
            Page<CarRefuel> page = new Page<>();
            SortUtil.handlePageSort(request, page, "date", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findCarRefuel(page, carRefuel);
        } catch (Exception e) {
            log.error("查询车辆加油信息异常", e);
            return null;
        }
    }

    @Override
    public List<CarRefuel> findCarRefuelDetail(CarRefuel carRefuel) {
        return this.baseMapper.findCarRefuelDetail(carRefuel);
    }

    @Override
    @Transactional
    public void createCarRefuel(String carRefuels) {
        JSONArray jsonArray = JSONArray.fromObject(carRefuels);
        List<CarRefuel> carRefuelList = (List<CarRefuel>) JSONArray.toCollection(jsonArray, CarRefuel.class);
        this.saveBatch(carRefuelList);
    }

    @Override
    @Transactional
    public void updateCarRefuel(String carRefuels) {
        JSONArray jsonArray = JSONArray.fromObject(carRefuels);
        List<CarRefuel> carRefuelList = (List<CarRefuel>) JSONArray.toCollection(jsonArray, CarRefuel.class);
        // 更新的方式是先把原来同一日期的全部删掉，再保存从前台传来的修改后的数据
        String[] dates = {carRefuelList.get(0).getDate()};
        this.deleteCarRefuel(dates);
        this.saveBatch(carRefuelList);
    }

//    @Override
//    @Transactional
//    public void deleteCarRefuel(String[] carRefuelIds) {
//        List<String> list = Arrays.asList(carRefuelIds);
//        this.baseMapper.deleteBatchIds(list);
//    }

    @Override
    @Transactional
    public void deleteCarRefuel(String[] carDate) {
        this.baseMapper.delete(new LambdaQueryWrapper<CarRefuel>().in(CarRefuel::getDate, carDate));
    }
}
