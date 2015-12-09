package edu.upc.eetac.dsa.eventsBCN.dao;

/**
 * Created by juan on 30/11/15.
 */
public interface EventDAOQuery {

    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_EVENT = "insert into events (id,title,description,date,photo,category,companyid) values(unhex(?),?,?,?,?,?,unhex(?))";
    public final static String GET_EVENTS = "select hex(id) as id, title,description,date,photo,category,hex(companyid) as companyid, last_modified, creation_timestamp from events ";
    public final static String GET_EVENTS_BY_ID = "select hex(e.id) as id, e.title,e.description,e.date,e.photo,e.category,hex(e.companyid) as companyid, e.last_modified, e.creation_timestamp from events e where id = unhex(?)";
    public final static String GET_EVENTS_BY_CATEGORY = "select hex(e.id) as id, e.title,e.description,e.date,e.photo,e.category,hex(e.companyid) as companyid, e.last_modified, e.creation_timestamp from events e where category = ?";
    public final static String GET_EVENTS_BY_COMPANY = "select hex(e.id) as id, e.title,e.description,e.date,e.photo,e.category,hex(e.companyid) as companyid, e.last_modified, e.creation_timestamp from events e where companyid = unhex(?)";
    public final static String DELETE_EVENT = "delete from events where id=unhex(?)";
    public final static String UPDATE_EVENT = "update events set title=?, description=?, date=?, photo=?, category=? where id=unhex(?)";
}
