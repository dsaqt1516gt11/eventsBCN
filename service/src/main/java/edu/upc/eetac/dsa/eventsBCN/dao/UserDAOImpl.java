package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Aitor on 24/10/15.
 */
public class UserDAOImpl implements UserDAO {
    @Override
    public User createUser(User user, String role) throws SQLException, UserAlreadyExistsException{
        System.out.println("Estoy dentro de createUser");
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            System.out.println(user.getName());
            User u = getUserByName(user.getName());
            if (u != null)
                throw new UserAlreadyExistsException();
            System.out.println("De vuelta a createUser");
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                user.setId(rs.getString(1));
            else
                throw new SQLException();

            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(UserDAOQuery.CREATE_USER);
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoto());
            stmt.executeUpdate();
            System.out.println("Usuario creado");

            stmt = connection.prepareStatement(UserDAOQuery.ASSIGN_ROLE);
            stmt.setString(1, user.getId());
            stmt.setString(2, role);
            stmt.executeUpdate();
            System.out.println("Rol asignado");

            System.out.println(role);
            if(role.equals("registered")) {
                System.out.println("entro al if");
                for (String nombre : user.getCategories()) {
                    if (nombre != null && nombre != "") {
                        stmt = connection.prepareStatement(UserDAOQuery.ASSIGN_CATEGORIE);
                        stmt.setString(1, user.getId());
                        stmt.setString(2, nombre);
                        stmt.executeUpdate();
                    }

                }
                System.out.println("relaciones usuario categorias creadas");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getUserByName(user.getName());
    }

    @Override
    public User getUserById(String id) throws SQLException{
        //System.out.println("Estoy dentro de getUserById");
        // Modelo a devolver
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(UserDAOQuery.GET_USER_BY_ID);
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet res = stmt.executeQuery();

            // Procesa los resultados
            if (res.next()) {
                user = new User();
                user.setId(res.getString("id"));
                user.setName(res.getString("name"));
                user.setEmail(res.getString("email"));
                user.setPhoto(res.getString("photo"));
            }

            //consultar las categorias de un usuario
            List<String> categories = new ArrayList<>();

            stmt = connection.prepareStatement(UserDAOQuery.CATEGORIES_BY_USERID);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            //System.out.println("Primera consulta, ahroa vamos a guardar el resultado en una lista");
            while (rs.next()) {
                //System.out.println("antes de añadir");
                //System.out.println(rs.getString("category"));
                categories.add(rs.getString("category"));
                //System.out.println("Estamos dentro del bucle");
            }
            //System.out.println("cojo categorias");
            //System.out.println(categories);
            user.setCategories(categories);
        } catch (SQLException e) {
            // Relanza la excepción
            throw e;
        } finally {
            // Libera la conexión
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        // Devuelve el modelo
        return user;

    }

    @Override
    public User getUserByName(String name) throws SQLException{
        System.out.println("Estoy dentro de getUserByName");
        // Modelo a devolver
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(UserDAOQuery.GET_USER_BY_NAME);
            stmt.setString(1, name);

            // Ejecuta la consulta
            ResultSet res = stmt.executeQuery();
            System.out.println("paso la query");


            // Procesa los resultados
            while (res.next()) {
                System.out.println("no deberia entrar");
                user = new User();
                user.setId(res.getString("id"));
                user.setName(res.getString("name"));
                user.setEmail(res.getString("email"));
                user.setPhoto(res.getString("photo"));
                System.out.println("categorias principales");
            }

            //pillar las categoiras de un usuario
            if(user!=null) {
                //consultar las categorias de un usuario
                List<String> categories = new ArrayList<>();

                stmt = connection.prepareStatement(UserDAOQuery.CATEGORIES_BY_USERID);
                stmt.setString(1, user.getId());
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
                user.setCategories(categories);
            }
        } catch (SQLException e) {
            // Relanza la excepción
            throw e;
        } finally {
            // Libera la conexión
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        // Devuelve el modelo
        return user;
    }

    @Override
    public User updateProfile(User user, String role) throws SQLException {
        User u = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UPDATE_USER);
            stmt.setString(4, user.getId());
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoto());

            int rows = stmt.executeUpdate();
            if (rows == 1)
                u = getUserById(user.getId());

            if(role.equals("registered")) {
                stmt = connection.prepareStatement(UserDAOQuery.DELETE_CATEGORIES);
                stmt.setString(1, user.getId());
                stmt.executeUpdate();
                System.out.println("Categorias eliminadas");

                for (String nombre : user.getCategories()) {
                    if (nombre != null && nombre != "") {
                        stmt = connection.prepareStatement(UserDAOQuery.ASSIGN_CATEGORIE);
                        stmt.setString(1, user.getId());
                        stmt.setString(2, nombre);
                        stmt.executeUpdate();
                    }
                }
                System.out.println("relaciones usuario categorias creadas");

            }


        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return u;
    }

    @Override
    public boolean checkPassword(String id, String password) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.GET_PASSWORD);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes());
                    String passedPassword = new BigInteger(1, md.digest()).toString(16);

                    return passedPassword.equalsIgnoreCase(storedPassword);
                } catch (NoSuchAlgorithmException e) {
                }
            }
            return false;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean userFollow(String userfollowid, String userFollowerid) throws  SQLException, UserAlreadyFollowedException{
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection();
            boolean isFollowed = checkFollow(userfollowid,userFollowerid);
            if (isFollowed == true)
                throw new UserAlreadyFollowedException();

            System.out.println(isFollowed);
            stmt = connection.prepareStatement(UserDAOQuery.FOLLOW_USER);
            stmt.setString(1, userfollowid);
            stmt.setString(2, userFollowerid);

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
    public boolean userUnfollow(String userfollowid, String userFollowerid) throws SQLException,UserNotFollowedException{

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection();
            boolean isFollowed = checkFollow(userfollowid,userFollowerid);
            if (isFollowed != true)
                throw new UserNotFollowedException();

            System.out.println(isFollowed);

            stmt = connection.prepareStatement(UserDAOQuery.UNFOLLOW_USER);
            stmt.setString(1, userfollowid);
            stmt.setString(2, userFollowerid);

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
    public boolean checkFollow(String referenceid, String followerid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean a=false;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.COMPARE_USER_FOLLOW);

            stmt.setString(1, referenceid);
            stmt.setString(2, followerid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { a=true;}
        }catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return a;
    }
}
