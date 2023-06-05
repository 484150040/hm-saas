package com.hm.digital.twin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.hm.digital.twin.dto.EasyExcelDto;
import com.huaweicloud.sdk.thirdparty.org.slf4j.Logger;
import com.huaweicloud.sdk.thirdparty.org.slf4j.LoggerFactory;

/**
 * easyexcel 模板导出工具
 * @author LionLi
 */
public class EasyExcelUtil {

  private static final Logger logger = LoggerFactory.getLogger(EasyExcelUtil.class);

  private static Sheet initSheet;
  // 系统桌面路径
  public static final String HOME_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
  // classpath 路径
  public static final String CLASS_PATH = EasyExcelUtil.class.getResource("/").getPath();
  // classpath 下 excel 文件存储路径
  public static final String SAVE_PATH = "excel" + File.separator;
  // 文件后缀
  public static final String SUFFIX = ".xlsx";


  public static String export(EasyExcelDto easyExcelObj) {
    String path = easyExcelObj.getExportPath();
    // 如果导出路径为空 默认使用系统桌面路径
    if (StringUtils.isBlank(path)) {
      path = HOME_PATH;
    }
    // 导出路径
    String exportPath = path + File.separator + easyExcelObj.getExportName() + SUFFIX;
    // 模板所在位置路径
    String templateFileName = "D:\\excel\\" + easyExcelObj.getTemplateName() + SUFFIX;
//    String templateFileName = CLASS_PATH + SAVE_PATH + easyExcelObj.getTemplateName() + SUFFIX;

    ExcelWriter excelWriter = EasyExcel.write(exportPath).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).withTemplate(templateFileName).build();
    WriteSheet writeSheet = EasyExcel.writerSheet().build();
    if (easyExcelObj.getData() != null) {
      // 单表多数据导出 模板格式为 {.属性}
      for (Object d : easyExcelObj.getData()) {
        excelWriter.fill(d, writeSheet);
      }
    } else if (easyExcelObj.getMultiListMap() != null) {
      // 多表多数据导出
      for (Map.Entry<String, Object> map : easyExcelObj.getMultiListMap().entrySet()) {
        // 设置列表后续还有数据
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        if (map.getValue() instanceof Collection) {
          // 多表导出必须使用 FillWrapper 模板格式为 {key.属性}
          excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>)map.getValue()), fillConfig, writeSheet);
        } else {
          excelWriter.fill(map.getValue(),writeSheet);
        }
      }

    } else {
      throw new IllegalArgumentException("数据为空");
    }
    // 结束构建
    excelWriter.finish();
    return exportPath;
  }


  public static void doPost(HttpServletResponse response,String filePath)
      throws ServletException, IOException {
    String fileName=filePath.substring(filePath.lastIndexOf("\\")+1);//下载后设置的文件名
    if(null != filePath){
      File downloadFile = new File(filePath);
      if(downloadFile.exists()){
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        Long length = downloadFile.length();
        response.setContentLength(length.intValue());
        ServletOutputStream out = response.getOutputStream();
        FileInputStream is = new FileInputStream(downloadFile);
        BufferedInputStream bis = new BufferedInputStream(is);
        int size = 0;
        byte[] b = new byte[4096];
        while((size = bis.read(b)) != -1){
          out.write(b, 0, size);
        }
        bis.close();
        out.flush();
        out.close();

      }else{
        System.out.println("文件不存在!");
      }
    }
  }

  public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
    return EasyExcel.read(file.getInputStream(), head, null)
        .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
        .doReadAllSync();
  }
}