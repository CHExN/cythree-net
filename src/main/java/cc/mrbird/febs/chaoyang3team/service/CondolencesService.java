package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Condolences;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface CondolencesService extends IService<Condolences> {

    IPage<Condolences> findCondolencesDetail(QueryRequest request, Condolences condolences, ServletRequest servletRequest);

    void createCondolences(Condolences condolences, ServletRequest request);

    void updateCondolences(Condolences condolences);

    void deleteCondolences(String[] condolencesIds);
}
