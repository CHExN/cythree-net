package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroom;
import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomOut;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface UnionStoreroomOutService extends IService<UnionStoreroomOut> {

    void deleteStoreroomOutsByStoreroomId(String[] storeroomIds);

    void deleteStoreroomOutsByOutId(String[] outIds);

    List<String> getStoreroomIdsByOutIds(String[] outIds);

    List<UnionStoreroom> getStoreroomsByOutId(String ouId);

    void batchInsertStoreroomOut(List<UnionStoreroomOut> list);

}
