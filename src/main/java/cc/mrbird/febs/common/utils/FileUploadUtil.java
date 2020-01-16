package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class FileUploadUtil {

    public static String fileUpload(MultipartFile filesParam) throws Exception {
        String fileName = filesParam.getOriginalFilename();
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateTimeNum = simpleDateFormat.format(new Date());
        String newFileName = dateTimeNum + "_" + fileName;
        //target文件夹中创建了
//        String paths = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static").getPath() + "/files";
//        String paths = request.getServletContext().getRealPath("/resources/static/files");
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
                String fullPathAddress = paths + "\\" + newFileName;
                Thumbnails.of(fullPathAddress).scale(0.5f).toFile(fullPathAddress);
            }
        }
        return "/files/" + newFileName;
    }

    public static void fileDelete(String fileName) {
//        try {
//            String paths = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static").getPath() + "/files";
//            String file = fileName.substring(7);
//            File targetFile = new File(paths, file);
//            if (targetFile.exists()) {
//                if (targetFile.delete()) {
//                    log.info("文件删除成功");
//                } else {
//                    log.error("文件删除失败");
//                }
//            } else {
//                log.error("没有文件");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
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
