package com.pinyougou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	@Autowired
	private TbBrandMapper brandMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//获取tb_goods数据
		TbGoods tbGoods = goods.getGoods();
		tbGoods.setAuditStatus("0");//新录入商品都是未审核 0
		goodsMapper.insert(tbGoods);

		//获取tb_goods_desc数据
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		goodsDesc.setGoodsId(tbGoods.getId());
		goodsDescMapper.insert(goodsDesc);

		//是否启用规格判断
		if("1".equals(tbGoods.getIsEnableSpec())){//启用规格
			//保存tb_item表的数据
			List<TbItem> itemList = goods.getItemList();
			for (TbItem item : itemList) {
			/*后台组装
			  `title` varchar(100) NOT NULL COMMENT '商品标题',   // 商品名称（SPU名称）+ 商品规格选项名称 中间以空格隔开
			  `image` varchar(2000) DEFAULT NULL COMMENT '商品图片',  // 从 tb_goods_desc item_images中获取第一张
			  `categoryId` bigint(10) NOT NULL COMMENT '所属类目，叶子类目',  //三级分类id
			  `create_time` datetime NOT NULL COMMENT '创建时间',
			  `update_time` datetime NOT NULL COMMENT '更新时间',
			  `goods_id` bigint(20) DEFAULT NULL,
			  `seller_id` varchar(30) DEFAULT NULL,
					//以下字段作用：
			  `category` varchar(200) DEFAULT NULL, //三级分类名称
			  `brand` varchar(100) DEFAULT NULL,//品牌名称
			  `seller` varchar(200) DEFAULT NULL,//商家店铺名称*/
				String title = tbGoods.getGoodsName();
				//{"机身内存":"16G","网络":"联通3G"}
				String spec = item.getSpec();
				Map<String,String> specMap = JSON.parseObject(spec, Map.class);
				for(String key:specMap.keySet()){
					title+=" "+specMap.get(key);
				}
				item.setTitle(title);

				setItemValue(tbGoods,goodsDesc,item);

				itemMapper.insert(item);
			}
		}else{
			//没有启用规格，生一条默认的item数据
			TbItem item = new TbItem();
			item.setTitle(tbGoods.getGoodsName());
			setItemValue(tbGoods,goodsDesc,item);
			/*为启用规格时，组装页面需要提交的数据
			`spec` varchar(200) DEFAULT NULL,
			 `price` decimal(20,2) NOT NULL COMMENT '商品价格，单位为：元',
			 `num` int(10) NOT NULL COMMENT '库存数量',
			 `status` varchar(1) NOT NULL COMMENT '商品状态，1-正常，2-下架，3-删除',
			 `is_default` varchar(1) DEFAULT NULL,*/
			item.setSpec("{}");
			item.setPrice(tbGoods.getPrice());
			item.setNum(99999);
			item.setStatus("1");
			item.setIsDefault("1");
			itemMapper.insert(item);
		}





	}


	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}
	}


		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		if(goods!=null){
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}

		}

		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

    @Override
    public void addGoods(TbGoods goods) {
	    goodsMapper.insert(goods);
    }

	@Override
	public void updateIsMarketable(Long[] ids, String isMarketable) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			//只有审核通过的才能上下架操作
			if("1".equals(tbGoods.getAuditStatus())){
				tbGoods.setIsMarketable(isMarketable);
				goodsMapper.updateByPrimaryKey(tbGoods);
			}else {
				throw new RuntimeException("只有审核通过的才能上下架操作");
			}

		}
	}



	/**
	 * 抽取封装item数据的方法
	 * @param tbGoods
	 * @param goodsDesc
	 * @param item
	 */
	private void setItemValue(TbGoods tbGoods,TbGoodsDesc goodsDesc,TbItem item){
		//image数据组装
		//[{"color":"红色","url":"http://192.168.25.133/group1/M00/00/01/wKgZhVmHINKADo__AAjlKdWCzvg874.jpg"},
		// {"color":"黑色","url":"http://192.168.25.133/group1/M00/00/01/wKgZhVmHINyAQAXHAAgawLS1G5Y136.jpg"}]
		String itemImages = goodsDesc.getItemImages();
		List<Map> imageList = JSON.parseArray(itemImages, Map.class);
		if (imageList!=null && imageList.size()>0) {
			String image = (String) imageList.get(0).get("url");
			item.setImage(image);
		}

		item.setCategoryid(tbGoods.getCategory3Id());
		item.setCreateTime(new Date());
		item.setUpdateTime(new Date());
		item.setGoodsId(tbGoods.getId());
		item.setSellerId(tbGoods.getSellerId());

		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
		item.setCategory(itemCat.getName());

		TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
		item.setBrand(brand.getName());

		TbSeller seller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
		item.setSeller(seller.getNickName());
	}

	/**
	 * 这个是我们批量审核
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, String status) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

}
