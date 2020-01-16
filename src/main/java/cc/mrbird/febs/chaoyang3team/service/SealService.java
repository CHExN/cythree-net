package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Seal;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface SealService extends IService<Seal> {

    IPage<Seal> findSealDetail(QueryRequest request, Seal seal, ServletRequest servletRequest);

    void createSeal(Seal seal, ServletRequest request);

    void updateSeal(Seal seal);

    void deleteSeals(String[] sealIds);

    void deleteSealsFile(String[] fileIds);

    FebsResponse uploadSealFile(MultipartFile file, String sealId) throws Exception;

}
