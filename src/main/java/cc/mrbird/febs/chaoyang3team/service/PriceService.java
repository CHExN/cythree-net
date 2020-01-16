package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Price;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface PriceService extends IService<Price> {

    /**
     * 通过物品名称，模糊查找对应表记录的价格信息
     *
     * @param name 物品名称
     * @return 对应物品名称的价格信息列
     */
    List<Price> findByName(String name);

    IPage<Price> findPrices(QueryRequest request, Price price);

    void createPrice(Price price);

    void updatePrice(Price price);

    void deletePrices(String[] priceIds);

}
