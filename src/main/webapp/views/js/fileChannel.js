var info = new Array();
var uploadpara = {}
var nowpath = ""
var yeshu = 1
var meiyeshuju = 12

$(function () {

    upload(uploadpara)

    $("#fileTableTbody").delegate('tr td[id != null]' , "click" , function(){
        console.log(this.id)
        if(this.id != null && this.id != undefined && this.id != '')
            uploadpara.sshcdpath = info[this.id].absolutePath;
        else
            return
        upload(uploadpara)
    })

    $("#fileTableTbody").delegate('tr td[id != ]' , "mouseenter" , function(){
        var $color = {}
        if(this.id != null && this.id != undefined && this.id != '') $color = $(this).addClass("success")
        else $color = $(this).addClass("danger")

        $color.parent().siblings().children().removeClass()
    })

})

/**
 * 接收一个请求参数，发出请求并调用loadinfo加载数据
 * @param uploadpara
 */
var upload = function(uploadpara){
    $.ajax({
        type : "POST",
        url : "/doob/ssh/file/cd",
        data : uploadpara,
        dataType : "json",
        success : function(data){
            if (loginRightsChucke(data)) return
            info = data.ssh_info == null ? alert('请求数据为空') : data.ssh_info;
            nowpath = data.nowpath;
            yeshu = 1
            if(info != null) loadinfo(info , nowpath)

        },
        error : function(){
            alert("请求失败！")
        }
    })
}


/**
 * 获得信息并加载在页面
 * @param info
 */
var loadinfo = function(info , nowpath){
    var i = (yeshu-1)*meiyeshuju
    var j = (i + meiyeshuju > info.length) ? info.length : i+meiyeshuju
    var html = ""
    for(i ; i < j ; i++ ){
        html += '<tr>'+
                    '<td>'+ (i+1) + '</td>'+
                    '<td id="'+ i +'">' + info[i].filename +'</td>'+
                    '<td>' + info[i].attributes.permissionsString + '</td>'+
                    '<td>' + info[i].attributes.size + '</td>'+
                    '<td>' + info[i].attributes.modTimeString + '</td>'+
                    '<td>' + isdirectory(info[i].directory) + '</td>'+
                    '<td>' + '<button type="button" class="btn btn-success" id="downfile'+ i +'">下载文件</button>'
                '</tr>'
    }
    $('#fileTableTbody').html(html)
    $('#nowpath').html('当前目录：' + nowpath)
    $('#totalpages').html('#'+maxpageno(info)+'@')
}
//判断是否为文件夹
var isdirectory = function(a){
    if(a) return '目录'
    else return '文件'
}

//下一页点击实现的功能
var xiayy = function(){
    yeshu=parseInt($('#yes').val())+1;
    xianzhi(yeshu,info)
    $('#yes').val(yeshu);
    loadinfo(info , nowpath);
}
//上一页
var shangyy = function(){
    yeshu=parseInt($('#yes').val())-1;
    xianzhi(yeshu , info)
    $('#yes').val(yeshu);
    loadinfo(info , nowpath);
}
//转到页数
var zhuand = function(){
    yeshu=parseInt($('#yes').val());
    xianzhi(yeshu ,info)
    loadinfo(info , nowpath);
}
//不要页数超过指定范围
var xianzhi = function(ys , info){
    var max = maxpageno(info)
    var min = 1
    if(ys > max) yeshu = max;
    if(ys < min) yeshu = min;
}
//计算最大页数
var maxpageno = function(info){
    return info.length % meiyeshuju == 0 ? parseInt(info.length/meiyeshuju) : parseInt(info.length/meiyeshuju) + 1
}