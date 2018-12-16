//自定义服务  参数一：服务名称 参数二：服务层处理的事情  发起http请求
app.service("loginService",function ($http) {
    //获取用户名
    this.getName=function () {
        return $http.get("../login/loginName.do");
    }

})
