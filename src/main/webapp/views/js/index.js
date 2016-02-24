$(function(){

        $('#login').show();$('#signin').hide();

        $('#login').click(function(){
                $.ajax({
                        type : "POST",
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

        $('#signin').click(function(){
                $.ajax({
                        type : "POST",
                        url : "/doob/signin",
                        data : $('#zhuce').serialize(),
                        dataType : "json",
                        success : function(data){
                                if(data.signin == 1) {
                                        alert('注册成功！')
                                        $('#firstname').val($('#name').val())
                                        $('#lastname').val($('#pass').val())
                                }
                                else alert("注册失败！")
                        },
                        error : function(data){
                                alert('注册失败！')
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
        $('#userinfo').html(data.userinfo.name)
        $('#myModal').modal('hide');
}

var LoginRights = function(){//检查是否登陆
        var userinfo = $('#userinfo').html();
        if(userinfo == '登陆/注册'){
                $('#myModal').modal('show');
                return false;
        }
}