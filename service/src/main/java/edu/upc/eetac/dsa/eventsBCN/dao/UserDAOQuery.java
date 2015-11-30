package edu.upc.eetac.dsa.eventsBCN.dao;

/**
 * Created by Aitor on 24/10/15.
 */
public interface UserDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_USER = "insert into users (id, name, password, email) values (UNHEX(?), ?, UNHEX(MD5(?)), email)";
    public final static String ASSIGN_ROLE = "insert into user_roles (userid, role) values (UNHEX(?), ?)";
    public final static String GET_USER_BY_ID = "select hex(u.id) as id, u.name as name, u.email as email from users u where id=unhex(?)";
    public final static String GET_USER_BY_NAME = "select hex(u.id) as id, u.name as name, u.email as email from users u where u.loginid=?";
    public final static String UPDATE_USER = "update users set name=?, email=? where id=unhex(?)";
    public final static String GET_PASSWORD =  "select hex(password) as password from users where id=unhex(?)";
}
