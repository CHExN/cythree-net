package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcEvaluateMapper;
import cc.mrbird.febs.chaoyang3team.domain.*;
import cc.mrbird.febs.chaoyang3team.service.*;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.weChatPhoneNumber.WxCryptUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author CHExN
 */
@Service("wcEvaluateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcEvaluateServiceImpl extends ServiceImpl<WcEvaluateMapper, WcEvaluate> implements WcEvaluateService {

    @Autowired
    private FileService fileService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WcService wcService;
    @Autowired
    private WcEvaluateTagService wcEvaluateTagService;

    @Override
    public IPage<WcEvaluate> findWcEvaluateDetail(QueryRequest request, WcEvaluate wcEvaluate) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            return this.baseMapper.findWcEvaluateDetail(page, wcEvaluate);
        } catch (Exception e) {
            log.error("查询公厕评价/投诉异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createWcEvaluate(WcEvaluate wcEvaluate) {
        wcEvaluate.setCreateTime(LocalDateTime.now());
        if (wcEvaluate.getIsComplaint().equals("0") && StringUtils.isNotBlank(wcEvaluate.getEncryptedData())
                && StringUtils.isNotBlank(wcEvaluate.getIv()) && StringUtils.isNotBlank(wcEvaluate.getSessionKey())) {
            // 解密微信手机号
            String decrypt = WxCryptUtils.decrypt(wcEvaluate.getEncryptedData(), wcEvaluate.getIv(), wcEvaluate.getSessionKey());
//            System.out.println(decrypt);
            if(StringUtils.isNotBlank(decrypt)) {
                JSONObject resultObject = JSONObject.fromObject(decrypt);
                Map<String, String> signInData = (Map<String, String>) JSONObject.toBean(resultObject, Map.class);
                String phoneNumber = signInData.get("phoneNumber");
//                System.out.println(phoneNumber);
                wcEvaluate.setPhone(phoneNumber);
            }
        }

        this.save(wcEvaluate);
        if (wcEvaluate.getIsComplaint().equals("0")) {
            StringBuilder message = new StringBuilder();
            Wc wc = wcService.selectOne(wcEvaluate.getWcId());
            message.append("公厕 「").append(wc.getWcName()).append("」 收到一条投诉");
            messageService.oneToOne(new Message(
                    null,
                    null,
                    message.toString(),
                    "bot",
                    "系统",
                    "duizhang,fuduizhang,yewu", // 队长，副队长，业务，测试账号
                    null)
            );
        }
    }

    @Override
    @Transactional
    public void reviewWcEvaluate(WcEvaluate wcEvaluate) {
        wcEvaluate.setModifyTime(LocalDateTime.now());
        if (StringUtils.isNotBlank(wcEvaluate.getProcessBefore())
                && StringUtils.isNotBlank(wcEvaluate.getProcess())
                && !wcEvaluate.getProcess().equals(wcEvaluate.getProcessBefore())
                && !wcEvaluate.getProcess().equals("0")
        ) wcEvaluate.setProcessTime(LocalDateTime.now());
        this.updateById(wcEvaluate);
    }

    @Override
    @Transactional
    public void deleteWcEvaluate(String[] ids) {
        // 删除音频、图片、头像
        List<String> fileIds = this.baseMapper.getFileIdsByWcEvaluateIds(StringUtils.join(ids, ","));
        if (!fileIds.isEmpty()) {
            fileService.deleteFiles(fileIds.toArray(new String[0]));
        }
        // 删除记录
        List<String> wcEvaluateIds = Arrays.asList(ids);
        this.baseMapper.deleteBatchIds(wcEvaluateIds);
    }

    @Override
    public List<WcEvaluateImport> findWcEvaluateExcel(WcEvaluate wcEvaluate) {
        List<WcEvaluateImport> wcEvaluateExcel = this.baseMapper.findWcEvaluateExcel(wcEvaluate);
        List<WcEvaluateTag> wcEvaluateTagList = wcEvaluateTagService.getWcEvaluateTagList(null);
        Map<String, String> wcEvaluateTagMap = wcEvaluateTagList.stream()
                .collect(Collectors.toMap(a -> a.getId().toString(), WcEvaluateTag::getTagName));
        Set<String> mapKeys = wcEvaluateTagMap.keySet();
        for (WcEvaluateImport wcEvaluateImport : wcEvaluateExcel) {
            String[] tagIds = wcEvaluateImport.getTag().split(",");
            for (int j = 0; j < tagIds.length; j++) {
                if (StringUtils.isNotBlank(tagIds[j]) && mapKeys.contains(tagIds[j])) {
                    tagIds[j] = wcEvaluateTagMap.get(tagIds[j]);
                }
            }
            wcEvaluateImport.setTag(tagIds.length == 1 ? wcEvaluateImport.getTag() : String.join("；", tagIds));
        }
        return wcEvaluateExcel;
    }

    @Override
    public List<WcComplaintImport> findWcComplaintExcel(WcEvaluate wcEvaluate) {
        return this.baseMapper.findWcComplaintExcel(wcEvaluate);
    }
}
