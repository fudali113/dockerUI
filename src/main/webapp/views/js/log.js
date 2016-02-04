$(function () {

  var websocket = new SockJS('http://localhost:8080/doob/sockjs/mywebsocket');

  websocket.onopen = function (evnt) {
  };
  websocket.onmessage = function (evnt) {
    $("#msgcount").html("(<font color='red'>"+evnt.data+"</font>)")
  };
  websocket.onerror = function (evnt) {
  };
  websocket.onclose = function (evnt) {
  }

})

