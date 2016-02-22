var xinxi = new Array();

$(function(){

    $('#mingl').bind('keypress',function(event){
        if(event.keyCode == "13") $('#scml').click();
    });

    /*$('#sshdl').click(function(){
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
                    channelid = data.channelid;
                    xinxi = data.ssh_info;
                    ConnectionInfo = data.ConnectionInfo;
                    $('#myModal').modal('hide');
                    dqxx();
                }
            },
            error : function(){
                alert('请求失败！')
            }
        })
    })*/

    $('#scml').click(function(){
        var mingl = {};
        mingl.ssh_mingl = $('#mingl').val();
        $.ajax({
            type : "POST",
            url : "/doob/ssh/shell/handle",
            data : mingl,
            dataType : "json",
            success : function(data){
                if (loginRightsChucke(data)) return
                xinxi = data.ssh_info;
                dqxx(xinxi);
            },
            error : function(){
                alert('请求失败！')
            }
        })

        $('#mingl').val('');

    })

    loclafocusblur();

})

/**
 * 读取信息
 * @param xinxi
 */
var dqxx = function(xinxi){
    var html = "";
    for (var i=0 ;i < xinxi.length ;i++){
        if(xinxi[i] != "") html += xinxi[i]+"\n";
    }
    $('#shuc').val(html);
}

var loclafocusblur = function(){
    var i;
    var ssh = ['139.129.4.187' , '22' , 'root' , '******']
        $('#SSHlogin div input').focus(function(){
            i = parseInt(this.id.substring(3)) - 1
            if($(this).val() == ssh[i]) {
                $(this).val("");
            }
        }).blur(function(){
            if ($(this).val() == '') {
                $(this).val(ssh[i]);
            }
        });
}

