package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: my_batis
 * @description:
 * @author: Kim_yuan
 * @create: 2020-09-20 13:52
 **/

public class User implements Serializable {
    private Integer id;
    private String username;
    private Date birthday;
    private String address;
    private String sex;

    /*
     * 一個用戶可以有多個角色
     * 建立多對多關係映射
     * */
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    /*
     * 一個用戶可以有多個賬戶,
     * 一對多關係映射: 主表實體應該包含從表實體的集合引用
     **/

    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", sex='" + sex + '\'' +
                ", roles=" + roles +
                '}';
    }
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", birthday=" + birthday +
//                ", address='" + address + '\'' +
//                ", sex='" + sex + '\'' +
//                ", accounts=" + accounts +
//                '}';
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
