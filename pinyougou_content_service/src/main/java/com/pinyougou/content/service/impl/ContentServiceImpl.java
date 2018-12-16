package com.pinyougou.content.service.impl;

import java.util.List;

import com.pinyougou.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;


import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //清除新增广告分类的缓存数据
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());

    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //清除修改广告对应的广告分类的数据
        TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
        //清除新增广告分类的缓存数据
        redisTemplate.boundHashOps("content").delete(tbContent.getCategoryId());
        contentMapper.updateByPrimaryKey(content);
        if(content.getCategoryId().longValue()!=tbContent.getCategoryId().longValue()){//判断广告分类id是否发生修改。需要清除修改后的广告分类对应的数据
            //清除新增广告分类的缓存数据
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbContent tbContent = contentMapper.selectByPrimaryKey(id);

            contentMapper.deleteByPrimaryKey(id);
            //清除新增广告分类的缓存数据
            redisTemplate.boundHashOps("content").delete(tbContent.getCategoryId());
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbContent> findBycategoryId(Long categoryId) {

        //1.从redis中获取广告数据
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        //2、判断是否能从缓存中获取广告数据
        if (contentList == null) {
            //3、如果从redis中取不到，就从数据库中获取 然后将数据列表存入缓存中
            System.out.println("mysql.............");
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            //构建广告分类查询条件，查询有效状态的广告数据列表
            criteria.andCategoryIdEqualTo(categoryId);
            criteria.andStatusEqualTo("1");
            contentList = contentMapper.selectByExample(example);
            redisTemplate.boundHashOps("content").put(categoryId,contentList);
        }else {
            System.out.println("redis.........");
        }


        return contentList;
    }


}
