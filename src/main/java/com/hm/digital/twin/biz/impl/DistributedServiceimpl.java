package com.hm.digital.twin.biz.impl;

import com.hm.digital.inface.biz.DistributedService;
import com.hm.digital.inface.entity.Distributed;
import com.hm.digital.inface.entity.Space;
import com.hm.digital.inface.mapper.DistributedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributedServiceimpl implements DistributedService {

    @Autowired
    private DistributedMapper distributedMapper;

    @Override
    public Distributed save(Distributed distributed) {
        return distributedMapper.save(distributed);
    }

    @Override
    public Distributed saveAndFlush(Distributed distributed) {
        return distributedMapper.saveAndFlush(distributed);
    }

    @Override
    public List<Distributed> findAll(Specification<Distributed> toSpec) {
        return distributedMapper.findAll(toSpec);
    }

    @Override
    public void remove(String id) {
        distributedMapper.deleteById(id);
    }
}
