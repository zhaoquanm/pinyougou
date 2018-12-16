//服务层
app.service('uploadService',function($http){

	//增加  $http.get()   $http.post
	/*this.add=function(entity){
		return  $http.post('../goods/add.do',entity );
	}*/

	//基于angularjs结合html5提供的对象完成图片上传功能
	this.uploadFile=function () {
		//创建html5提供的表单数据对象
		var formData= new FormData();
		//参数一：表单文件对象提交值名称，与后端代码接收参数名相同
		//参数二：要提交的文件对象  file.files[0] 第一个file指的是<input type="file" id="file" />标签的id值
        formData.append("file",file.files[0]);
		return $http({
			method:"post",
			url:"../upload/uploadFile.do",
			data:formData,
            headers: {'Content-Type':undefined},//enctype="multipart/form-data"
            transformRequest: angular.identity
		});
    }


});
