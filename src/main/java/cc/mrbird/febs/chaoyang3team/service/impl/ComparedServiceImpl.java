package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.ComparedMapper;
import cc.mrbird.febs.chaoyang3team.domain.Compared;
import cc.mrbird.febs.chaoyang3team.service.ComparedService;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Service("comparedService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ComparedServiceImpl extends ServiceImpl<ComparedMapper, Compared> implements ComparedService {

    @Override
    public List<Compared> getComparedList(Compared compared) {
        LambdaQueryWrapper<Compared> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(compared.getBasis())) {
            lambdaQueryWrapper.eq(Compared::getBasis, compared.getBasis());
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public IPage<Map<String, Object>> compared(QueryRequest request, String c1Json, String c2Json) {
        try {
            JSONObject c1JsonObject = JSONObject.fromObject(c1Json);
            JSONObject c2JsonObject = JSONObject.fromObject(c2Json);
            Compared c1 = (Compared) JSONObject.toBean(c1JsonObject, Compared.class);
            Compared c2 = (Compared) JSONObject.toBean(c2JsonObject, Compared.class);
            Page<Object> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);
            return baseMapper.compared(page, c1, c2);
        } catch (Exception e) {
            log.error("对比信息异常", e);
            return null;
        }
    }
}
