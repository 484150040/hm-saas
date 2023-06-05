package com.hm.digital.twin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hm.digital.common.config.QueryCondition;
import com.hm.digital.common.enums.MatchType;
import com.hm.digital.common.query.BaseQuery;
import com.hm.digital.inface.entity.Distributed;
import com.hm.digital.inface.entity.Space;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SpaceVO extends BaseQuery<Space> {

    /**
     *  id
     */
    @QueryCondition(func = MatchType.equal)
    private String id;

    /**
     *  名称
     */
    @QueryCondition(func = MatchType.like)
    private String name;

    /**
     * 大小
     */
    @QueryCondition(func = MatchType.equal)
    private String size;

    /**
     *  类型
     */
    @QueryCondition(func = MatchType.equal)
    private String type;

    /**
     *  标签
     */
    @QueryCondition(func = MatchType.like)
    private String label;

    /**
     *  品牌
     */
    @QueryCondition(func = MatchType.equal)
    private String brand;

    /**
     *  状态
     */
    @QueryCondition(func = MatchType.equal)
    private String status;

    /**
     *  是否使用
     */
    @QueryCondition(func = MatchType.equal)
    private String isUse;

    /**
     *  创建时间
     */
    @QueryCondition(func = MatchType.equal)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     *  修改时间
     */
    @QueryCondition(func = MatchType.equal)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     *  是否损坏
     */
    @QueryCondition(func = MatchType.equal)
    private String isDamage;

    /**
     *  操作人
     */
    @QueryCondition(func = MatchType.equal)
    private String operator;

    /**
     * 位置信息
     */
    private Distributed distributed;
    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endDate;

    @Override
    public Specification<Space> toSpec() {
        Specification<Space> spec = super.toSpecWithAnd();
        return ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            predicatesList.add(spec.toPredicate(root, criteriaQuery, criteriaBuilder));
            if (startTime != null) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.greaterThanOrEqualTo(
                                        root.get("createTime"), startTime)));
            }
            if (endDate != null) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(
                                        root.get("createTime"), endDate)));
            }

            return criteriaBuilder.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
        });
    }
}
