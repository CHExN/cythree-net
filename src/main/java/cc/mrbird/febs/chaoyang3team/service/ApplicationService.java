package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Application1;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface ApplicationService extends IService<Application1> {

    IPage<Application1> findApplicationDetail(QueryRequest request, Application1 application, ServletRequest servletRequest);

    void createApplication(Application1 application, ServletRequest request);

    void updateApplication(Application1 application);

    void deleteApplications(String[] applicationIds);

    void deleteApplicationsFile(String[] fileIds);

    FebsResponse uploadApplicationPhoto(MultipartFile file, String id) throws Exception;

}
