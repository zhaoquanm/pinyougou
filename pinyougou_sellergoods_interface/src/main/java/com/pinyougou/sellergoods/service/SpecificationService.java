package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;
import groupEntity.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

    PageResult search(TbSpecification tbSpecification, Integer pageNum, Integer pageSize);

    void add(Specification specification);

    Specification findOne(Long id);

    void update(Specification specification);

    void delete(long[] ids);

    List<Map> selectSpecList();
}
