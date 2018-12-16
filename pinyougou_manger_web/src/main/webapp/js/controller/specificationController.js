//这个是控制层controller
app.controller("specificationController",function ($scope, $controller,specificationService) {

    //这个是伪继承 而
    $controller("baseController",{$scope:$scope});
    //这个是分条件查询
    $scope.searchEntity={};
    $scope.search=function (pageNum,pageSize) {
        specificationService.search($scope.searchEntity,pageNum,pageSize).success(function (response) {
            $scope.list=response.rows;//这个结果中 是我们查询的list
            $scope.paginationConf.totalItems=response.total;

        })
    }

    //新建规格以及规格的选项


        //规格选项的新建
        //先定义一个数组
    $scope.entity={specificationOptions:[]};
    $scope.addRows=function () {
        $scope.entity.specificationOptions.push({});

    }
        //删除一个选项
    $scope.deleRow=function (index) {

        $scope.entity.specificationOptions.splice(index,1);

    }
    
    //将这个新建的数据提交给 后台
    $scope.save=function () {
        var method=null;
        if($scope.entity.specification.id!=null){
            //修改操作
            method=specificationService.update($scope.entity);
        }else{
            //添加操作
            method=specificationService.add($scope.entity);
        }

        method.success(function (response) {
            if(response.success){
                //添加成功，重新加载列表
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        })
    }

    //定义一个通过id去查询的方法
    $scope.findOne=function (id) {
        specificationService.findOne(id).success(function (resopnse) {
            $scope.entity=resopnse;
        })
    }
    //定义一个删除的方法
    $scope.dele=function () {
        if (confirm("您确定要删除吗？")) {
            specificationService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    //添加成功，重新加载列表
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            })
        }


    }

})