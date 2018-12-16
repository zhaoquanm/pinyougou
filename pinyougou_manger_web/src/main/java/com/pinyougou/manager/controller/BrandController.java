package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {


    @Reference
    private BrandService brandService;

    /**
     * 查询所有的数据
     *
     * @return
     */
    @RequestMapping("findAll")
    public List<TbBrand> findAll() {
        List<TbBrand> brands = brandService.findAll();
        return brands;
    }

    /**
     * 分页查询：
     * 当我们的前端传过来 两个数 pageNumber和pageSize，然后根据这两个参数 去查询 然后 得到结果 这个结果返回的是
     * 里面是两个 一个是总的记录数totals 另一个是查询信息列表
     */
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody TbBrand tbBrand, Integer pageNum, Integer pageSize) {

        return brandService.findPage(tbBrand,pageNum, pageSize);

    }

    /**
     * 添加一个品牌
     */
    @RequestMapping("addBrand")
    public Result addBrand(@RequestBody TbBrand tbBrand) {
        try {
            System.out.println(tbBrand.getFirstChar());
            brandService.addBrand(tbBrand);
            return new Result(true, "添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败！");
        }

    }

    /**
     * 根据id去查询品牌表中的数据
     */
    @RequestMapping("findById")
    public TbBrand findById(long id) {
        TbBrand tbBrand = brandService.findById(id);
        return tbBrand;
    }

    /**
     * 修改 将id所在的行的记录 修改
     */
    @RequestMapping("update")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            System.out.println(tbBrand.getFirstChar());
            brandService.update(tbBrand);
            return new Result(true, "修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败！");
        }
    }

    /**
     * 删除的功能 就是通过前台传过来的 id的几个数组 来将这个中的数据传递过来 然后在service中 遍历这个数组 然后将其进行批量删除
     */
    @RequestMapping("deleteByIds")
    public Result deleteByIds(long[] ids){
       // System.out.println(ids.toString());
        for (long id : ids) {
            System.out.println(id);
        }
        try{
            brandService.deleteByIds(ids);
            return new Result(true,"删除成功！");
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"删除失败！");

        }
    }

    @RequestMapping("selectBrandList")
    public List<Map> selectBrandList(){

        return brandService.selectBrandList();
    }
}
