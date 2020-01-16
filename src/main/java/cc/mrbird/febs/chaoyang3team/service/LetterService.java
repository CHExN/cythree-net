package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Letter;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;

/**
 * @author CHExN
 */
public interface LetterService extends IService<Letter> {

    IPage<Letter> findLetterDetail(QueryRequest request, Letter letter, ServletRequest servletRequest);

    void createLetter(Letter letter, ServletRequest request);

    void updateLetter(Letter letter);

    void deleteLetters(String[] letterIds);

    void deleteLetterFile(String[] fileIds);

    FebsResponse uploadLetterFile(MultipartFile file, String letterId) throws Exception;


}
