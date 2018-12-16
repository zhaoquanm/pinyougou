//定义控制器  参数一：控制器名称  参数二：控制器做的事情
//$scope angularjs内置对象，全局作用域对象  作用：html与js数据交换的桥梁
//$http angularjs发起请求的内置服务对象   全部都是异步ajax
app.controller("indexController",function ($scope,$controller,loginService) {

    //控制器继承代码  参数一：继承的父控制器名称  参数二：固定写法，共享$scope对象
    $controller("baseController",{$scope:$scope});

    //获取用户名
    $scope.getName=function () {
        //response接收响应结果
        loginService.getName().success(function (response) {
            $scope.loginName=response.loginName;
        })
    }
});