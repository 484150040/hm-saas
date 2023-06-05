package com.hm.digital.twin.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;
import com.hm.digital.common.enums.ErrorCode;
import com.hm.digital.common.rest.BaseController;
import com.hm.digital.common.utils.ResultData;
import com.hm.digital.inface.biz.DistributedService;
import com.hm.digital.inface.biz.RedisService;
import com.hm.digital.inface.biz.SpaceService;
import com.hm.digital.inface.entity.Assets;
import com.hm.digital.inface.entity.Space;
import com.hm.digital.inface.mapper.AssetsMapper;
import com.hm.digital.twin.vo.AssetsVO;
import com.hm.digital.twin.vo.SpaceVO;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/assets")
public class AssetsController extends BaseController<AssetsMapper, Assets> {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SpaceService spaceService;
    /**
     * 查询资产信息
     *
     * @param assetsVO
     * @return
     */
    @SneakyThrows
    @RequestMapping("/findAllPage")
    public ResultData findAllPage(@RequestBody AssetsVO assetsVO) {
        Page<Assets> users = baseBiz.findAll(assetsVO.toSpec(), assetsVO.toPageable());
        if (users == null) {
            return ResultData.error(ErrorCode.NULL_OBJ.getValue(), ErrorCode.NULL_OBJ.getDesc());
        }
        return ResultData.success(users);
    }


    /**
     * 添加一条记录
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultData add(@RequestBody AssetsVO entity) {
        List<Assets> assetsList = baseBiz.findAll(entity.toSpec());

        SpaceVO spaceVO = new SpaceVO();
        spaceVO.setId(entity.getSpaceId());
        List<Space> spaceList = spaceService.findAll(spaceVO.toSpec());
        if (!CollectionUtils.isEmpty(assetsList)||CollectionUtils.isEmpty(spaceList)) {
            return ResultData.error(ErrorCode.ERROR_OBJ.getValue(), ErrorCode.ERROR_OBJ.getDesc());
        }
        Assets assets = new Assets();
        BeanUtils.copyProperties(entity, assets);
        assets.setSpaceName(spaceList.get(0).getName());
        SpaceVO spaceVOs = new SpaceVO();
        spaceVOs.setBrand(spaceList.get(0).getBrand());
        spaceVOs.setName(spaceList.get(0).getName());
        Long count = spaceService.selectNum(spaceVOs.toSpec());
        assets.setNum(count.toString());
        assets.setVersion("1");
        Assets object = baseBiz.save(assets);

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
    public ResultData update(@PathVariable String id, @RequestBody AssetsVO entity) {
        if (StringUtils.isEmpty(id)) {
            return ResultData.error(500, "数据为空");
        }
        SpaceVO spaceVO = new SpaceVO();
        spaceVO.setId(entity.getSpaceId());
        List<Space> spaceList = spaceService.findAll(spaceVO.toSpec());
        List<Assets> userList = baseBiz.findAll(entity.toSpec());
        if (!CollectionUtils.isEmpty(userList)) {
            return ResultData.error(ErrorCode.ERROR_OBJ.getValue(), ErrorCode.ERROR_OBJ.getDesc());
        }
        Assets assets = new Assets();
        BeanUtils.copyProperties(entity, assets);
        assets.setSpaceName(spaceList.get(0).getName());
        SpaceVO spaceVOs = new SpaceVO();
        spaceVOs.setBrand(spaceList.get(0).getBrand());
        spaceVOs.setName(spaceList.get(0).getName());
        Long count = spaceService.selectNum(spaceVOs.toSpec());
        assets.setNum(count.toString());
        assets.setVersion(String.valueOf((Integer.valueOf(assets.getVersion())+1)));
        Assets object = baseBiz.saveAndFlush(assets);

        return ResultData.success(object);
    }

}
