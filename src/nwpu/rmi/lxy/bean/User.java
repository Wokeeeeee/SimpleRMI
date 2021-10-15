package nwpu.rmi.lxy.bean;

import java.io.Serializable;

//用户名、密码
public class User implements Serializable {
    private String name;
    private String password;

    public User(String _name,String _password){
        super();
        name=_name;
        password=_password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User |" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ' ';
    }
}
