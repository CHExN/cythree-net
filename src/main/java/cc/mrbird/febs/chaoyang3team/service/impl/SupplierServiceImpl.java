package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.SupplierMapper;
import cc.mrbird.febs.chaoyang3team.domain.Supplier;
import cc.mrbird.febs.chaoyang3team.service.SupplierService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("supplierService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    public IPage<Supplier> findSupplier(QueryRequest request, Supplier supplier) {
        try {
            Page<Supplier> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findSupplierDetail(page, supplier);
        } catch (Exception e) {
            log.error("查询供应商信息异常", e);
            return null;
        }
    }

    @Override
    public List<Supplier> allSupplier() {
        return this.baseMapper.selectList(null);
    }

    @Override
    @Transactional
    public Long createSupplier(Supplier supplier) {
        this.save(supplier);
        return supplier.getId();
    }

    @Override
    @Transactional
    public void updateSupplier(Supplier supplier) {
        this.baseMapper.updateById(supplier);
    }

    @Override
    @Transactional
    public void updateSuppliersStatus(String[] supplierIds) {
        List<Supplier> supplierList = new ArrayList<>();
        Arrays.stream(supplierIds).forEach(supplierId -> {
            Supplier supplier = new Supplier();
            supplier.setId(Long.valueOf(supplierId));
            supplier.setStatus("1");
            supplierList.add(supplier);
        });
        this.updateBatchById(supplierList);
    }
}
