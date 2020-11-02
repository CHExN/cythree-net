package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcInspection;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.ServletRequest;
import java.util.Map;

/**
 * @author CHExN
 */
public interface WcInspectionService extends IService<WcInspection> {

    Map<String, Object> findWcInspectionAndWcTemplateDetail(QueryRequest request, WcInspection wcInspection, int pageSize1, int pageNum1);

    Map<String, Object> getWcInspectionAndWcLocation(QueryRequest request, ServletRequest servletRequest, WcInspection wcInspection, String longitude, String latitude, Integer radius, Integer length);

    void createWcInspection(WcInspection wcInspection, ServletRequest servletRequest);

    void updateWcInspection(WcInspection wcInspection);

    void deleteWcInspection(String[] wcInspectionIds);

    Map<String, Object> getWcInspectionLoadData(ServletRequest servletRequest);


}
