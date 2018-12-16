//这个是规格的服务层 其中是进行的是和controller 中进行的请求和
app.service("specificationService",function ($http) {
        //条件查询
    this.search=function (specificationEntity,pageNum,pageSize) {
        return $http.post("../specification/search.do?pageNum="+pageNum+"&pageSize="+pageSize,specificationEntity);
    }
    //定义规格的添加的功能的前台的请求
    this.add=function (entity) {
        return $http.post("../specification/add.do",entity)
        
    };
    //通过id去查询
    this.findOne=function (id) {
        return $http.get("../specification/findOne.do?id="+id);

    }
    //修改的方法
    this.update=function (entity) {
        return $http.post("../specification/update.do",entity);

    }
    //删除的方法
    this.dele=function (selectIds) {
        return $http.get("../specification/delete.do?ids="+selectIds);

    }
    //查询模块中的规格的列表
    this.selectSpecList=function () {
        return $http.get("../specification/selectSpecList.do")

    }

})