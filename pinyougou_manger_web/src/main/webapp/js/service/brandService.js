//定义这个AngularJs的服务层
app.service("brandService",function ($http) {
    //定义查询所有的方法
    this.findAll=function () {
        return  $http.get("../brand/findAll.do");
    };
    //定义一个发起分页请求的方法
    this.findPage=function (searchEntity,pageNum,pageSize) {
        return  $http.post("../brand/findPage.do?pageNum="+pageNum+"&pageSize="+pageSize,searchEntity);
    }
    //定义一个报保存的方法
    this.add=function (brand) {
        return  $http.post("../brand/addBrand.do", brand)
    }
    //定义一个更新的操作
    this.update=function (brand) {
        return  $http.post("../brand/update.do" , brand)
    }
    //通过id去查询
    this.findById=function (id) {
        return   $http.get("../brand/findById.do?id=" + id);
    }
    //通过id去删除一个品牌
    this.dele=function (ids) {
        return  $http.get("../brand/deleteByIds.do?ids=" + ids);
    }

    //定义一个去查询所有的brand的列表
    this.selectBrandList= function () {
        return $http.get("../brand/selectBrandList.do");

    }


});