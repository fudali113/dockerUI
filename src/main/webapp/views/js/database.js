var app = angular.module('database', []);
app.controller('datacon', function($scope , $http) {

    $scope.databases = []

    $scope.clickimage = function(e){
        alert(e.target.innerHTML)
    }

    $scope.createDatabase = function(){
        $('#myModal').modal('show');
    }

})