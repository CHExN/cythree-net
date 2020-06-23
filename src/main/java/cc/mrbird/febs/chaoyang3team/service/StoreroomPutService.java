package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomPut;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
public interface StoreroomPutService extends IService<StoreroomPut> {

    void deleteStoreroomPutsByStoreroomId(String[] storeroomIds);

    void deleteStoreroomPutsByPutId(String[] putIds);

    List<String> getStoreroomIdsByPutIds(String[] putIds);

    List<Storeroom> getStoreroomsByPutId(String ouId);

    void batchInsertStoreroomPut(List<StoreroomPut> list);

    List<Map<String, Object>> findStoreroomPutTypeApplicationProportion(String date);

    List<Map<String, Object>> findStoreroomPutSupplierProportion(String date, String typeApplication);

}
