//这个中是我们提取的一些公用的代码 将这些代码可以放到我们的 一个公共的一个控制层中
app.controller("baseController",function ($scope) {
    //分页配置
    $scope.paginationConf = {
        currentPage: 1,  				//当前页
        totalItems: 10,					//总记录数
        itemsPerPage: 10,				//每页记录数
        perPageOptions: [10, 20, 30, 40, 50], //分页选项，下拉选择一页多少条记录
        onChange: function () {			//页面变更后触发的方法.这个是一个改变事件，当里面的值发生改变之后就会执行这个方法
            $scope.reloadList();		//启动就会调用分页组件
        }
    };
    //启动就会调用分页组件，这个是 表示的是当我们 将里面的默认值进行 改变的时候的函数
    $scope.reloadList = function () {
        //然后在这个中只需要去调用findPage  就可以,这个方法中的参数是 我们在插件中已经定义好的变量 这样 我们就可以 直接盗用这个插件中的变量来实现  这个方法
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage)

    }


    //1.先定义一个用于存放id的数组
    //定义记录选中id的数组
    $scope.selectIds = [];

    //更新复选框选中状态
    $scope.updateSelection = function ($event, id) {
        //判断选中状态
        if ($event.target.checked) {//选中状态
            $scope.selectIds.push(id);
        } else {
            //取消勾选，移除当前id值  //参数一：移除位置的元素的索引值  参数二：从该位置移除几个元素
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);
        }

    }

    //提供一个 当我们 勾选的状态 即使不在当前页面 也可以 存在的 方法
    $scope.isChecked=function (id) {
        if($scope.selectIds.indexOf(id)!=-1){
            return true;

        }else{
            return false;
        }

    }
    //解析json数据，然后根据属性名获取属性值做字符串拼接操作
    //jsonString需要解析的json字符串  key:json对象的属性名
    $scope.jsonStringParse=function (jsonString,key) {
        var value="";
        //[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        var jsonArray =JSON.parse(jsonString);
        for(var i=0;i<jsonArray.length;i++){
            //json对象根据属性名获取属性值，有两种方式
            //如果属性名是确定值，直接  对象.属性名
            //如果属性名是不确定值是变量，需要  对象[属性名]
            if(i>0){
                value+=","+jsonArray[i][key];
            }else {
                value+=jsonArray[i][key];
            }
        }

        return value;
    }

})