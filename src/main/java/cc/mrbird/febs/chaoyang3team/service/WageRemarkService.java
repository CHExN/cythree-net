package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.WageRemark;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface WageRemarkService extends IService<WageRemark> {

    List<WageRemark> findWageRemarkDetail(WageRemark wageRemark);

    void createWageRemark(WageRemark wageRemark);

    void updateWageRemark(WageRemark wageRemark);

    void deleteWageRemark(String[] wageRemarkIds);

    WageRemark getOneWageRemark(WageRemark wageRemark);

}
