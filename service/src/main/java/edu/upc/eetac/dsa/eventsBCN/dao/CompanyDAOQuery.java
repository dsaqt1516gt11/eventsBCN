package edu.upc.eetac.dsa.eventsBCN.dao;

/**
 * Created by Aitor on 30/11/15.
 */
public interface CompanyDAOQuery {

    public final static String CREATE_COMPANY = "insert into companies (id, name, description,location,coordinate,userid) values (UNHEX(?),?,?,?,geomfromtext('point(?)'),UNHEX(?))";
    public final static String GET_COMPANY_BY_ID = "select hex(id),name,description,location,coordinate,userid from companies where id=unhex(?)";
    public final static String GET_COMPANY_BY_NAME = "select hex(id),name,description,location,coordinate,userid from companies where name=?";
    public final static String GET_USER_COMPANY="select hex(u.id) as id, u.name as name, u.email as email, u.photo as photo from users u,companies c where u.id=c.userid and  c.userid=unhex(?)";
    public final static String UPDATE_COMPANY = "update events set name=?, description=?, location=?, coordinate=geomfromtext('point(?)') where id=unhex(?)";
    public final static String COMPARE_USER_EVENT =  "select hex(eventid) as eventid from user_event where userid=UNHEX(?) and eventid=UNHEX(?)";
    public final static String ASSIST_EVENT = "insert into user_event (userid, eventid) values (UNHEX(?), UNHEX(?))";
    public final static String ABSENT_EVENT = "delete from user_event where userid=unhex(?) and eventid=unhex(?)";
}
