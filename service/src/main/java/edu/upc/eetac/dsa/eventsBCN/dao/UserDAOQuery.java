package edu.upc.eetac.dsa.eventsBCN.dao;

/**
 * Created by Aitor on 24/10/15.
 */
public interface UserDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_USER = "insert into users (id, name, password, email, photo) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?)";
    public final static String ASSIGN_ROLE = "insert into user_roles (userid, role) values (UNHEX(?), ?)";
    public final static String ASSIGN_CATEGORIE = "insert into categories_user (userid, category) values (UNHEX(?), ?)";
    public final static String GET_USER_BY_ID = "select hex(u.id) as id, u.name as name, u.email as email, u.photo as photo from users u where id=unhex(?)";
    public final static String GET_USER_BY_NAME = "select hex(u.id) as id, u.name as name, u.email as email, u.photo as photo from users u where u.name=?";
    public final static String UPDATE_USER = "update users set name=?, email=?, photo=? where id=unhex(?)";
    public final static String GET_PASSWORD =  "select hex(password) as password from users where id=unhex(?)";
    public final static String FOLLOW_USER = "insert into r_users (referenceid,followerid) values(UNHEX(?),UNHEX(?))";
    public final static String UNFOLLOW_USER = "delete from r_users where referenceid=unhex(?) and followerid=unhex(?)";
    public final static String COMPARE_USER_FOLLOW =  "select hex(followerid) as followerid from r_users where referenceid=UNHEX(?) and followerid=UNHEX(?)";
    public final static String CATEGORIES_BY_USERID = "select category from categories_user where userid=unhex(?)";
    public final static String DELETE_CATEGORIES = "delete from categories_user where userid=unhex(?)";
}
