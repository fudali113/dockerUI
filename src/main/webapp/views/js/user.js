var userinfo = []
var app = angular.module('userApp', []);
app.controller('user', function($scope , $http) {

  $scope.updatePara = {} //储存上传参数

  $scope.update_pass = false
  $scope.update_pass_count = 0

  $scope.init = function () {
    $http.get("/doob/userinfo").success(function (response) {
      var u = response.userinfo

      window.parent.mydaovoice(u)

      userinfo = ["", u.phone, u.email, u.conmax, u.imagemax]
      $scope.updatePara = u
      $scope.updatePara.pass = ""
      $scope.updatePara.pass1 = ""
    });
  }
  $scope.init()

  $scope.showupdate_pass = function(){
    if( $scope.update_pass_count % 2 == 0)
      $scope.update_pass = true
    else $scope.update_pass = false
    $scope.update_pass_count++
  }

  $scope.update = function(){
    if($scope.updatePara.pass != $scope.updatePara.pass1){
      alert('两次密码输入必须一致！')
      return
    }
    var para = notChangeIsUndefined(userinfo , $scope.updatePara ,['pass','phone','email','conmax','imagemax'])
    if(para == undefined) return
    para.pass1 = undefined
    $http({
      method: 'POST',
      url: '/doob/tables/update/user' ,
      params : para ,
    }).success(function(data){
      if(data.result != 1) return
      $scope.init()
      alert('修改成功！')
    }).error(function(){
      alert('修改失败！')
    })
  }

})

var notChangeIsUndefined = function(o1 ,o2 ,para){
  var count = 0
  for (i=0;i<para.length;i++){
    if(o1[i] == o2[para[i]]){
      o2[para[i]] = undefined
      count++
    }
  }
  if(count == para.length) return undefined
  return o2
}


/*$(function () {

  var websocket = new SockJS('http://localhost:8080/doob/sockjs/mywebsocket');

  websocket.onopen = function (evnt) {
    $("#msgcount").html("<p>"+ 'OK' +"</p>")
  };
  websocket.onmessage = function (evnt) {
    $("#msgcount").append("<p>"+evnt.data+"</p>")
  };
  websocket.onerror = function (evnt) {
    $("#msgcount").html("websocket is error")
  };
  websocket.onclose = function (evnt) {
    $("#msgcount").html("websocket连接已经中断")
  }

})*/

