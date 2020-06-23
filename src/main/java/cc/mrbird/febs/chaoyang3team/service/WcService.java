package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
public interface WcService extends IService<Wc> {

    FebsResponse uploadWcPhoto(MultipartFile file, String wcId) throws Exception;

    IPage<Wc> findWcDetail(QueryRequest request, Wc wc);

    IPage<Wc> findWcDetailExcel(QueryRequest request, Wc wc);

    List<String> findWcOwns();

    IPage<Wc> getWcName(QueryRequest request, Wc wc, ServletRequest servletRequest);

    void createWc(Wc wc);

    void updateWc(Wc wc);

    void deleteWc(String[] wcIds);

    void deleteWcFile(String[] fileIds);

    void batchInsertWc(List<Wc> wcList);

    List<Wc> findWcCostAccount(String year, String month);

    List<Wc> findWcCostAccountByYear(String year, Integer up);

    Wc getWcByWcNum(String wcNum, Boolean isLastFour);

    Long getWcIdByWcNum(String wcNum, Boolean isLastFour);

    Wc selectOne(Long wcId);

    List<Map<String, Object>> findAllMonthWcConsumptionByYear(String year);

    List<Map<String, Object>> findAllOwnWcConsumptionByYear(String year);

    List<Wc> findWcListByPosition(String longitude, String latitude, Integer radius, Integer length);

    Wc getWcAndFilesById(Long wcId);

}
