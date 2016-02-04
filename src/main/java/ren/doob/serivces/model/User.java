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
}
