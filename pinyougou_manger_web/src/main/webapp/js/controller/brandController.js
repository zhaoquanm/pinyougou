app.controller("brandController", function ($scope, $controller,brandService) {
    //这个是伪继承 而
    $controller("baseController",{$scope:$scope})
    //定义一个用于查询品牌的方法
    $scope.findAll = function () {
        //接收controller返回的响应的结果
        brandService.findAll().success(function (response) {
            $scope.list = response;
        })
    };
    $scope.searchEntity={};
    //发起分页的请求
    $scope.search = function (pageNum, pageSize) {
        //发起请求
        brandService.findPage($scope.searchEntity,pageNum, pageSize).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        })

    };
    //添加品牌的方法
    // 然后 定义一个单击事件 这个单击事件函数中就时通过id去将里面的值传递给controller 然后在 添加
    $scope.add = function () {
        var methodName = null;
        //在这个中判断id是否为空 当不为空 则表示的是进行的是修改的操作 当id为空 在表示的是进行的是新建的操作
        if ($scope.brand.id != null) {
            //修改操作
            methodName = brandService.update($scope.brand);
        } else {
            //添加操作
            methodName = brandService.add($scope.brand);
        }
        //在这个方法中定义一个提交的方式
        methodName.success(function (response) {
            //数据的回显 就是调用
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
            $scope.brand = {};
        })
    };
    //修改品牌的方法——当我们点击修改时 会得到这个的id 然后通过id去查询 然后将其展示到那个页面上
    $scope.findById = function (id) {
        brandService.findById(id).success(function (response) {
            $scope.brand = response;
        })
    };
    //删除数据 通过去将选中的品牌的id的数组


    //删除操作
    $scope.dele = function () { // 2,3,4
        if (confirm("您确定要删除吗？")) {
            brandService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    //添加成功，重新加载品牌列表
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            })
        }

    };

})