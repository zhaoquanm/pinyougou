//服务层
app.service('contentService',function($http){
	    	
	//根据广告分类id去查询广告列表数据
	this.findBycategoryId=function (categoryId ) {
		return $http.get("content/findBycategoryId.do?categoryId="+categoryId)
    }
});
