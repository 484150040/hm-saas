package com.hm.digital.twin.controller;

import com.hm.digital.common.enums.ErrorCode;
import com.hm.digital.common.rest.BaseController;
import com.hm.digital.common.utils.ResultData;
import com.hm.digital.inface.biz.RedisService;
import com.hm.digital.inface.entity.Distributed;
import com.hm.digital.inface.mapper.DistributedMapper;
import com.hm.digital.twin.vo.DistributedVO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/distributed")
public class DistributedController extends BaseController<DistributedMapper, Distributed> {
    @Autowired
    private RedisService redisService;

    /**
     * 查询空间分布信息
     *
     * @param distributedVO
     * @return
     */
    @SneakyThrows
    @RequestMapping("/findAllPage")
    public ResultData findAllPage(@RequestBody DistributedVO distributedVO) {
        Page<Distributed> distributeds = baseBiz.findAll(distributedVO.toSpec(), distributedVO.toPageable());
        if (distributeds == null) {
            return ResultData.error(ErrorCode.NULL_OBJ.getValue(), ErrorCode.NULL_OBJ.getDesc());
        }
        return ResultData.success(distributeds);
    }

    /**
     * 查询空间位置单独信息
     *
     * @param distributedVO
     * @return
     */
    @SneakyThrows
    @RequestMapping("/findAllOne")
    public ResultData findAllandeziOne(@RequestBody DistributedVO distributedVO) {
        List<Distributed> distributeds = baseBiz.findAll(distributedVO.toSpec());
        if (CollectionUtils.isEmpty(distributeds)) {
            return ResultData.error(ErrorCode.NULL_OBJ.getValue(), ErrorCode.NULL_OBJ.getDesc());
        }
        return ResultData.success(distributeds.get(0));
    }

}
