package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.VacationMapper;
import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import cc.mrbird.febs.chaoyang3team.service.VacationService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.JavaReflectionUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHExN
 */
@Slf4j
@Service("vacationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VacationServiceImpl extends ServiceImpl<VacationMapper, Vacation> implements VacationService {

    @Override
    public IPage<Vacation> findVacationDetail(QueryRequest request, Vacation vacation) {
        try {
            Page<Vacation> page = new Page<>();
            SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
            if (!(vacation.getName() == null || vacation.getName().isEmpty())) {
                vacation.setNames(vacation.getName().replace("，", ",").split(","));
            }
            return this.baseMapper.findVacationDetail(page, vacation);
        } catch (Exception e) {
            log.error("查询人员休假信息异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createVacation(Vacation vacation) {
        vacation.setCreateTime(LocalDateTime.now());
        this.save(vacation);
    }

    @Override
    @Transactional
    public void updateVacation(Vacation vacation) {
        vacation.setModifyTime(LocalDateTime.now());
        this.baseMapper.updateById(vacation);
    }

    @Override
    @Transactional
    public void deleteVacation(String[] vacationIds) {
        List<String> list = Arrays.asList(vacationIds);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    public List<String> getType() {
        return this.baseMapper.getType();
    }

    @Override
    public IPage<Vacation> findInsideAnnualLeave(QueryRequest request, Vacation vacation) {
        try {
            Page<Vacation> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            Long total = this.baseMapper.findInsideVacationCount(vacation);
            if (total == 0) return null;
            IPage<Vacation> annualLeave = this.baseMapper.findInsideAnnualLeave(page, vacation);
            annualLeave.setTotal(total);
            annualLeave.setRecords(setDecember(annualLeave.getRecords(), true));
            return annualLeave;
        } catch (Exception e) {
            log.error("查询编内年假统计信息异常", e);
            return null;
        }
    }

    @Override
    public IPage<Vacation> findInsOutVacation(QueryRequest request, Vacation vacation) {
        try {
            Page<Vacation> page = new Page<>();
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);
            if (!(vacation.getName() == null || vacation.getName().isEmpty())) {
                vacation.setNames(vacation.getName().replace("，", ",").split(","));
            }
            switch (vacation.getInsOut()) {
                case "0": {
                    Long total = this.baseMapper.findInsideVacationCount(vacation);
                    if (total == 0) return null;
                    IPage<Vacation> insideVacation = this.baseMapper.findInsideVacation(page, vacation);
                    insideVacation.setTotal(total);
                    insideVacation.setRecords(setDecember(insideVacation.getRecords(), false));
                    return insideVacation;
                }
                case "1": {
                    Long total = this.baseMapper.findAttributionVacationCount(vacation);
                    if (total == 0) return null;
                    IPage<Vacation> attributionVacation = this.baseMapper.findAttributionVacation(page, vacation);
                    attributionVacation.setTotal(total);
                    attributionVacation.setRecords(setDecember(attributionVacation.getRecords(), false));
                    return attributionVacation;
                }
                case "2": {
                    Long total = this.baseMapper.findOutsideVacationCount(vacation);
                    if (total == 0) return null;
                    IPage<Vacation> outsideVacation = this.baseMapper.findOutsideVacation(page, vacation);
                    outsideVacation.setTotal(total);
                    outsideVacation.setRecords(setDecember(outsideVacation.getRecords(), false));
                    return outsideVacation;
                }
                case "3": {
                    Long total = this.baseMapper.findSendVacationCount(vacation);
                    if (total == 0) return null;
                    IPage<Vacation> sendVacation = this.baseMapper.findSendVacation(page, vacation);
                    sendVacation.setTotal(total);
                    sendVacation.setRecords(setDecember(sendVacation.getRecords(), false));
                    return sendVacation;
                }
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("查询修假统计信息异常", e);
            return null;
        }
    }

    private static List<Vacation> setDecember(List<Vacation> vacationList, boolean isYear) {
        vacationList.forEach(vacation -> {
            if (vacation.getDate() == null) return;
            // 下面三个的长度都是一样的
            String[] dateList = vacation.getDate().split(";"); // 休假日期（范围）
            String[] remarkList = vacation.getRemark().split("@"); // 备注
            String[] typeList = vacation.getType().split(","); // 休假类型
            Map<Integer, Map<String, Integer>> days = new HashMap<>();
            final String[] remarks = {"", "", "", "", "", "", "", "", "", "", "", ""};
            for (int i = 0; i < dateList.length; i++) {
                // 给每月备注加上
                final int month = LocalDate.parse(dateList[i].substring(0, 10)).getMonthValue() - 1;
                remarks[month] = remarks[month].equals("") ? remarkList[i] : remarks[month] + "," + remarkList[i];
                // 检测日期分割
                String[] dateSplit = dateList[i].split(",");
                if (dateSplit.length > 1) {
                    for (String date : dateSplit)
                        addSet(date, typeList[i], days, remarks);
                } else {
                    addSet(dateList[i], typeList[i], days, remarks);
                }
            }
            if (isYear) {
                int alreadyDays = 0;
                int nowMonthDays;
                for (int i = 0; i < 12; i++) {
                    Map<String, Integer> typeDays = days.get(i);
                    if (typeDays == null) continue;
                    nowMonthDays = days.get(i).values().stream().mapToInt(e -> e).sum();
                    JavaReflectionUtil.invokeSet(vacation, "month" + (i + 1), nowMonthDays + "");
                    alreadyDays = alreadyDays + nowMonthDays;
                }
                vacation.setAlreadyDays(alreadyDays);
            } else {
                for (int i = 0; i < 12; i++) {
                    Map<String, Integer> typeDays = days.get(i);
                    if (typeDays == null) continue;
                    for (String key : typeDays.keySet()) {
                        String fieldName = "month" + (i + 1);
                        String value = (String) JavaReflectionUtil.invokeGet(vacation, fieldName);
                        value = value == null ? key + typeDays.get(key) + "天" : value + "\n" + key + typeDays.get(key) + "天";
                        JavaReflectionUtil.invokeSet(vacation, fieldName, value);
                    }
                }
            }
            for (int i = 0; i < 12; i++) {
                if (remarks[i].equals("")) continue;
                String fieldName = "month" + (i + 1) + "Remark";
                JavaReflectionUtil.invokeSet(vacation, fieldName, remarks[i]);
            }
        });
        return vacationList;
    }

    private static void addSet(String data, String type, Map<Integer, Map<String, Integer>> days, String[] remarks) {
        String dateString = data.substring(0, 10);
        LocalDate startDate = LocalDate.parse(dateString);
        LocalDate endDate = LocalDate.parse(data.substring(11));
        remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + ",";
        // 这里主要应对如"2020-01-01~2020-01-01"这种头尾都相等的特殊情况
        if (startDate.equals(endDate)) {
            remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + dateString + "~" + dateString;
            addTypeDaysMap(days, startDate.getMonthValue() - 1, type);
            return;
        }
        // 这是包头又包尾的写法
        addTypeDaysMap(days, startDate.getMonthValue() - 1, type);
        do {
            // 如果是本月的最后一天
            if (startDate.equals(startDate.with(TemporalAdjusters.lastDayOfMonth()))) {
                remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + dateString + "~" + startDate.toString();
            }
            startDate = startDate.plusDays(1);
            days.get(startDate.getMonthValue() - 1);
            addTypeDaysMap(days, startDate.getMonthValue() - 1, type);
            // 如果是本月的第一天
            if (startDate.equals(LocalDate.of(startDate.getYear(), startDate.getMonth(), 1))) {
                dateString = startDate.toString();
            }
        } while (!startDate.equals(endDate));
        remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + dateString + "~" + endDate.toString();
    }

    private static void addTypeDaysMap(Map<Integer, Map<String, Integer>> days, int month, String type) {
        Map<String, Integer> typeDays = days.get(month);
        if (typeDays == null || typeDays.get(type) == null) {
            if (typeDays == null) {
                typeDays = new HashMap<>();
                typeDays.put(type, 1);
                days.put(month, typeDays);
            } else {
                typeDays.put(type, 1);
            }
        } else {
            typeDays.put(type, days.get(month).get(type) + 1);
        }
    }
}
