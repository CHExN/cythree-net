package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传文件工具类
 */
@Slf4j
public class FileUploadUtil {

    /**
     * 上传文件
     *
     * @param filesParam 要上传的文件
     * @return 上传后文件的地址
     * @throws Exception 报错
     */
    public static String fileUpload(MultipartFile filesParam) throws Exception {
        String fileName = filesParam.getOriginalFilename();
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateTimeNum = simpleDateFormat.format(new Date());
        String newFileName = dateTimeNum + "_" + fileName;
        String paths = "/home/cythree/files";
        File targetFile = new File(paths);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        File fileTransferTo = new File(targetFile, newFileName);
        OutputStream outputStream = new FileOutputStream(fileTransferTo);
        byte[] fileBytes = filesParam.getBytes();
        outputStream.write(fileBytes);
        outputStream.close();

        // 图片压缩
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (fileType.equals("jpeg") || fileType.equals("jpg") || fileType.equals("png") || fileType.equals("bmp")) {
            if (filesParam.getSize() > 1048576) {
                String fullPathAddress = paths + "/" + newFileName;
                Thumbnails.of(fullPathAddress).scale(0.5f).toFile(fullPathAddress);
            }
        }
        return "/files/" + newFileName;
    }


    /**
     * 删除文件
     *
     * @param fileName 要删除的文件名称
     */
    public static void fileDelete(String fileName) {
        String paths = "/home/cythree/files";
        String file = fileName.substring(7);
        File targetFile = new File(paths, file);
        if (targetFile.exists()) {
            if (targetFile.delete()) {
                log.info("文件删除成功");
            } else {
                log.error("文件删除失败");
            }
        } else {
            log.error("没有文件");
        }
    }
}
