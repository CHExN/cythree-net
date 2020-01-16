package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.CarMapper;
import cc.mrbird.febs.chaoyang3team.domain.Car;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.service.CarService;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("carService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    @Autowired
    private MessageService messageService;

    @Override
    public IPage<Car> findCar(QueryRequest request, Car car) {
        try {
            Page<Car> page = new Page<>();
            SortUtil.handlePageSort(request, page, "carId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findCarDetail(page, car);
        } catch (Exception e) {
            log.error("查询车辆信息异常", e);
            return null;
        }
    }

    @Override
    public List<Car> getCarSimplify() {
        return baseMapper.getCarSimplify();
    }

    @Override
    @Transactional
    public void createCar(Car car) {
        this.save(car);
    }

    @Override
    @Transactional
    public void updateBatchCar(String cars) {
        JSONArray jsonArray = JSONArray.fromObject(cars);
        List<Car> carList = (List<Car>) JSONArray.toCollection(jsonArray, Car.class);
        this.updateBatchById(carList);
    }

    @Override
    @Transactional
    public void updateCar(Car car) {
        this.baseMapper.updateById(car);
    }

    @Override
    @Transactional
    public void deleteCar(String[] carIds) {
        List<String> list = Arrays.asList(carIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<String> getCarKind() {
        return this.baseMapper.getCarKind();
    }

    @Override
    public List<String> getCarUse() {
        return this.baseMapper.getCarUse();
    }

    @Override
    @Transactional
    public void handleCarBirthday() {
        List<Car> carList = this.baseMapper.selectList(new QueryWrapper<>());
        StringBuilder message = new StringBuilder();

        for (Car car : carList) {
            // 现在日期
            LocalDate now = LocalDate.now();
            // 出生日期
            LocalDate dateOfBirth = LocalDate.parse(car.getDate());
            // 今年生日日期
            LocalDate BirthdayThisYear = LocalDate.parse(car.getDate()).plusYears(now.getYear() - dateOfBirth.getYear());
            // 计算 [现在日期] 与 [今年生日日期]，两个日期相距多少天
            long until1 = now.until(BirthdayThisYear, ChronoUnit.DAYS);
            // 今年生日与上年生日日期中间日期
            LocalDate June = until1 >= 0 ? BirthdayThisYear.minusMonths(6) : BirthdayThisYear.plusMonths(6);
            // 计算 [现在日期] 与 [今年生日与上年生日日期中间日期]，两个日期相距多少天
            long until2 = now.until(June, ChronoUnit.DAYS);
            // 计算 [出生日期] 与 [现在日期]，两个日期相距多少年（年龄）
            long yearsDiff = dateOfBirth.until(now, ChronoUnit.YEARS);

            System.out.println("距今年生日 " + until1 + "天");
            System.out.println("距中间日期 " + until2 + "天");
            System.out.println(yearsDiff + "岁");
            System.out.println(0 <= until1 && until1 <= 30);


            if (car.getColor().equals("1")) {
                if (0 <= until1 && until1 <= 30) {
                    message.append("车牌号为 ")
                            .append(car.getCarNum())
                            .append(" 的黄牌车辆")
                            .append("距离下次维修还有")
                            .append(until1)
                            .append("天");
                }
                if (yearsDiff >= 10 && 0 <= until2 && until2 <= 30) {
                    message.append("车牌号为 ")
                            .append(car.getCarNum())
                            .append(" 的黄牌车辆,因满10年，所以")
                            .append("距离下次维修还有")
                            .append(until2)
                            .append("天");
                }

            } else if (car.getColor().equals("2")) {
                if (0 <= until1 && until1 <= 30) {
                    message.append("车牌号为 ")
                            .append(car.getCarNum())
                            .append(" 的蓝牌车辆")
                            .append("距离下次维修还有")
                            .append(until1)
                            .append("天");
                }
                if (yearsDiff >= 15 && 0 <= until2 && until2 <= 30) {
                    message.append("车牌号为 ")
                            .append(car.getCarNum())
                            .append(" 的蓝牌车辆,因满15年，所以")
                            .append("距离下次维修还有")
                            .append(until2)
                            .append("天");
                }
            }

            if (message.length() != 0) {
                messageService.oneToOne(new Message(
                        null,
                        null,
                        message.toString(),
                        "bot",
                        "系统",
                        "cars",
                        null)
                );
                message.delete(0, message.length());
            }
        }
    }
}
