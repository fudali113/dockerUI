var app = angular.module('containersApp', []);
app.controller('containers', function($scope , $http) {
    var url = new Array()
    url[0] = "/doob/docker/containers/json"
    url[1] = "/doob/docker/images/json"
    $http.get(url[0]).success(function (response) {
            $scope.containers = response;
    });
    $http.get(url[1]).success(function (response) {
        $scope.images = response;
    });

    $scope.getIC = function(obj , ic){
        var url = obj.target.href
        var href = url.split('#')[1]

        $http.get(href).success(function(data){// i为image   c为 container
            if( ic == 'i' ) {
                $scope.imageInfo = data
                $scope.modelShowValue = 2
                $scope.modelTitle = "镜像信息："
            }
            if( ic == 'c') {
                $scope.containerInfo = data
                $scope.modelShowValue = 1
                $scope.modelTitle = "容器信息："
            }
        })

        $('#myModal').modal('show');
    }

    $scope.openSignInModel = function(){
        $scope.modelTitle = "注册一个新的容器："
        $scope.modelShowValue = 3
        $('#myModal').modal('show');
    }

    $scope.signInContainer = function(){

    }

    $scope.deleteContainer = function (containers) {
        var handleurl = '/doob/docker/containers/' + $scope.containerInfo.Id
        $http.delete(handleurl).success(function(data){
            alert("delete success!")
        })
    }
    $scope.deleteImage = function(id){
        var handleurl = '/doob/docker/images/' + id
        $http.delete(handleurl).success(function(data){
            alert("delete success!")
        })
    }
    $scope.handleContainer = function(handleName){
        var handleurl = '/doob/docker/containers/' + $scope.containerInfo.Id + '/'+ handleName
        $http.post(handleurl).success(function(data){
            alert("handle success!")
        })
    }

});

