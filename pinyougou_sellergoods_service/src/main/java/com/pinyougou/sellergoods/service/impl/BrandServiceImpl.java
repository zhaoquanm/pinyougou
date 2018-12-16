package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper mapper;

    @Override
    public List<TbBrand> findAll() {
        return mapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(TbBrand tbBrand, Integer pageNum, Integer pageSize) {
        //基于pagehelper的分页查询
        //设置分页查询条件
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (tbBrand != null) {
            String name = tbBrand.getName();

            if (name != null & !"".equals(name)) {
                criteria.andNameLike("%" + name + "%");
            }
            String firstChar = tbBrand.getFirstChar();
            if (firstChar != null & !"".equals(firstChar)) {
                criteria.andFirstCharEqualTo(firstChar);
            }
        }
        //这个Page是【PageHelper自己带的一个实体类

        Page<TbBrand> page = (Page<TbBrand>) mapper.selectByExample(example);
        //然后将查询到的结果封装到PageResult中  其中 这个total  表示的是 page.getTotal(),就是将查询所有的结果中的总记录数
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public void addBrand(TbBrand tbBrand) {
        mapper.insert(tbBrand);
    }

    @Override
    public TbBrand findById(long id) {
        return mapper.selectByPrimaryKey(id);

    }

    @Override
    public void update(TbBrand tbBrand) {
        mapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public void deleteByIds(long[] ids) {
        //这个中遍历这个数组 然后在去删除
        for (long id : ids) {
            mapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectBrandList() {
        return mapper.selectBrandList();
    }
}
