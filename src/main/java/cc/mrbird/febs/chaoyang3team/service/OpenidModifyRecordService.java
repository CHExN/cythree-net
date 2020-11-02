package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.OpenidModifyRecord;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author CHExN
 */
public interface OpenidModifyRecordService extends IService<OpenidModifyRecord> {

    IPage<OpenidModifyRecord> findOpenidModifyRecordDetail(QueryRequest request, OpenidModifyRecord openidModifyRecord);

    void createOpenidModifyRecord(OpenidModifyRecord openidModifyRecord);

//    void updateOpenidModifyRecord(OpenidModifyRecord openidModifyRecord);

    void deleteOpenidModifyRecord(String[] ids);

}
