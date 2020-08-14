package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcEvaluateMapper;
import cc.mrbird.febs.chaoyang3team.domain.Message;
import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcEvaluate;
import cc.mrbird.febs.chaoyang3team.service.FileService;
import cc.mrbird.febs.chaoyang3team.service.MessageService;
import cc.mrbird.febs.chaoyang3team.service.WcEvaluateService;
import cc.mrbird.febs.chaoyang3team.service.WcService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        this.save(wcEvaluate);
        if (wcEvaluate.getIsComplaint().equals("1")) return;
        StringBuilder message = new StringBuilder();
        Wc wc = wcService.selectOne(wcEvaluate.getWcId());
        message.append("公厕 「").append(wc.getWcName()).append("」 收到一条投诉");
        messageService.oneToOne(new Message(
                null,
                null,
                message.toString(),
                "bot",
                "系统",
                "captain,vice,business", // 队长，副队长，业务
                null)
        );
    }

    @Override
    @Transactional
    public void deleteWcEvaluate(String[] ids) {
        // 删除音频文件
        List<String> fileIds = this.baseMapper.getFileIdsByWcEvaluateIds(StringUtils.join(ids, ","));
        if(!fileIds.isEmpty()) {
            fileService.deleteFiles(fileIds.toArray(new String[0]));
        }
        // 删除记录
        List<String> wcEvaluateIds = Arrays.asList(ids);
        this.baseMapper.deleteBatchIds(wcEvaluateIds);
    }
}
