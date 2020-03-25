package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WageRemarkMapper;
import cc.mrbird.febs.chaoyang3team.domain.WageRemark;
import cc.mrbird.febs.chaoyang3team.service.WageRemarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service
public class WageRemarkServiceImpl extends ServiceImpl<WageRemarkMapper, WageRemark> implements WageRemarkService {

    @Override
    public List<WageRemark> findWageRemarkDetail(WageRemark wageRemark) {
        try {
            LambdaQueryWrapper<WageRemark> queryWrapper = new LambdaQueryWrapper<WageRemark>()
                    .eq(WageRemark::getInsideOrOutside, wageRemark.getInsideOrOutside())
                    .eq(WageRemark::getYear, wageRemark.getYear())
                    .eq(WageRemark::getMonth, wageRemark.getMonth());

            if (StringUtils.isNotBlank(wageRemark.getRemark())) {
                queryWrapper.like(WageRemark::getRemark, wageRemark.getRemark());
            }

            WageRemark wageRemarkResult = baseMapper.selectOne(queryWrapper);
            List<WageRemark> records = new ArrayList<>();
            if (wageRemarkResult != null) {
                String[] remarkArr = wageRemarkResult.getRemark().split(",");
                Long id = wageRemarkResult.getId();
                String year = wageRemarkResult.getYear();
                String month = wageRemarkResult.getMonth();
                for (int i = 0; i < remarkArr.length; ++i) {
                    WageRemark addWageRemark = new WageRemark(id, null, year, month, remarkArr[i], i);
                    records.add(addWageRemark);
                }
            }
            return records;
        } catch (Exception e) {
            log.error("获取工资备注失败", e);
            return null;
        }
    }

    @Override
    public void createWageRemark(WageRemark wageRemark) {
        this.save(wageRemark);
    }

    @Override
    public void updateWageRemark(WageRemark wageRemark) {
        this.baseMapper.updateById(wageRemark);
    }

    @Override
    public void deleteWageRemark(String[] wageRemarkIds) {
        List<String> list = Arrays.asList(wageRemarkIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public WageRemark getOneWageRemark(WageRemark wageRemark) {
        LambdaQueryWrapper<WageRemark> queryWrapper = new LambdaQueryWrapper<WageRemark>()
                .eq(WageRemark::getInsideOrOutside, wageRemark.getInsideOrOutside())
                .eq(WageRemark::getYear, wageRemark.getYear())
                .eq(WageRemark::getMonth, wageRemark.getMonth());

        return baseMapper.selectOne(queryWrapper);
    }
}
