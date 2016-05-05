var channelid;
var xinxi;
var yesandnologin = 0;
var loginInfo = {}

$(function(){

    $.get("/doob/ssh/isCon",function(data){
        shellConLoad(data)
    })

    gethostinfo()

    $('#sshdl').click(function(){
        $('#sshdl').attr('disabled',true)
        $.ajax({
            type : "POST",
            url : "/doob/ssh/connection",
            data : $('#SSHlogin').serialize(),
            dataType : "json",
            success : function(data){
                if (loginRightsChucke(data))
                if(data.result == 0){
                    alert('请确认输入信息！')
                    return
                }
                shellConLoad(data)
                gethostinfo()
                $('#sshdl').attr('disabled',false)
            },
            error : function(){
                alert('请求失败！')
                $('#sshdl').attr('disabled',false)
            }
        })
    })

    $('#index').siblings().hide()
    $('#mymenuul > li a').click(function(){
        var src = this.title;
        $('#'+src).show().siblings().hide()
        $('#mymenuul > li a').removeClass("mymenuactive")
        $(this).addClass("mymenuactive")
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

var gethostinfo = function(){
    $.get("/doob/ssh/hostInfo",function(data){
        var hostInfoList = data.hostInfo
        var html=""
        for(i=0;i<hostInfoList.length;i++){
            var onlineshellid = hostInfoList[i].id == data.onlineShell ? '  class="success"':''
            html += '<tr '+onlineshellid+' onclick="recon('+hostInfoList[i].id+')">'+
                '<td>'+hostInfoList[i].ip+'</td>'+
                '<td>'+hostInfoList[i].port+'</td>'+
                '<td>'+hostInfoList[i].name+'</td>'+
                '</tr>'
        }
        $('#shellHostInfo').html(html)
    })
}

var shellConLoad = function(data){
    if(data == undefined) return
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
    $('#myModal').modal('hide');
    //刷新两个页面
    $("#shellreload")[0].contentWindow.dqxx(data.ssh_info);
    $("#filereload")[0].contentWindow.upload({});
}

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

var recon = function(no){
    $.ajax({
        type : "POST",
        url : "/doob/ssh/recon/"+no,
        dataType : "json",
        success : function(data){
            if(data.recon == 0) alert("重新登陆失败！")
            else{
                gethostinfo()
                //刷新两个页面
                $("#shellreload")[0].contentWindow.load();
                $("#filereload")[0].contentWindow.upload({});
            }
        }
    })
}
