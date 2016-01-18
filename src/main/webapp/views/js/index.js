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
                                        $('#userinfo').after('<a href="#">' + data.userinfo.name + '</a>')
                                            .hide()
                                        $('#myModal').modal('hide');
                                }else{
                                        alert('登陆失败！')
                                }
                        }
                })
        })

})