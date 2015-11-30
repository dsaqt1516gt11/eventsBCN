package edu.upc.eetac.dsa.eventsBCN.dao;

/**
 * Created by Aitor on 30/11/15.
 */
public interface CompanyDAOQuery {
    public final static String COMPARE_USER_EVENT =  "select hex(eventid) as eventid from user_event where userid=UNHEX(?) and eventid=UNHEX(?)";
    public final static String ASSIST_EVENT = "insert into user_event (userid, eventid) values (UNHEX(?), UNHEX(?))";
    public final static String ABSENT_EVENT = "delete from user_event where userid=unhex(?) and eventid=unhex(?)";
}
