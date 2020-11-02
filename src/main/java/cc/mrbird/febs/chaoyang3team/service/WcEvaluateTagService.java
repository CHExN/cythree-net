package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WcEvaluateTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WcEvaluateTagService extends IService<WcEvaluateTag> {

    List<WcEvaluateTag> getWcEvaluateTagList(String deleted);

    void createWcEvaluateTag(WcEvaluateTag wcEvaluateTag);

    void updateWcEvaluateTag(WcEvaluateTag wcEvaluateTag);

    void deleteWcEvaluateTag(String[] ids);

}
