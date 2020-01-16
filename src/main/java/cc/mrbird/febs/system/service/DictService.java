package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface DictService extends IService<Dict> {

    /**
     * 通过表名查找对应表记录的字典
     *
     * @param tableName 表名
     * @return 对应表名的字典列
     */
    List<Dict> findByTableName(String tableName);

    IPage<Dict> findDicts(QueryRequest request, Dict dict);

    void createDict(Dict dict);

    void updateDict(Dict dict);

    void deleteDicts(String[] dictIds);

}
