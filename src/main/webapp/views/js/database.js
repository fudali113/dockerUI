var app = angular.module('database', []);
app.controller('datacon', function($scope , $http) {

    var databases = ['mysql','postgresql','mongodb','redis','go']
    var count = 1
    $scope.databases = []
    $scope.createOrHistory = true
    $scope.nowHidePage = "my database"


    $scope.createDatabase = function(no){
        $('#myModal').modal('show');
        $scope.nowSelectDatabase = database[no]
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
            params : '',
        }).success(function(data){
            if (data == undefined) return
        })
    }

})