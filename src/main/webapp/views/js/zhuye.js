var app = angular.module('containersApp', []);
app.controller('containers', function($scope , $http) {

    var url = "/doob/docker/containers/json"
    $http.get(url).success( function(response) {
        $scope.containers = response;
    });

    $scope.getIC = function(obj , ic){
        var url = obj.target.href
        var href = url.split('#')[1]

        $http.get(href).success(function(data){
            if( ic == 'i' ) {
                $scope.imageInfo = data
                $scope.imageName = true
                $scope.containerName = false
                $scope.modelTitle = "镜像信息："
            }
            if( ic == 'c') {
                $scope.containerInfo = data
                $scope.containerName = true
                $scope.imageName = false
                $scope.modelTitle = "容器信息："
            }
        })

        $('#myModal').modal('show');
    }

});

