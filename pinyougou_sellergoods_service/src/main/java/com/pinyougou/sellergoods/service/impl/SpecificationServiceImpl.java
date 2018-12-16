package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import groupEntity.Specification;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private TbSpecificationMapper specificationMapper;
    @Autowired
    private TbSpecificationOptionMapper optionMapper;

    @Override
    public PageResult search(TbSpecification tbSpecification, Integer pageNum, Integer pageSize) {
        //进行的是分页查询


        PageHelper.startPage(pageNum, pageSize);


        //这个中进行的是条件查询
        TbSpecificationExample example = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = example.createCriteria();
        if (tbSpecification != null) {
            String specName = tbSpecification.getSpecName();
           // System.out.println(specName);
            if (specName != null && !"".equals(specName)) {
                criteria.andSpecNameLike("%" + specName + "%");
            }

        }
        //这个Page 他继承了ArrayList 所以 他能够将我们查询到的结果进行封装
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        //然后将结果返回到controller

        return new PageResult(page.getTotal(), page);


    }

    /**
     * 这是我们要添加的规格以及规格选项 而 在这个规格选项中 specID 表示的是 我们的规格的id
     *
     * @param specification
     */
    @Override
    public void add(Specification specification) {
        //这个中进行的是添加 规格以及规格选项 的操作
        //在这个中 当我们添加成功 后就必须要将这个返回一个值  而这个值 就必须是我们 添加到这个规格中的主键自增的id的值 然后才能将这个id 当做option中的spec_id 的值
        /**在 TbSpecificationMapper中 的 insert标签中添加 下面的 一段代码  这样 就会 在执行上面的操作的时候 就会把 id 传过来 这样 我们就实现了添加
         * <selectKey resultType="java.lang.Long" order="AFTER" keyProperty="id">
         select last_insert_id() as id
         </selectKey>
         */
       // specificationMapper.insert(specification.getSpecification());
        TbSpecification tbSpecification = specification.getSpecification();

   specificationMapper.insert(tbSpecification);


        List<TbSpecificationOption> options = specification.getSpecificationOptions();
        for (TbSpecificationOption option : options) {
            option.setSpecId(tbSpecification.getId());
            optionMapper.insert(option);
        }
    }
    //这个是根据规格的id去查询规格以及规格的所在的选项
    @Override
    public Specification findOne(Long id) {
        //先通过规格的id去查询规格  得到这个规格的对象
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);

        TbSpecificationOptionExample example= new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //通过这个对象 去得到这个对象下的方法 而这个方法是  通过spec_id得到和这个id相等的id 并将这个结果放到example中
        criteria.andSpecIdEqualTo(id);
        //通过example去查询得到 一个选项的集合
        List<TbSpecificationOption> options = optionMapper.selectByExample(example);
         //将这个选项集合 以及 规格的对象 封装到Specification中 最后在返回 回去
        Specification specification = new Specification();
        specification.setSpecification(tbSpecification);
        specification.setSpecificationOptions(options);


        return specification;
    }

    @Override
    public void update(Specification specification) {
        //在这的功能是根据id去修改规格里面的值
        TbSpecification tbSpecification = specification.getSpecification();

        specificationMapper.updateByPrimaryKey(tbSpecification);

        //下面是根据id 去修改选项的值

        //这个是先清除里面原有的选项的内容 根据id
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        optionMapper.deleteByExample(example);
        List<TbSpecificationOption> options = specification.getSpecificationOptions();
            //然后直接添加
        for (TbSpecificationOption option : options) {

            option.setSpecId(tbSpecification.getId());
            //直接将修改变成添加
            optionMapper.insert(option);
        }



    }

    @Override
    public void delete(long[] ids) {
        //这个是 要删除规格 以及规格下的选项
            //删除规格
        for (long id : ids) {
            specificationMapper.deleteByPrimaryKey(id);

            //删除规格下的选项表
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            optionMapper.deleteByExample(example);
        }

    }

    @Override
    public List<Map> selectSpecList() {
        return specificationMapper.selectSpecList();
    }
}
