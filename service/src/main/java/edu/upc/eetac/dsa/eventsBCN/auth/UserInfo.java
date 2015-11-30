package edu.upc.eetac.dsa.eventsBCN.auth;

import edu.upc.eetac.dsa.eventsBCN.entity.Role;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aitor on 24/10/15.
 */
public class UserInfo implements Principal { //Esto no es un POJO ni usa HTTP

    private String name;// sera el identificador de usuario "userid"
    private List<Role> roles = new ArrayList<>();

    public UserInfo() {
    }

    public UserInfo(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}