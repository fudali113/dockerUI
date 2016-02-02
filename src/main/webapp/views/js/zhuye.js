/*var app = angular.module('containersApp', []);
app.controller('containers', function($scope , $http) {
    var url="139.129.4.187:13131/containers/json"
    $http.get(url).success( function(response) {
        $scope.containers = response;
    });
});*/

$(function(){

    $.ajax({
        type : "POST",
        url : "http://139.129.4.187:13131/containers/json",
        dataType : "json",
        success : function(data){
            loadContainers(data)
        },
        error : function(){
            alert('请求失败！')
        }
    })

})

var loadContainers = function(container) {
    var html = ""
    for (var i = 0; i < data.length; i++) {
        html += "<td><a href=" + container[i].Id + ">" + container[i].name + "</a></td>" +
                "<td><a href=" + container.Image + ">" + container.Image + "</a></td>" +
                "<td>" + container.Command + "</td>" +
                "<td>" + container.Created + "</td>" +
                "<td><span class=\"label label-" + container.Status + "\">" + container.Status + "</span></td>"
    }

    $('#containers').html(html)
}
