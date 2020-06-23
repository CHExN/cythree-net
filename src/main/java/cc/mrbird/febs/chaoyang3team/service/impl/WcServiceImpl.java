package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.WcMapper;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.domain.Wc;
import cc.mrbird.febs.chaoyang3team.domain.WcFile;
import cc.mrbird.febs.chaoyang3team.service.*;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.FileUploadUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Service("wcService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WcServiceImpl extends ServiceImpl<WcMapper, Wc> implements WcService {

    @Autowired
    private FileService fileService;
    @Autowired
    private WcFileService wcFileService;
    @Autowired
    private WcWaterService wcWaterService;
    @Autowired
    private WcElectricityService wcElectricityService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private WcStoreroomService wcStoreroomService;

    @Override
    public FebsResponse uploadWcPhoto(MultipartFile file, String wcId) throws Exception {
        String urlResult = FileUploadUtil.fileUpload(file);
        // 添加到数据库
        String fileType = urlResult.substring(urlResult.lastIndexOf(".") + 1).toLowerCase();
        File fileInfo = new File(null, urlResult, fileType);
        this.fileService.createFile(fileInfo);
        this.wcFileService.createWcFile(new WcFile(Long.valueOf(wcId), fileInfo.getFileId()));

        ImmutableMap<String, Object> result = ImmutableMap.of(
                "uid", fileInfo.getFileId(),
                "url", urlResult,
                "status", "done",
                "name", urlResult.substring(25)
        );
        return new FebsResponse().data(result);
    }

    @Override
    public IPage<Wc> findWcDetail(QueryRequest request, Wc wc) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "wcId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findWcDetail(page, wc);
        } catch (Exception e) {
            log.error("查询公厕异常", e);
            return null;
        }
    }

    @Override
    public IPage<Wc> findWcDetailExcel(QueryRequest request, Wc wc) {
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "wcId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.findWcDetailExcel(page, wc);
        } catch (Exception e) {
            log.error("查询公厕导出数据异常", e);
            return null;
        }
    }

    @Override
    public List<String> findWcOwns() {
        return this.baseMapper.findWcOwns();
    }

    @Override
    public IPage<Wc> getWcName(QueryRequest request, Wc wc, ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = JWTUtil.getUsername(FebsUtil.decryptToken(httpServletRequest.getHeader("Authentication")));
        User user = userManager.getUser(username);
        // 判断用户角色是否包不含“业务”（id：77）
        if (!Arrays.asList(user.getRoleId().split(",")).contains("77")) {
            wc.setWcOwn(user.getDeptName()); //如果不包含业务角色，那dept就只可能是： 南分队 北分队 保洁分队 外围分队
        }
        try {
            Page<Wc> page = new Page<>();
            SortUtil.handlePageSort(request, page, "wcId", FebsConstant.ORDER_ASC, false);
            return this.baseMapper.getWcName(page, wc);
        } catch (Exception e) {
            log.error("查询公厕名称异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createWc(Wc wc) {
        this.save(wc);
    }

    @Override
    @Transactional
    public void updateWc(Wc wc) {
        this.baseMapper.updateById(wc);
    }

    @Override
    @Transactional
    public void deleteWc(String[] wcIds) {
        // 删除与公厕有关的出库记录绑定数据
        // this.wcStoreroomService.deleteWcStoreroomsByWcId(wcIds);
        // this.wcWaterService.deleteByWcId(wcIds);
        // this.wcElectricityService.deleteByElectricityId(wcIds);

        // 删除公厕信息
        List<String> ids = Arrays.asList(wcIds);
        this.baseMapper.deleteBatchIds(ids);

        // 删除照片信息
        List<String> fileIdList = this.wcFileService.findFileIdsByWcIds(wcIds);
        if (!fileIdList.isEmpty()) {
            String[] fileIds = fileIdList.toArray(new String[0]);
            this.deleteWcFile(fileIds);
        }
        // new String[fileIdList.size()]
        /*转换集合为数组的时候，有两种方式：使用初始化大小的数组（这里指的是初始化大小的时候使用了集合的size()方法）和空数组。
        在低版本的 Java 中推荐使用初始化大小的数组，因为使用反射调用去创建一个合适大小的数组相对较慢。但是在 openJDK 6 之后的高版本中方法被
        优化了，传入空数组相比传入初始化大小的数组，效果是相同的甚至有时候是更优的。因为使用 concurrent 或 synchronized 集合时，如果集合
        进行了收缩，toArray()和size()方法可能会发生数据竞争，此时传入初始化大小的数组是危险的。*/

    }

    @Override
    @Transactional
    public void deleteWcFile(String[] fileIds) {
        // 根据文件id删除
        this.fileService.deleteFiles(fileIds);
        // 删除文件关联
        this.wcFileService.deleteWcFilesByFileId(fileIds);
    }

    @Override
    @Transactional
    public void batchInsertWc(List<Wc> wcList) {
        this.saveBatch(wcList);
    }

    @Override
    public List<Wc> findWcCostAccount(String year, String month) {
        return baseMapper.findWcCostAccount(year, month);
    }

    @Override
    public List<Wc> findWcCostAccountByYear(String year, Integer up) {
        // up为0时是查上半年，为1时查下半年
        if (up == 0) {
            return baseMapper.findWcCostAccountByFirstHalf(year);
        } else if (up == 1) {
            return baseMapper.findWcCostAccountBySecondHalf(year);
        }
        return new ArrayList<>();
    }

    @Override
    public Wc getWcByWcNum(String wcNum, Boolean isLastFour) {
        return baseMapper.getWcByWcNum(wcNum, isLastFour);
    }

    @Override
    public Long getWcIdByWcNum(String wcNum, Boolean isLastFour) {
        return baseMapper.getWcIdByWcNum(wcNum, isLastFour);
    }

    @Override
    public Wc selectOne(Long wcId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Wc>().eq(Wc::getWcId, wcId));
    }

    @Override
    public List<Map<String, Object>> findAllMonthWcConsumptionByYear(String year) {
        return baseMapper.findAllMonthWcConsumptionByYear(year);
    }

    @Override
    public List<Map<String, Object>> findAllOwnWcConsumptionByYear(String year) {
        return baseMapper.findAllOwnWcConsumptionByYear(year);
    }

    @Override
    public List<Wc> findWcListByPosition(String longitude, String latitude, Integer radius, Integer length) {
        return baseMapper.findWcListByPosition(longitude, latitude, radius, length);
    }

    @Override
    public Wc getWcAndFilesById(Long wcId) {
        return baseMapper.getWcAndFilesById(wcId);
    }

}
