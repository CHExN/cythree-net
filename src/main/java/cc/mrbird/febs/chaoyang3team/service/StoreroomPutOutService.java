package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.StoreroomPutOut;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface StoreroomPutOutService extends IService<StoreroomPutOut> {

    IPage<StoreroomPutOut> findStoreroomPutDetail(QueryRequest request, StoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    IPage<StoreroomPutOut> findStoreroomOutDetail(QueryRequest request, StoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    IPage<StoreroomPutOut> getStoreroomOutSimplify(QueryRequest request, StoreroomPutOut storeroomPutOut);

    void createStoreroomPut(StoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    void createStoreroomOut(StoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

    void updateStoreroom(StoreroomPutOut storeroomPutOut);

    void deleteStoreroomPuts(String[] StoreroomPutIds);

    void deleteStoreroomOuts(String[] StoreroomOutIds);

    /**
     * 即入即出
     * @param storeroomPutOut 出入库信息
     */
    void inOut(StoreroomPutOut storeroomPutOut, ServletRequest servletRequest);

}
