package com.hm.digital.twin.dto;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * easyexcel 导出工具所用 构建对象
 * @see EasyExcelDto
 * @author YYS
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EasyExcelDto {

  // 导出的文件名
  private String exportName;
  // 模板的文件名
  private String templateName;
  // 导出到具体路径
  private String exportPath;
  // 单 list
  private Object[] data;
  // 多 list 使用 此属性填充
  private Map<String,Object> multiListMap;

}
