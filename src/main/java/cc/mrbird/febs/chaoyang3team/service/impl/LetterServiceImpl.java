package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.LetterMapper;
import cc.mrbird.febs.chaoyang3team.domain.*;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.LetterFileService;
import cc.mrbird.febs.chaoyang3team.service.LetterService;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Slf4j
@Service("letterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LetterServiceImpl extends ServiceImpl<LetterMapper, Letter> implements LetterService {

    @Autowired
    private FileService fileService;
    @Autowired
    private LetterFileService letterFileService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageService messageService;

    @Override
    public FebsResponse uploadLetterFile(MultipartFile file, String letterId) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.letterFileService.createLetterFile(new LetterFile(Long.valueOf(letterId), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(7)
        );
        return new FebsResponse().data(result);
    }

    @Override
    public IPage<Letter> findLetterDetail(QueryRequest request, Letter letter, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“办公室”（id：79）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("79");
            if (!isRoleId) letter.setDeptId(user.getDeptId());

            Page<Letter> page = new Page<>();
            SortUtil.handlePageSort(request, page, "letterId", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findLetter(page, letter);
        } catch (Exception e) {
            log.error("申请介绍信信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createLetter(Letter letter, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        letter.setUsername(username);
        letter.setReview("vice,captain");
        letter.setDeptId(user.getDeptId());
        letter.setDeptName(user.getDeptName());
        letter.setDateTime(DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        this.save(letter);
        // 插入附件与上会议题的关联
        if (letter.getFileId() != null) {
            String[] fileIds = letter.getFileId().split(",");
            for (String fileId : fileIds) {
                this.letterFileService.createLetterFile(new LetterFile(letter.getLetterId(), Long.valueOf(fileId)));
            }
        }
    }

    @Override
    @Transactional
    public void updateLetter(Letter letter) {
        // 判断更新的是否是更新状态的
        if (letter.getLetterUser() == null && letter.getRemark() == null) {
            String message = "";
            if (letter.getProcess() == 1)
                message = "已有一条介绍信申请审核通过";
            messageService.oneToOne(new Message(
                    null,
                    null,
                    message,
                    "bot",
                    "系统",
                    "office",
                    null)
            );
        }
        this.baseMapper.updateById(letter);
    }

    @Override
    @Transactional
    public void deleteLetters(String[] letterIds) {
        List<String> list = Arrays.asList(letterIds);
        this.baseMapper.deleteBatchIds(list);
        // 根据介绍信id查找对应照片id
        List<String> fileIdList = this.letterFileService.findFileIdsByLetterIds(letterIds);

        if (!fileIdList.isEmpty()) {
            String[] fileIds = fileIdList.toArray(new String[0]);
            this.deleteLetterFile(fileIds);
        }
    }

    @Override
    public void deleteLetterFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.letterFileService.deleteLetterFilesByFileId(fileIds);
    }
}
