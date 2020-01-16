package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Application;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface ApplicationService extends IService<Application> {

    IPage<Application> findApplicationDetail(QueryRequest request, Application application, ServletRequest servletRequest);

    void createApplication(Application application, ServletRequest request);

    void updateApplication(Application application);

    void deleteApplications(String[] applicationIds);

    void deleteApplicationsFile(String[] fileIds);

    FebsResponse uploadApplicationPhoto(MultipartFile file, String id) throws Exception;

}
