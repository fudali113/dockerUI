package ren.doob.serivces.model;

/**
 * @author fudali
 * @package ren.doob.serivces.model
 * @class User
 * @date 2015-12-21
 */

public class User {

    private Integer id;
    private String name;
    private String pass;
    private String xingm;
    private String phone;
    private String email;
    private Integer conmax;
    private Integer imagemax;
    private Integer systemadmin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getXingm() {
        return xingm;
    }

    public void setXingm(String xingm) {
        this.xingm = xingm;
    }

    public Integer getConmax() {
        return conmax;
    }

    public void setConmax(Integer conmax) {
        this.conmax = conmax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getImagemax() {
        return imagemax;
    }

    public void setImagemax(Integer imagemax) {
        this.imagemax = imagemax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSystemadmin() {
        return systemadmin;
    }

    public void setSystemadmin(Integer systemadmin) {
        this.systemadmin = systemadmin;
    }
}
