package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Canteen;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface CanteenService extends IService<Canteen> {

    List<Canteen> getCanteens();

    Long createCanteen(Canteen canteen);

    void updateCanteen(Canteen canteen);

    void deleteCanteens(String[] canteenIds);
}
