package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;

import java.sql.SQLException;

/**
 * Created by juan on 30/11/15.
 */
public interface EventDAO {

    public Event createEvent( String title, String description,String date,String photo,String category,String companyid) throws SQLException;
    public Event getEventById(String id) throws SQLException;
    public EventCollection getEventsByCategory(String category) throws SQLException;
    public EventCollection getEventsByCompany(String companyid) throws SQLException;
    public Event updateEvent(String id, String title, String description,String date,String photo,String category) throws SQLException;
    public boolean deleteEvent(String id) throws SQLException;

}
