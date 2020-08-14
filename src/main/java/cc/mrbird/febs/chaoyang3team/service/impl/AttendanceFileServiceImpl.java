package cc.mrbird.febs.chaoyang3team.service.impl;

import cc.mrbird.febs.chaoyang3team.dao.AttendanceFileMapper;
import cc.mrbird.febs.chaoyang3team.domain.AttendanceFile;
import cc.mrbird.febs.chaoyang3team.domain.File;
import cc.mrbird.febs.chaoyang3team.service.AttendanceFileService;
import cc.mrbird.febs.common.domain.FebsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHExN
 */
@Service("attendanceFFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttendanceFileServiceImpl extends ServiceImpl<AttendanceFileMapper, AttendanceFile> implements AttendanceFileService {

    @Override
    @Transactional
    public void deleteAttendanceFilesByFileId(String[] fileIds) {
        List<String> list = Arrays.asList(fileIds);
        baseMapper.delete(new LambdaQueryWrapper<AttendanceFile>().in(AttendanceFile::getFileId, list));
    }

    @Override
    public FebsResponse findFilesByYearMonth(String year, String month) {
        List<File> fileList = this.baseMapper.findFilesByYearMonth(year, month);
        List<Object> result = new ArrayList<>();
        fileList.forEach(file -> {
            result.add(ImmutableMap.of(
                    "uid", file.getFileId(),// 文件唯一标识，建议设置为负数，防止和内部产生的 id 冲突
                    "url", file.getAddress(),
                    "status", "done", // 状态有：uploading done error removed
                    "name", file.getAddress().substring(25) // 文件名
            ));
        });
        return new FebsResponse().data(result);
    }

    @Override
    @Transactional
    public void createAttendanceFile(AttendanceFile attendanceFile) {
        this.save(attendanceFile);
    }
}
