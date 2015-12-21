package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 30/11/15.
 */
public class EventDAOImpl implements EventDAO {

    @Override
    public Event createEvent(Event event) throws SQLException, EventAlreadyExistsException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();
            Event ev = getEventByTitle(event.getTitle());
            if (ev != null)
                throw new EventAlreadyExistsException();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();
            System.out.println("Vamos ha hacer la query!");
            stmt = connection.prepareStatement(EventDAOQuery.CREATE_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, event.getTitle());
            stmt.setString(3, event.getDescription());
            stmt.setString(4, event.getDate());
            stmt.setString(5, event.getPhoto());
            stmt.setString(6, event.getCategory());
            stmt.setString(7, event.getCompanyid());
            stmt.executeUpdate();
        } catch (SQLException e) {

            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getEventById(id);
    }

    @Override
    public Event getEventById(String id) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getString("date"));
                event.setPhoto(rs.getString("photo"));
                event.setCategory(rs.getString("category"));
                event.setCompanyid(rs.getString("companyid"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());

            }

            stmt.close();
            //consultar los usuarios que van ha asistir
            UserDAO userDAO = new UserDAOImpl();
            List<String> idusers = new ArrayList<>();

            stmt = connection.prepareStatement(EventDAOQuery.GET_IDUSERS_ASSIST_TO_EVENT);
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            System.out.println("Primera consulta, ahora vamos a guardar el resultado en una lista");
            while (resultSet.next()) {
                idusers.add(resultSet.getString("userid"));
            }
            List<User> users = new ArrayList<>();
            for( String iduser : idusers ) {
               users.add(userDAO.getUserById(iduser));
            }
            if (users!=null)
                event.setUsers(users);
            stmt.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return event;
    }


    @Override
    public Event getEventByTitle(String title) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_TITLE);
            stmt.setString(1, title);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getString("date"));
                event.setPhoto(rs.getString("photo"));
                event.setCategory(rs.getString("category"));
                event.setCompanyid(rs.getString("companyid"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return event;
    }


    @Override
    public EventCollection getEvents() throws SQLException{

        EventCollection eventCollection = new EventCollection();

        Connection connection = null;

        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getString("date"));
                event.setPhoto(rs.getString("photo"));
                event.setCategory(rs.getString("category"));
                event.setCompanyid(rs.getString("companyid"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());
                eventCollection.setOldestTimestamp(event.getLastModified());
                eventCollection.getEvents().add(event);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    @Override
    public EventCollection getEventsByCategories(String id) throws SQLException{

        EventCollection eventCollection = new EventCollection();
        System.out.println("ID:" + id );
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            //consultar las categorias de un usuario
            List<String> categories = new ArrayList<>();


            stmt = connection.prepareStatement(UserDAOQuery.CATEGORIES_BY_USERID);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Primera consulta, ahroa vamos a guardar el resultado en una lista");
            while (rs.next()) {
                System.out.println("antes de añadir");
                System.out.println(rs.getString("category"));
                categories.add(rs.getString("category"));
                System.out.println("Estamos dentro del bucle");
            }
            System.out.println("cojo categorias");
            System.out.println(categories);

            for (String category : categories) {
                System.out.println(category);
                stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_CATEGORY);
                stmt.setString(1, category);
                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    Event event = new Event();
                    event.setId(res.getString("id"));
                    event.setTitle(res.getString("title"));
                    event.setDescription(res.getString("description"));
                    event.setDate(res.getString("date"));
                    event.setPhoto(res.getString("photo"));
                    event.setCategory(res.getString("category"));
                    event.setCompanyid(res.getString("companyid"));
                    event.setCreationTimestamp(res.getTimestamp("creation_timestamp").getTime());
                    event.setLastModified(res.getTimestamp("last_modified").getTime());

                    eventCollection.setOldestTimestamp(event.getLastModified());
                    eventCollection.getEvents().add(event);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    @Override
    public EventCollection  getEventsByCompany(String idcompany) throws SQLException {
        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_COMPANY);
            stmt.setString(1, idcompany);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getString("date"));
                event.setPhoto(rs.getString("photo"));
                event.setCategory(rs.getString("category"));
                event.setCompanyid(rs.getString("companyid"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified());

                eventCollection.getEvents().add(event);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return eventCollection;
    }

    @Override
    public boolean deleteEvent(String eventid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.DELETE_EVENT);
            stmt.setString(1, eventid);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public Event updateEvent(Event event) throws SQLException{
        Event ev = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.UPDATE_EVENT);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getDate());
            stmt.setString(4, event.getPhoto());
            stmt.setString(5, event.getCategory());
            stmt.setString(6, event.getId());

            int rows = stmt.executeUpdate();
            if (rows == 1)
                ev = getEventById(event.getId());
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return ev;
    }

    @Override
    public boolean assisttoEvent(String id, String eventid) throws SQLException, UserAlreadyAssisttoEvent {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            boolean isAssisted = checkUser(id, eventid);
            if (isAssisted == true)
                throw new UserAlreadyAssisttoEvent();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.ASSIST_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, eventid);
            stmt.executeUpdate();

            //meter el usuario en la lista de usuarios q asisten al evento

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return true;
    }

    @Override
    public boolean wontassit(String id, String eventid) throws SQLException, UserWontAssistException {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {

            boolean isAssisted = checkUser(id, eventid);
            if (isAssisted != true)
                throw new UserWontAssistException();

            System.out.println(isAssisted);
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.ABSENT_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, eventid);

            int rows = stmt.executeUpdate();
            System.out.println(rows);
            if (rows == 1) return true;
            else return false;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean checkUser(String id, String eventid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean a = false;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.COMPARE_USER_EVENT);

            stmt.setString(1, id);
            stmt.setString(2, eventid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                a = true;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return a;
    }
}
