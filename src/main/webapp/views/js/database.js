var app = angular.module('database', []);

app.filter('containerNameFilter',function (){
    return function (input){
        name = input.substring(1)
        names = name.split("_")
        simpleName = names[names.length - 1]
        return simpleName
    }
})

app.controller('datacon', function($scope , $http) {

    var databases = ['mysql','postgresql','mongodb','redis','go']
    var count = 1
    $scope.databases = []
    $scope.createOrHistory = true
    $scope.nowHidePage = "my database"
    $scope.fromData={}

    
    var getDatabase = function(){
        $http.get("/doob/docker/cons/1").success(function (response) {
            var list = new Array()
            for (i=0 ; i < response.length ; i++){
                list.push(JSON.parse(response[i]))
            }
            $scope.containers = list;
        })
    }
    getDatabase()


    $scope.createDatabase = function(no){
        $('#myModal').modal('show');
        $scope.nowSelectDatabase = databases[no]
    }

    $scope.selectPage = function(){
        if (count % 2 == 1) {
            $scope.createOrHistory = false
            $scope.nowHidePage = "create database"
        }else {
            $scope.createOrHistory = true
            $scope.nowHidePage = "my database"
        }
        count++
    }

    $scope.createDatabaseInModel = function(){
        $http({
            method: 'POST',
            url:'/doob/database/create/'+$scope.nowSelectDatabase ,
            params : $scope.fromData,
        }).success(function(data){
            if (data.result == 1) {
                $('#myModal').modal('hide')
                alert("创建"+$scope.nowSelectDatabase+"数据库实例成功")
                getDatabase()
            }else{
                alert('创建失败')
            }
        })
    }

})