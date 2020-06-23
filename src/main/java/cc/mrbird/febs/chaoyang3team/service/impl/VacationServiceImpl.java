package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.VacationMapper;
import cc.mrbird.febs.chaoyang3team.domain.Vacation;
import cc.mrbird.febs.chaoyang3team.service.VacationService;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
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
import java.util.List;
import java.util.stream.IntStream;

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

    private List<Vacation> setDecember(List<Vacation> vacationList, boolean isYear) {
        vacationList.forEach(vacation -> {
            if (vacation.getDate() == null) return;
            List<String> dateList = Arrays.asList(vacation.getDate().split(","));
            final int[] days = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            final String[] remarks = {"", "", "", "", "", "", "", "", "", "", "", ""};
            dateList.forEach(date -> { // 计算是否有当月的月份
                String dateString = date.substring(0, 10);
                LocalDate startDate = LocalDate.parse(dateString);
                LocalDate endDate = LocalDate.parse(date.substring(11));
                remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1].equals("") ? "" : remarks[startDate.getMonthValue() - 1] + ",";
                if (startDate.equals(endDate)) { // 这里主要应对如"2020-01-01~2020-01-01"这种头尾都相等的特殊情况
                    remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + dateString + "~" + dateString;
                    days[startDate.getMonthValue() - 1]++;
                    return;
                }
                // 这是包头又包尾的写法
                days[startDate.getMonthValue() - 1]++;
                do {
                    // 如果是本月的最后一天
                    if (startDate.equals(startDate.with(TemporalAdjusters.lastDayOfMonth()))) {
                        remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + dateString + "~" + startDate.toString();
                    }
                    startDate = startDate.plusDays(1);
                    days[startDate.getMonthValue() - 1]++;
                    // 如果是本月的第一天
                    if (startDate.equals(LocalDate.of(startDate.getYear(),startDate.getMonth(),1))) {
                        dateString = startDate.toString();
                    }
                } while (!startDate.equals(endDate));
                remarks[startDate.getMonthValue() - 1] = remarks[startDate.getMonthValue() - 1] + dateString + "~" + endDate.toString();
            });
            System.out.println(Arrays.toString(days));
            System.out.println(Arrays.toString(remarks));
            if (isYear) {
                vacation.setAlreadyDays(IntStream.of(days).sum()); //days数组总和
                vacation.setMonth1(days[0] == 0 ? null : days[0] + "");
                vacation.setMonth2(days[1] == 0 ? null : days[1] + "");
                vacation.setMonth3(days[2] == 0 ? null : days[2] + "");
                vacation.setMonth4(days[3] == 0 ? null : days[3] + "");
                vacation.setMonth5(days[4] == 0 ? null : days[4] + "");
                vacation.setMonth6(days[5] == 0 ? null : days[5] + "");
                vacation.setMonth7(days[6] == 0 ? null : days[6] + "");
                vacation.setMonth8(days[7] == 0 ? null : days[7] + "");
                vacation.setMonth9(days[8] == 0 ? null : days[8] + "");
                vacation.setMonth10(days[9] == 0 ? null : days[9] + "");
                vacation.setMonth11(days[10] == 0 ? null : days[10] + "");
                vacation.setMonth12(days[11] == 0 ? null : days[11] + "");
            } else {
                vacation.setMonth1(days[0] == 0 ? null : vacation.getType() + days[0] + "天");
                vacation.setMonth2(days[1] == 0 ? null : vacation.getType() + days[1] + "天");
                vacation.setMonth3(days[2] == 0 ? null : vacation.getType() + days[2] + "天");
                vacation.setMonth4(days[3] == 0 ? null : vacation.getType() + days[3] + "天");
                vacation.setMonth5(days[4] == 0 ? null : vacation.getType() + days[4] + "天");
                vacation.setMonth6(days[5] == 0 ? null : vacation.getType() + days[5] + "天");
                vacation.setMonth7(days[6] == 0 ? null : vacation.getType() + days[6] + "天");
                vacation.setMonth8(days[7] == 0 ? null : vacation.getType() + days[7] + "天");
                vacation.setMonth9(days[8] == 0 ? null : vacation.getType() + days[8] + "天");
                vacation.setMonth10(days[9] == 0 ? null : vacation.getType() + days[9] + "天");
                vacation.setMonth11(days[10] == 0 ? null : vacation.getType() + days[10] + "天");
                vacation.setMonth12(days[11] == 0 ? null : vacation.getType() + days[11] + "天");
            }

            vacation.setMonth1Remark(remarks[0].equals("") ? null : remarks[0]);
            vacation.setMonth2Remark(remarks[1].equals("") ? null : remarks[1]);
            vacation.setMonth3Remark(remarks[2].equals("") ? null : remarks[2]);
            vacation.setMonth4Remark(remarks[3].equals("") ? null : remarks[3]);
            vacation.setMonth5Remark(remarks[4].equals("") ? null : remarks[4]);
            vacation.setMonth6Remark(remarks[5].equals("") ? null : remarks[5]);
            vacation.setMonth7Remark(remarks[6].equals("") ? null : remarks[6]);
            vacation.setMonth8Remark(remarks[7].equals("") ? null : remarks[7]);
            vacation.setMonth9Remark(remarks[8].equals("") ? null : remarks[8]);
            vacation.setMonth10Remark(remarks[9].equals("") ? null : remarks[9]);
            vacation.setMonth11Remark(remarks[10].equals("") ? null : remarks[10]);
            vacation.setMonth12Remark(remarks[11].equals("") ? null : remarks[11]);
        });
        return vacationList;
    }
}
