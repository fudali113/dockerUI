var channelid = "";
var xinxi = new Array();
var ConnectionInfo = "";

$(function(){

    $('#mingl').bind('keypress',function(event){
        if(event.keyCode == "13") $('#scml').click();
    });

    $('#sshdl').click(function(){
        $.ajax({
            type : "POST",
            url : "/doob/SSHLogin",
            data : $('#SSHlogin').serialize(),
            dataType : "json",
            success : function(data){
                if(data.result == 0){
                    alert('请确认输入信息！')
                }else {
                    channelid = data.channelid;
                    xinxi = data.xinxi;
                    ConnectionInfo = data.ConnectionInfo;
                    $('#myModal').modal('hide');
                    dqxx();
                }
            },
            error : function(){
                alert('请求失败！')
            }
        })
    })

    $('#scml').click(function(){
        var mingl = {};
        mingl.channelid = channelid;
        mingl.mingl = $('#mingl').val();
        mingl.ConnectionInfo = ConnectionInfo;
        $.ajax({
            type : "POST",
            url : "/doob/SSHcaozuo",
            data : mingl,
            dataType : "json",
            success : function(data){
                xinxi = data.xinxi;
                dqxx();
            },
            error : function(){
                alert('请求失败！')
            }
        })

        $('#mingl').val('');
    })
})

/**
 * 读取信息
 * @param xinxi
 */
var dqxx = function(){
    var html = "";
    for (var i=0 ;i < xinxi.length ;i++){
        if(xinxi[i] != "") html += xinxi[i]+"\n";
    }
    $('#shuc').val(html);
}
