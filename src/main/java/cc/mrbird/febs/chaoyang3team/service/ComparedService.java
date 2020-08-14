package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Compared;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
public interface ComparedService extends IService<Compared> {

    List<Compared> getComparedList(Compared compared);

    IPage<Map<String, Object>> compared(QueryRequest request, String c1, String c2);

}
