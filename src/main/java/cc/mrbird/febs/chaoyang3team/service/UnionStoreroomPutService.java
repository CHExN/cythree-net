package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPut;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomPutService extends IService<UnionStoreroomPut> {

    void deleteStoreroomPutsByStoreroomId(String[] storeroomIds);

    void deleteStoreroomPutsByPutId(String[] putIds);

    List<String> getStoreroomIdsByPutIds(String[] putIds);

    List<UnionStoreroom> getStoreroomsByPutId(String ouId);

    void batchInsertStoreroomPut(List<UnionStoreroomPut> list);

}
