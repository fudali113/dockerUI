$(function () {

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

})

