var app = angular.module('yanshiApp', []);

app.filter('containerNameFilter',function (){
    return function (input){
        name = input.substring(1)
        names = name.split("_")
        simpleName = names[names.length - 1]
        return simpleName
    }
})

app.controller('logs', function($scope , $http) {
    $scope.nowCon = "..."
    $scope.nowSelect = "所有"
    $scope.selectList = ['所有','系统','访问','应用']

    var url = "/doob/docker/cons/2"
    $http.get(url).success( function(response) {
        var list = new Array()
        for (i=0 ; i < response.length ; i++){
            list.push(JSON.parse(response[i]))
        }
        $scope.containers = list;
    });

    $http.get('/doob/docker/containers/8e53f468da98/logs').success( function(response) {
        var systemlogs = response;
        for (i=0 ; i < systemlogs.length - 2 ; i++) {
            $scope.syslogs += systemlogs[i]
        }
    });

    $scope.buttonChange = function(eve){
        $scope.nowSelect = eve.target.innerHTML
        if($scope.nowSelect == $scope.selectList[0] || $scope.nowSelect == $scope.selectList[1]){
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

    $scope.getDockerPng = function(index){
        var num = index%5
        return '/doob/views/images/dockerImages/docker00'+num+'.png'
    }

})
