package edu.upc.eetac.dsa.eventsBCN.dao;

/**
 * Created by Aitor on 30/11/15.
 */
public interface CompanyDAOQuery {

    public final static String CREATE_COMPANY = "insert into companies (id, name, description,location,latitude, longitude,userid) values (UNHEX(?),?,?,?,?,?,UNHEX(?))";
    public final static String GET_COMPANY_BY_ID = "select hex(c.id) as id, c.name as name, c.description as description, c.location as location, c.latitude as latitude, c.longitude as longitude, hex(c.userid) as userid from companies c where id=unhex(?)";
    public final static String GET_COMPANY_BY_NAME = "select hex(c.id) as id, c.name as name, c.description as description, c.location as location, c.latitude as latitude, c.longitude as longitude, hex(c.userid) as userid from companies c where c.name=?";
    public final static String UPDATE_COMPANY = "update companies set name=?, description=?, location=?, latitude=?, longitude=? where id=unhex(?)";
    public final static String COMPARE_USER_EVENT =  "select hex(eventid) as eventid from user_event where userid=UNHEX(?) and eventid=UNHEX(?)";
    public final static String ASSIST_EVENT = "insert into user_event (userid, eventid) values (UNHEX(?), UNHEX(?))";
    public final static String ABSENT_EVENT = "delete from user_event where userid=unhex(?) and eventid=unhex(?)";
    public final static String GET_COMPANY_FROM_USER = "select hex(c.id) as id from companies c where userid=unhex(?)";
}
