package com.hm.digital.twin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hm.digital.common.config.QueryCondition;
import com.hm.digital.common.enums.MatchType;
import com.hm.digital.common.query.BaseQuery;
import com.hm.digital.inface.entity.Assets;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class AssetsVO extends BaseQuery<Assets> {

    /**
     * id
     */
    @QueryCondition(func = MatchType.equal)
    private String id;

    /**
     * 金额
     */
    @QueryCondition(func = MatchType.equal)
    private BigDecimal amount;


    /**
     * 空间名称
     */
    @QueryCondition(func = MatchType.like)
    private String spaceName;

    /**
     * 空间id
     */
    @QueryCondition(func = MatchType.equal)
    private String spaceId;

    /**
     * 挪用、借用、损坏等状态
     */
    @QueryCondition(func = MatchType.equal)
    private String status;


    /**
     * 数量
     */
    @QueryCondition(func = MatchType.equal)
    private String num;

    /**
     * 创建时间
     */
    @QueryCondition(func = MatchType.equal)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 修改时间
     */
    @QueryCondition(func = MatchType.equal)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;


    /**
     * 操作人
     */
    @QueryCondition(func = MatchType.equal)
    private String operator;

    /**
     * 是否使用
     */
    @QueryCondition(func = MatchType.equal)
    private String isUse;

    /**
     * 版本号
     */
    @QueryCondition(func = MatchType.equal)
    private String version;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endDate;

    @Override
    public Specification<Assets> toSpec() {
        Specification<Assets> spec = super.toSpecWithAnd();
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
