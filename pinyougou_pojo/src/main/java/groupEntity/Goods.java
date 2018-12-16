package groupEntity;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {
    //这个类中就需要三个实体类的 我们提交的是这个对象组合形式，这个提交的数据中 包含三张表 一个是商品表，一个是 商品的描述表。还有一个是当我们选定之后 就会出现一个山工中的几个规格表  而一个商品中有 好多个商品属性项 来提供 我们选择

    private TbGoods goods;
    private TbGoodsDesc goodsDesc;
    private List<TbItem> ItemList;

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return ItemList;
    }

    public void setItemList(List<TbItem> itemList) {
        ItemList = itemList;
    }
}
