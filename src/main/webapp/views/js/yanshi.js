var app = angular.module('yanshiApp', []);
app.controller('logs', function($scope , $http) {
    $scope.nowCon = "..."
    $scope.nowSelect = "所有"
    $scope.selectList = ['所有','系统','访问','应用']

    var url = "/doob/docker/containers/json"
    $http.get(url).success( function(response) {
        $scope.containers = response;
    });

    $http.get('/doob/docker/containers/8e53f468da98/logs').success( function(response) {
        var systemlogs = response;
        for (i=0 ; i < systemlogs.length - 2 ; i++) {
            $scope.syslogs += systemlogs[i]
        }
    });

    $scope.buttonChange = function(eve){
        $scope.nowSelect = eve.target.innerHTML
        if($scope.nowSelect == $scope.selectList[0]){
            $scope.logsInfo = $scope.syslogs
        }else{
            $scope.logsInfo = '我不是全部的log'
        }
    }

    $scope.closeLogsInfo = function(){
        $scope.logsInfo = ""
    }

    $scope.changeNowCon = function(eve){
        $scope.nowCon  = eve.target.nextElementSibling.innerHTML
        $http.get('/doob/docker/containers/'+$scope.nowCon+'/logs').success( function(response) {
            var systemlogs = response;
            $scope.syslogs = ''
            for (i=0 ; i < systemlogs.length - 2 ; i++) {
                $scope.syslogs += systemlogs[i]+'\n'
            }
            $scope.logsInfo = $scope.syslogs
        });
    }

    $scope.amendConName = function(name){
        if(typeof(name) == 'object' ) return name[0].substring(1)
        return name
    }

})
