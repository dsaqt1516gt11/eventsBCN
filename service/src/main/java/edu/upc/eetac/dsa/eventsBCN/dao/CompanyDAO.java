package edu.upc.eetac.dsa.eventsBCN.dao;

import java.sql.SQLException;

/**
 * Created by Aitor on 30/11/15.
 */
public interface CompanyDAO {
    public boolean assisttoEvent(String id, String groupid) throws SQLException, UserAlreadyAssisttoEvent;

    public boolean didntassit(String id, String eventid) throws SQLException, UserDidntAssistException;

    public boolean checkUser(String userid, String id) throws SQLException;
}
