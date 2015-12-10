package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;

import java.sql.SQLException;

/**
 * Created by juan on 30/11/15.
 */
public interface EventDAO {
    public boolean assisttoEvent(String id, String eventid) throws SQLException, UserAlreadyAssisttoEvent;
    public boolean wontassit(String id, String eventid) throws SQLException, UserWontAssistException;
    public boolean checkUser(String id, String eventid) throws SQLException;
    public Event createEvent(Event event) throws SQLException, EventAlreadyExistsException;
    public Event getEventById(String id) throws SQLException;
    public Event getEventByTitle(String title) throws SQLException;
    public EventCollection getEvents() throws SQLException;
    public EventCollection getEventsByCategories(String id) throws SQLException;
    public EventCollection getEventsByCompany(String idcompany) throws SQLException;
    public Event updateEvent(Event event) throws SQLException;
    public boolean deleteEvent(String eventid) throws SQLException;

}
