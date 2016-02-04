var app = angular.module('yanshiApp', []);
app.controller('logs', function($scope , $http) {
    $scope.nowCon = "容器1"
    $scope.nowSelect = "所有"
    $scope.selectList = ['所有','系统','访问','应用']

    $scope.buttonChange = function(eve){
        $scope.nowSelect = eve.target.innerHTML
        if($scope.nowSelect == $scope.selectList[0]){
            $scope.logsInfo = '我就是全部的log'
        }else{
            $scope.logsInfo = '我不是全部的log'
        }
    }

    $scope.closeLogsInfo = function(){
        $scope.logsInfo = ""
    }

    $scope.changeNowCon = function(eve){
        $scope.nowCon = eve.target.nextElementSibling.innerHTML
    }

})
