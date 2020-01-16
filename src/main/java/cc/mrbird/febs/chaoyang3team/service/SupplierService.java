package cc.mrbird.febs.chaoyang3team.service;

import cc.mrbird.febs.chaoyang3team.domain.Supplier;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author CHExN
 */
public interface SupplierService extends IService<Supplier> {

    IPage<Supplier> findSupplier(QueryRequest request, Supplier supplier);

    List<Supplier> allSupplier();

    Long createSupplier(Supplier supplier);

    void updateSupplier(Supplier supplier);

    void updateSuppliersStatus(String[] supplierIds);

}
