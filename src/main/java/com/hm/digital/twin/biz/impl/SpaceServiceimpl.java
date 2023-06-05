package com.hm.digital.twin.biz.impl;

import com.hm.digital.inface.biz.SpaceService;
import com.hm.digital.inface.entity.Space;
import com.hm.digital.inface.mapper.SpaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceServiceimpl implements SpaceService {

    @Autowired
    private SpaceMapper spaceMapper;

    @Override
    public List<Space> findAll(Specification<Space> toSpec) {
        return spaceMapper.findAll(toSpec);
    }

    @Override
    public Long selectNum(Specification<Space> toSpec) {
        return spaceMapper.count(toSpec);
    }
}
