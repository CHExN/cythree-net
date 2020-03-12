package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.SealMapper;
import cc.mrbird.febs.chaoyang3team.domain.*;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.chaoyang3team.service.SealFileService;
import cc.mrbird.febs.chaoyang3team.service.SealService;
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
@Service("sealService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SealServiceImpl extends ServiceImpl<SealMapper, Seal> implements SealService {

    @Autowired
    private FileService fileService;
    @Autowired
    private SealFileService sealFileService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageService messageService;

    @Override
    public FebsResponse uploadSealFile(MultipartFile file, String sealId) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.sealFileService.createSealFile(new SealFile(Long.valueOf(sealId), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(7)
        );
        return new FebsResponse().data(result);
    }

    @Override
    public IPage<Seal> findSealDetail(QueryRequest request, Seal seal, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“办公室”（id：79）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("79");
            if (!isRoleId) seal.setDeptId(user.getDeptId());

            Page<Seal> page = new Page<>();
            SortUtil.handlePageSort(request, page, "sealId", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findSeal(page, seal);
        } catch (Exception e) {
            log.error("印章使用审批单信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createSeal(Seal seal, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = this.userManager.getUser(username);
        seal.setReview("vice,captain");
        seal.setUsername(username);
        seal.setDeptId(user.getDeptId());
        seal.setDeptName(user.getDeptName());
        seal.setDateTime(DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        this.save(seal);
        // 插入附件与上会议题的关联
        if (seal.getFileId() != null) {
            String[] fileIds = seal.getFileId().split(",");
            for (String fileId : fileIds) {
                this.sealFileService.createSealFile(new SealFile(seal.getSealId(), Long.valueOf(fileId)));
            }
        }
    }

    @Override
    @Transactional
    public void updateSeal(Seal seal) {
        // 判断更新的是否是更新状态的
        if (seal.getSealUser() == null && seal.getAmount() == null && seal.getRemark() == null && seal.getProcess() == 1) {
            String message = "已有一条印章使用审批单审核通过";
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
        this.baseMapper.updateById(seal);
    }

    @Override
    @Transactional
    public void deleteSeals(String[] sealIds) {
        List<String> list = Arrays.asList(sealIds);
        this.baseMapper.deleteBatchIds(list);
        // 根据印章id查找对应照片id
        List<String> fileIdList = this.sealFileService.findFileIdsBySealIds(sealIds);

        if (!fileIdList.isEmpty()) {
            String[] fileIds = fileIdList.toArray(new String[0]);
            this.deleteSealsFile(fileIds);
        }
    }

    @Override
    public void deleteSealsFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.sealFileService.deleteSealFilesByFileId(fileIds);
    }
}
