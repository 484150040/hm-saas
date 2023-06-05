package com.hm.digital.twin.dto;

import com.hm.digital.inface.entity.Space;
import lombok.Data;

import java.util.Date;
@Data
public class SpaceDTO extends Space {
    /**
     *  名称
     */
    private String name;

    /**
     * 大小
     */
    private String size;

    /**
     *  类型
     */
    private String type;

    /**
     *  标签
     */
    private String label;

    /**
     *  品牌
     */
    private String brand;

    /**
     *  状态
     */
    private String status;

    /**
     *  是否使用
     */
    private String isUse;

    /**
     *  创建时间
     */
    private Date createTime;

    /**
     *  修改时间
     */
    private Date modifyTime;

    /**
     *  是否损坏
     */
    private String isDamage;

    /**
     *  操作人
     */
    private String operator;
}
