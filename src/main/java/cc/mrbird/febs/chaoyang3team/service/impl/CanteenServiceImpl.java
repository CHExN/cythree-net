package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.CanteenMapper;
import cc.mrbird.febs.chaoyang3team.domain.Canteen;
import cc.mrbird.febs.chaoyang3team.service.CanteenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("canteenService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CanteenServiceImpl extends ServiceImpl<CanteenMapper, Canteen> implements CanteenService {

    @Override
    public List<Canteen> getCanteens() {
        return baseMapper.selectList(null);
    }

    @Override
    @Transactional
    public Long createCanteen(Canteen canteen) {
        // 先删后插
        this.baseMapper.deleteById(canteen.getId());
        this.save(canteen);
        return canteen.getId();
    }

    @Override
    @Transactional
    public void updateCanteen(Canteen canteen) {
        //这个基本用不到了
        this.baseMapper.updateById(canteen);
    }

    @Override
    @Transactional
    public void deleteCanteens(String[] canteenIds) {
        List<String> list = Arrays.asList(canteenIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
