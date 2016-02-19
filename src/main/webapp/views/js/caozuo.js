var channelid;
var xinxi;
var yesandnologin = 0;
var loginInfo = {}

$(function(){

    $('#sshdl').click(function(){
        $.ajax({
            type : "POST",
            url : "/doob/ssh/connection",
            data : $('#SSHlogin').serialize(),
            dataType : "json",
            success : function(data){
                if (loginRightsChucke(data)) return
                if(data.result == 0){
                    alert('请确认输入信息！')
                }else {
                    var loginAccept = data.loginInfo.accept
                    var html = '连接信息：'+loginAccept.ssh_ip+':'+loginAccept.ssh_host+'@'+loginAccept.ssh_name+'<br/>'+
                                '连接时间：'+getNowTime()
                    $("#loginInfoDiv").html(html)
                    loginInfo.name = loginAccept.ssh_name
                    loginInfo.ip = loginAccept.ssh_ip
                    loginInfo.port = loginAccept.ssh_host
                    loginInfo.loginTime = getNowTime()
                    yesandnologin = 1;
                    channelid = data.channelid;
                    xinxi = data.ssh_info;
                    ConnectionInfo = data.ConnectionInfo;
                    $('#shellreload').attr('src', $('#shellreload').attr('src'));
                    $('#filereload').attr('src', $('#filereload').attr('src'));
                    $('#myModal').modal('hide');
                    dqxx();
                }
            },
            error : function(){
                alert('请求失败！')
            }
        })
    })

    $('#index').siblings().hide()
    $('li a').click(function(){
        var src = this.title;
        $('#'+src).show().siblings().hide()
        $(this).addClass("active").siblings().removeClass("active")
    })

    daovoice('init', {
        app_id: "29ab36ab",
        user_id: "NO_89757", // 必填: 该用户在您系统上的唯一ID
        email: "fudali113@gmail.com", // 选填:  该用户在您系统上的主邮箱
        name: "fudali", // 选填: 用户名
        signed_up: 1449821660 // 选填: 用户的注册时间，用Unix时间戳表示
    });
    daovoice('update');
})

var loginInfoORLogin = function(){
    if(yesandnologin == 0){
        reLogin()
    }else{
        $("#loginFromDiv").hide()
        $("#loginDiv").hide()
        $("#loginInfoDiv").show()
        $("#reLoginDiv").show()
        $("#modelTitle").html("登陆信息")
    }
}

var reLogin = function(){
    $("#loginFromDiv").show()
    $("#loginDiv").show()
    $("#loginInfoDiv").hide()
    $("#reLoginDiv").hide()
    $("#modelTitle").html("登陆Shell")
}

var getNowTime = function(){
    var d = new Date()
    return d.getFullYear()+'-'+ (d.getMonth()+1)+'-'+ d.getDate()+' '+ d.getHours()+':'+ d.getMinutes()+':'+ d.getSeconds()+'    星期'+ d.getDay()
}