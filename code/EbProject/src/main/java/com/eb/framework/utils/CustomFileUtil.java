package com.eb.framework.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName FileUtil
 * @Description TODO
 * @Author
 * @Date 2020/5/19 8:54
 * @Version
 **/
public class CustomFileUtil {

    /**
     * Excel(xls格式)文件下载
     **/
    public static void downloadXLS(ExcelWriter writer, String fileName, HttpServletResponse response) throws IOException {
        // ExcelWriter writer = ExcelUtil.getWriter(); (对象创建需与导出文件保持格式一致,默认创建xls格式)
        // response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        // xxxx.xls是弹出下载对话框的文件名，
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        // 关闭输出Servlet流
        IoUtil.close(out);

    }

    /**
     * Excel(xlsx格式)文件下载
     **/
    public static void downloadXLSX(ExcelWriter writer, String fileName, HttpServletResponse response) throws IOException {
        // ExcelWriter writer = ExcelUtil.getWriter(true); (对象创建需与导出文件保持格式一致)
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

    /**
     * 普通文件下载文件下载(指定参数:filePath、fileSuffix、fileName)
     **/
    public static boolean downloadFile(Map<String, String> paramMap, HttpServletResponse response) throws IOException {
        if (paramMap.isEmpty()) {
            return false;
        }
        String filePath = paramMap.get("filePath");
        String fileSuffix = paramMap.get("fileSuffix");
        String fileName = paramMap.get("fileName");
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/force-download");
        // 设置编码，避免文件名中文乱码
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + fileSuffix);
        outputStream.write(FileUtil.readBytes(filePath));
        IoUtil.close(outputStream);
        return true;
    }

    /**
     * 普通文件下载文件下载(指定参数:filePath、fileSuffix、fileName)
     **/
    public static void downloadFile(File file, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/force-download");
        // 设置编码，避免文件名中文乱码
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        outputStream.write(FileUtil.readBytes(file));
        IoUtil.close(outputStream);
    }



    /**
     * 验证Excel文件是否存在或者后缀名是否正确
     **/
    public static boolean validExcelFile(File file) {
        if (!file.exists()) {
            return false;
        }
        String fileName = file.getName();
        return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
    }


    /**
     * 验证ZIP文件是否存在或者后缀名是否正确
     **/
    public static boolean validZipFile(File file) {
        if (!file.exists()) {
            return false;
        }
        String fileName = file.getName();
        return fileName.endsWith(".zip");
    }

    // 遍历指定文件夹根目录下的文件
    private static void getOneDir(File file, LinkedList<File> dirlist,
                                  LinkedList<File> fileList) {
        // 每个文件夹遍历都会调用该方法
        File[] files = file.listFiles();

        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                dirlist.add(f);
            } else {
                // 这里列出当前文件夹根目录下的所有文件,并添加到fileList列表中
                fileList.add(f);
                // System.out.println("file==>" + f);
            }
        }
    }

    /**
     * 非递归遍历(获取文件列表-不包含文件夹,只列举所有‘单个文件’)
     */
    public static LinkedList<File> getDirectory(String path) {
        File file = new File(path);
        // 保存待遍历文件夹的列表
        LinkedList<File> dirlist = new LinkedList<File>();
        LinkedList<File> fileList = new LinkedList<File>();
        // 调用遍历文件夹根目录文件的方法
        getOneDir(file, dirlist, fileList);
        File tmp;
        while (!dirlist.isEmpty()) {
            // 从文件夹列表中删除第一个文件夹，并返回该文件夹赋给tmp变量
            tmp = (File) dirlist.removeFirst();
            // 遍历这个文件夹下的所有文件，并添加到fileList列表中
            getOneDir(tmp, dirlist, fileList);

        }
        return fileList;
    }





    /**
     * 上传文件处理(对文件进行重命名处理)
     **/
    public static File uploadCommonFile(MultipartFile file, String targetPath,boolean isNeedRename) throws Exception {
        // 获取MultipartFile各个方法的调用的内容
        System.out.println("文件类型ContentType=" + file.getContentType());
        System.out.println("文件组件名称Name=" + file.getName());
        System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
        System.out.println("文件大小Size=" + file.getSize() / 1024 + "KB");
        // 根据条件判断是否需要进行重命名处理
        String newFileName = file.getOriginalFilename();
        if(isNeedRename){
            newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "-" + file.getOriginalFilename();
        }
        // 如果文件不为空，写入上传路径，进行文件上传
        if (!file.isEmpty()) {
            // 构建上传文件的存放路径
            // String path = request.getServletContext().getRealPath("/upload/");
            // String path = "D:\\upload\\eoas\\cm";
            System.out.println("targetPath = " + targetPath);

            // 获取上传的文件名称，并结合存放路径，构建新的文件名称
             File filepath = new File(targetPath, newFileName);

            // 判断路径是否存在，不存在则新创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            // 将上传文件保存到目标文件目录
            file.transferTo(new File(targetPath + File.separator + newFileName));
        }
        // 返回上传的文件路径
        return new File(targetPath + File.separator + newFileName);
    }

}



