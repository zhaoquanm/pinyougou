package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import groupEntity.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("specification")
public class SpecificationController {


    @Reference
    private SpecificationService specificationService;
    //条件分页查询
    @RequestMapping("search")
    public PageResult searchByEntity(@RequestBody TbSpecification tbSpecification,Integer pageNum,Integer pageSize){
        //这个是通过在前端传递过来的信息进行查询 并且按照 传递过来的参数进行分页的查询
       // System.out.println(tbSpecification.getSpecName());
        return  specificationService.search(tbSpecification,pageNum,pageSize);
    }

    /**
     * 在整个的添加页面中 我们需要去将里面的规格 的数据和 规格选项的数据 都必须添加进去
     *  1、我们点击新增选项规格的时候 我们  他就会在这个编辑页面中添加一条规格选项的步骤 当点击删除的时候 就会相应的删除这个选项的部分
     *
     * @return
     */
    @RequestMapping("add")
    public Result save(@RequestBody Specification specification){
        //上面的这个封装类中就是 我们写好的 specification和specificationOption 的数据

        try{
            specificationService.add(specification);
            return new Result(true,"添加成功！");

        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"添加失败！");

        }

    }

    /**
     * 通过id去查询
     * @param id
     * @return
     */
       @RequestMapping("findOne")
       public Specification findOne(Long id){

           return specificationService.findOne(id);

       }
    /**
     * 在这个中 我们是先根据 我们选中的所在选项的id 去查当前规格的名称 和 这个规格下的选项的内容的一个列表
     * 然后展示在编辑页面上 最后在展示 当我们在进行修改时 就会吧 选项中的内容置空 然后在添加
     *
     * @return
     */
    @RequestMapping("update")
    public Result update(@RequestBody Specification specification){
       try{
           //这个中就是调用
           specificationService.update(specification);
           return new Result(true,"修改成功！");
       }catch (Exception e){
           e.printStackTrace();
           return  new Result(false,"修改失败");

       }
    }

    @RequestMapping("delete")
    public Result deleteById(long[] ids){
        try{
            //这个中就是调用
            specificationService.delete(ids);
            return new Result(true,"删除成功！");
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"删除失败");

        }
    }
    @RequestMapping("selectSpecList")
    public List<Map> selectSpecList(){
        return specificationService.selectSpecList();
    }
}
