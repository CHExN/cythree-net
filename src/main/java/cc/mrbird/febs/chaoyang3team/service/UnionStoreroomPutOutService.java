package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.UnionStoreroomPutOut;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.Map;

/**
 * @author CHExN
 */
public interface UnionStoreroomPutOutService extends IService<UnionStoreroomPutOut> {

    IPage<UnionStoreroomPutOut> findStoreroomPutDetail(QueryRequest request, UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    IPage<UnionStoreroomPutOut> findStoreroomOutDetail(QueryRequest request, UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    void createStoreroomPut(UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    void createStoreroomOut(UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    void updateStoreroom(UnionStoreroomPutOut storeroomPutOut);

    Map<String, Object> deleteStoreroomPuts(String[] storeroomPutIds);

    void deleteStoreroomOuts(String[] StoreroomOutIds);

    /**
     * 即入即出
     * @param storeroomPutOut 出入库信息
     */
    void inOut(UnionStoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

}
