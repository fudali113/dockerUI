var app = angular.module('userApp', []);
app.controller('user', function($scope , $http) {
  $scope.update_pass = false
  $scope.update_pass_count = 0

  $http.get("/doob/userinfo").success(function (response) {
    $scope.userinfo = response.userinfo;
  });

  $scope.showupdate_pass = function(){
    if( $scope.update_pass_count % 2 == 0)
      $scope.update_pass = true
    else $scope.update_pass = false
    $scope.update_pass_count++
  }
})



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

