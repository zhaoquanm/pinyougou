app.controller("indexController",function ($scope,$controller,contentService) {
//控制器继承代码
    $controller("baseController",{$scope:$scope})
    //根据分类的id去查询广告的数据的列表
    $scope.findBycategoryId=function (categoryId) {
        contentService.findBycategoryId(categoryId).success(
            function (response) {
                //定义广告列表接受返回的数据
                $scope.contentList=response;
        })
    }
})