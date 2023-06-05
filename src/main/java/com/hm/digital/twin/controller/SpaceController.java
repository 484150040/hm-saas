package com.hm.digital.twin.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;
import com.hm.digital.common.enums.ErrorCode;
import com.hm.digital.common.rest.BaseController;
import com.hm.digital.common.utils.ResultData;
import com.hm.digital.inface.biz.DistributedService;
import com.hm.digital.inface.biz.RedisService;
import com.hm.digital.inface.entity.Distributed;
import com.hm.digital.inface.entity.Space;
import com.hm.digital.inface.mapper.SpaceMapper;
import com.hm.digital.twin.dto.SpaceDTO;
import com.hm.digital.twin.vo.DistributedVO;
import com.hm.digital.twin.vo.SpaceVO;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/space")
public class SpaceController extends BaseController<SpaceMapper, Space> {
    @Autowired
    private RedisService redisService;

    @Autowired
    private DistributedService distributedService;



    /**
     * 查询空间基础信息
     *
     * @param spaceVO
     * @return
     */
    @SneakyThrows
    @RequestMapping("/findAllPage")
    public ResultData findAllPage(@RequestBody SpaceVO spaceVO) {
        Page<Space> spaces = baseBiz.findAll(spaceVO.toSpec(), spaceVO.toPageable());
        List<Space> space = spaces.getContent();
        List<SpaceVO> spaceVOS = new ArrayList<>();
        for (Space space1 : space) {
            SpaceVO spaceVO1 = new SpaceVO();
            BeanUtils.copyProperties(space1, spaceVO1);
            DistributedVO distributedVO = new DistributedVO();
            distributedVO.setSpaceId(spaceVO1.getId());
            List<Distributed> distributeds = distributedService.findAll(distributedVO.toSpec());
            if (!CollectionUtils.isEmpty(distributeds)){
                spaceVO1.setDistributed(distributeds.get(0));
            }
            spaceVOS.add(spaceVO1);
        }
        if (spaces == null) {
            return ResultData.error(ErrorCode.NULL_OBJ.getValue(), ErrorCode.NULL_OBJ.getDesc());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("spaces",spaces);
        map.put("spaceVOS",spaceVOS);
        return ResultData.success(map);
    }
    /**
     * 查询空间基础信息
     *
     * @return
     */
    @SneakyThrows
    @RequestMapping("/findAllList")
    public ResultData findAllList() {
        List<Map> spaces = baseBiz.findList();
        List<Space> spacesAll = new ArrayList<>();
        for (Map space : spaces) {
            SpaceVO spaceVO = new SpaceVO();
            spaceVO.setName(space.get("name").toString());
            spaceVO.setBrand(space.get("brand").toString());
            List<Space> spaceList = baseBiz.findAll(spaceVO.toSpec());
            spacesAll.add(spaceList.get(0));
        }
        return ResultData.success(spacesAll);
    }


    /**
     * 添加一条记录
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultData add(@RequestBody SpaceVO entity) {

        List<Space> userList = baseBiz.findAll(entity.toSpec());
        if (!CollectionUtils.isEmpty(userList)) {
            return ResultData.error(ErrorCode.ERROR_OBJ.getValue(), ErrorCode.ERROR_OBJ.getDesc());
        }
        Space user = new Space();
        BeanUtils.copyProperties(entity, user);
        Space object = baseBiz.save(user);
        //新增位置
        if (entity.getDistributed() != null) {
            Distributed distributed = new Distributed();
            BeanUtils.copyProperties(entity.getDistributed(), distributed);
            if (StringUtils.isEmpty(distributed.getFloorSpace())){
                distributed.setFloorSpace("0");
            }
            distributed.setSpaceId(object.getId());
            distributed.setSpaceName(object.getName());
            distributed.setCreateTime(new Date());
            distributedService.save(distributed);
        }
        return ResultData.success(object);
    }

    /**
     * 修改一条记录
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultData update(@PathVariable String id, @RequestBody SpaceVO entity) {
        if (StringUtils.isEmpty(id)) {
            return ResultData.error(500, "数据为空");
        }
        List<Space> userList = baseBiz.findAll(entity.toSpec());
        if (!CollectionUtils.isEmpty(userList)) {
            return ResultData.error(ErrorCode.ERROR_OBJ.getValue(), ErrorCode.ERROR_OBJ.getDesc());
        }
        Space space = new Space();
        BeanUtils.copyProperties(entity, space);
        Space object = baseBiz.saveAndFlush(space);
        //修改数据
        if (entity.getDistributed() != null) {
            Distributed distributed = new Distributed();
            BeanUtils.copyProperties(entity.getDistributed(), distributed);
            distributedService.saveAndFlush(distributed);
        }
        return ResultData.success(object);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultData remove(@PathVariable String id) {
        baseBiz.deleteById(id);
        DistributedVO distributedVO = new DistributedVO();
        distributedVO.setSpaceId(id);
        List<Distributed> distributeds = distributedService.findAll(distributedVO.toSpec());
        if (!CollectionUtils.isEmpty(distributeds)){
            for (Distributed distributed : distributeds) {
                distributedService.remove(distributed.getId());
            }
        }
        return ResultData.success();
    }
}
