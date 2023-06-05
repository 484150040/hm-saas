package com.hm.digital.twin.vo;

import com.hm.digital.common.config.QueryCondition;
import com.hm.digital.common.enums.MatchType;
import com.hm.digital.common.query.BaseQuery;
import com.hm.digital.inface.entity.Distributed;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DistributedVO extends BaseQuery<Distributed> {

    /**
     * x轴
     */
    @QueryCondition(func = MatchType.equal)
    private String x;


    /**
     * y轴
     */
    @QueryCondition(func = MatchType.equal)
    private String y;

    /**
     * 空间id
     */
    @QueryCondition(func = MatchType.equal)
    private String spaceId;

    /**
     * 空间名称
     */
    @QueryCondition(func = MatchType.like)
    private String spaceName;

    /**
     * 精度
     */
    @QueryCondition(func = MatchType.equal)
    private String precision;

    /**
     * 纬度
     */
    @QueryCondition(func = MatchType.equal)
    private String latitude;

    /**
     * 位置
     */
    @QueryCondition(func = MatchType.equal)
    private String place;

    /**
     * 占地面积
     */
    @QueryCondition(func = MatchType.equal)
    private String floorSpace;

    /**
     * 创建时间
     */
    @QueryCondition(func = MatchType.equal)
    private Date createTime;


    /**
     * 修改时间
     */
    @QueryCondition(func = MatchType.equal)
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
    public Specification<Distributed> toSpec() {
        Specification<Distributed> spec = super.toSpecWithAnd();
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
