package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<TbBrand> findAll();

    PageResult findPage(TbBrand tbBrand,Integer pageNum, Integer pageSize);

    void addBrand(TbBrand tbBrand);

    TbBrand findById(long id);

    void update(TbBrand tbBrand);

    void deleteByIds(long[] ids);

    List<Map> selectBrandList();
}
