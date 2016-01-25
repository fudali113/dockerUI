$(function(){

        $('#iframe').hide();

        $('#login').click(function(){
                $.ajax({
                        type : "GET",
                        url : "/doob/login",
                        data : $('#denglu').serialize(),
                        dataType : "json",
                        success : function(data){
                                if (data.userinfo != null){
                                        userinfo(data)
                                }else{
                                        alert('登陆失败！')
                                }
                        }
                })
        })

        $.ajax({//如果用户信息存在session，则显示用户信息
                type : "GET",
                url : "/doob/userinfo",
                dataType : "json",
                success : function(data){
                        if (data.userinfo != null){
                                userinfo(data)
                        }
                }
        })

})

var userinfo = function(data){//登陆后把登陆按钮换为用户名
        $('#userinfo').after('<a href="#" id="userinfo_name">' + data.userinfo.name + '</a>')
            .hide()
        $('#myModal').modal('hide');
}

var LoginRights = function(){//检查是否登陆
        var userinfo = $('#userinfo_name').html();
        if(userinfo == '' || userinfo == undefined){
                $('#myModal').modal('show');
                return false;
        }
}