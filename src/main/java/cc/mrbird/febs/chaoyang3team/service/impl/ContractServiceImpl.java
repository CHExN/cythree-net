package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ContractMapper;
import cc.mrbird.febs.chaoyang3team.domain.Contract;
import cc.mrbird.febs.chaoyang3team.domain.ContractFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.SealFile;
import cc.mrbird.febs.chaoyang3team.service.ContractFileService;
import cc.mrbird.febs.chaoyang3team.service.ContractService;
import cc.mrbird.febs.chaoyang3team.service.FileService;
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
@Service("contractService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private FileService fileService;
    @Autowired
    private ContractFileService contractFileService;

    @Override
    public FebsResponse uploadContractFile(MultipartFile file, String contractId) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.contractFileService.createContractFile(new ContractFile(Long.valueOf(contractId), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }

    @Override
    public IPage<Contract> findContractDetail(QueryRequest request, Contract contract, ServletRequest servletRequest) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
            User user = this.userManager.getUser(username);

            //判断用户角色是否包含“办公室”（id：79）
            boolean isRoleId = Arrays.asList(user.getRoleId().split(",")).contains("79");
            if (!isRoleId) contract.setDeptId(user.getDeptId());

            Page<Contract> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findContract(page, contract, username);
        } catch (Exception e) {
            log.error("合同联审单信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createContract(Contract contract, ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        assert username != null;
        User user = this.userManager.getUser(username);

        contract.setDeptId(user.getDeptId());
        contract.setDeptName(user.getDeptName());
        contract.setUsername(username);
        contract.setCreateDate(DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        StringBuilder review = new StringBuilder();
        review
                .append("office,")  // 办公室
                .append(username.equals("union") ? "president," : "vice,") // 上级领导（副队长 or 工会会长）如果当前账号为工会，则上级领导为公会会长，否则全给副队长
                .append("vice,")     // 副队长
                .append("captain"); // 队长

//        review  // 这里是测试用的，上面是真正的
//                .append("xzhisoft,")
//                .append("xzhisoft,")
//                .append("xzhisoft,")
//                .append("xzhisoft");

        contract.setReview(review.toString());
        this.save(contract);
        // 插入附件与上会议题的关联
        if (contract.getFileId() != null) {
            String[] fileIds = contract.getFileId().split(",");
            for (String fileId : fileIds) {
                this.contractFileService.createContractFile(new ContractFile(contract.getId(), Long.valueOf(fileId)));
            }
        }
    }

    @Override
    @Transactional
    public void updateContract(Contract contract) {
        this.baseMapper.updateById(contract);
    }

    @Override
    @Transactional
    public void deleteContracts(String[] contractIds) {
        List<String> list = Arrays.asList(contractIds);
        this.baseMapper.deleteBatchIds(list);
        // 根据印章id查找对应照片id
        List<String> fileIdList = this.contractFileService.findFileIdsByContractIds(contractIds);

        if (!fileIdList.isEmpty()) {
            String[] fileIds = fileIdList.toArray(new String[0]);
            this.deleteContractsFile(fileIds);
        }
    }

    @Override
    public void deleteContractsFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.contractFileService.deleteContractFilesByFileId(fileIds);
    }
}
