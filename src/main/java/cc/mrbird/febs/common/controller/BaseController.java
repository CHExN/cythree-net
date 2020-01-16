package cc.mrbird.febs.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected Map<String, Object> getDataTable(IPage<?> pageInfo) {
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("rows", pageInfo == null ? new ArrayList<>() : pageInfo.getRecords());
        rspData.put("total", pageInfo == null ? 0 : pageInfo.getTotal());
        return rspData;
    }


}
