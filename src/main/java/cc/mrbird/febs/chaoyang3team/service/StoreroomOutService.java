package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Storeroom;
import cc.mrbird.febs.chaoyang3team.domain.StoreroomOut;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface StoreroomOutService extends IService<StoreroomOut> {

    void deleteStoreroomOutsByStoreroomId(String[] storeroomIds);

    void deleteStoreroomOutsByOutId(String[] outIds);

    List<String> getStoreroomIdsByOutIds(String[] outIds);

    List<Storeroom> getStoreroomsByOutId(String ouId);

    void batchInsertStoreroomOut(List<StoreroomOut> list);

}
