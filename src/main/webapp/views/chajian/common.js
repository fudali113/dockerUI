/**
 * 检查用户是否登陆
 * @param data
 * @returns {boolean}
 */
var loginRightsChucke = function (data) {
  if (data.loginRightsChucke == 0){
    alert("请登录！")
    return true
  }
  return false
}

var sshLoginRightsChucke = function(data){
  if(data.sshNoLogin == "1"){
    //alert("请登录ssh")
    return true
  }
}
