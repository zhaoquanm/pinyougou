 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			//为当前分类指定父id
            $scope.entity.parentId=$scope.paretId;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findByParentId($scope.paretId);//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	//定义一个父id变量
	$scope.paretId=0;
	//定义一个方法 查询下一级
	$scope.findByParentId=function (parentId) {
		//通过这个查询下一级的方法 的获取当前点击的id
        $scope.paretId=parentId;

		itemCatService.findByParentId(parentId).success(function (response) {
			$scope.list=response;

        })
		
    }
    $scope.grade=1;//
    //设置分类级别
	$scope.setGrade=function (grade) {
		$scope.grade=grade;
		
    }
    //实现面包屑导航
	$scope.selectItemList=function (entity_p) {
		//当级别是一级时
		if($scope.grade==1){
			$scope.entity_2=null;
			$scope.entity_3=null;

		}if($scope.grade==2){
			$scope.entity_2=entity_p;
			$scope.entity_3=null;

		}
		if($scope.grade==3){
			$scope.entity_3=entity_p;
		}
		//查询下级
		$scope.findByParentId(entity_p.id)

    }
    //定义一个查询所有模板的方法 并将查询的结果返回给我们一个list的变量中
	$scope.selectTypeList=function () {
		typeTemplateService.findAll().success(function (response) {
			$scope.selectTypeList=response;


        })
    }



});	
