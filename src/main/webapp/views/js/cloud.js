var app = angular.module('containersApp', []);
app.directive('runparameter', function() {
    return {
        restrict: 'E',
        template: '<div class="form-group">' +
                    '<div class="div_horizontal">' +
                        '<div align="center"><a class="btn btn-success" ng-click="addpara()"> <i class="fa fa-plus-square fa-lg"></i>添加参数 </a></div>' +
                        '<div ng-repeat="paraid in paraids">'+
                            '<div class="div_horizontal"><input type="text" class="form-control" id="para_key_{{paraid}}" name="para_key_{{paraid}}" placeholder="参数名{{paraid}}"></div>' +
                            '<div class="div_horizontal"><input type="text" class="form-control" id="para_value_{{paraid}}" name="para_value_{{paraid}}" placeholder="参数值{{paraid}}"></div>' +
                        '</div>'+
                    '</div>' +
                    '<div class="div_horizontal">' +
                        '<div align="center"><a class="btn btn-success" ng-click="addport()"> <i class="fa fa-plus-square fa-lg"></i>添加端口 </a></div>' +
                        '<div ng-repeat="portid in portids">'+
                            '<div class="div_horizontal"><input type="text" class="form-control" id="port_key_{{portid}}" name="port_key_{{portid}}" placeholder="主机端口{{portid}}"></div>' +
                            '<div class="div_horizontal"><input type="text" class="form-control" id="port_value_{{portid}}" name="port_value_{{portid}}" placeholder="容器端口{{portid}}"></div>' +
                        '</div>'+
                    '</div>' +
                '</div>',
        replace: true ,
        link : function(scope , element){
            scope.paraids = new Array()
            scope.portids = new Array()
            var i = scope.paraids.push(1)
            var j = scope.portids.push(1)
            scope.addpara = function(){
                i = scope.paraids.push(i+1)
            }
            scope.addport = function(){
                j = scope.portids.push(j+1)
            }
        }
    };
});
app.controller('containers', function($scope , $http) {
    $scope.fromData = {}
    var url = new Array()
    url[0] = "/doob/docker/containers/json"
    url[1] = "/doob/docker/images/json"
    $scope.init()

    $scope.init = function() {
        $http.get(url[0]).success(function (response) {
            $scope.containers = response;
        });
        $http.get(url[1]).success(function (response) {
            $scope.images = response;
        });
    }

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
        $scope.modelTitle = "创建一个新的容器："
        $scope.modelShowValue = 3
        $('#myModal').modal('show');
    }

    $scope.signInContainer = function(){
        var url = '/doob/docker/create/' + $scope.fromData.conName
        var parameterArray = jQuery('#createCon').serializeArray()
        var paras = {}
        for(i=0;i<parameterArray.length;i++){
            paras[parameterArray[i].name] = parameterArray[i].value
        }
        $http({
            method: 'POST',
            url: url,
            params : paras ,
        }).success(function(data){
            $scope.init()
            jQuery('#myModal').modal('hide');
        })
    }

    $scope.deleteContainer = function (containers) {
        var handleurl = '/doob/docker/containers/' + $scope.containerInfo.Id
        $http.delete(handleurl).success(function(data){
            $scope.init()
            alert("delete success!")
        })
    }
    $scope.deleteImage = function(id){
        var handleurl = '/doob/docker/images/' + id
        $http.delete(handleurl).success(function(data){
            $scope.init()
            alert("delete success!")
        })
    }
    $scope.handleContainer = function(handleName){
        var handleurl = '/doob/docker/containers/' + $scope.containerInfo.Id + '/'+ handleName
        $http.post(handleurl).success(function(data){
            $scope.init()
            alert("handle success!")
        })
    }

});

