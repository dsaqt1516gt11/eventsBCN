package edu.upc.eetac.dsa.eventsbcn.client;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.eventsbcn.entity.AuthToken;
import edu.upc.eetac.dsa.eventsbcn.entity.Company;
import edu.upc.eetac.dsa.eventsbcn.entity.Event;
import edu.upc.eetac.dsa.eventsbcn.entity.Link;
import edu.upc.eetac.dsa.eventsbcn.entity.Root;
import edu.upc.eetac.dsa.eventsbcn.entity.User;

/**
 * Created by juan on 6/12/15.
 */
public class EventsBCNClient {

    private final static String BASE_URI = "http://192.168.1.132:8080/eventsBCN";
    //private final static String BASE_URI = "http://147.83.7.207:8080/eventsBCN";
    private static EventsBCNClient instance;
    private Root root;
    private ClientConfig clientConfig = null;
    private Client client = null;
    private AuthToken authToken = null;
    private Company comp = null;
    private Event evt =null;
    private final static String TAG = EventsBCNClient.class.toString();
    private Context context;



    private EventsBCNClient() {
        clientConfig = new ClientConfig();
        clientConfig.register(MultiPartFeature.class);
        client = ClientBuilder.newClient(clientConfig);

        loadRoot();
    }

    public static EventsBCNClient getInstance() {
        if (instance == null)
            instance = new EventsBCNClient();
        return instance;
    }

    private void loadRoot() {
        WebTarget target = client.target(BASE_URI);
        Response response = target.request().get();

        String json = response.readEntity(String.class);
        root = (new Gson()).fromJson(json, Root.class);
    }

    public Root getRoot() {
        return root;
    }

    public final static Link getLink(List<Link> links, String rel){
        for(Link link : links){
            if(link.getRels().contains(rel)) {
                return link;
            }
        }
        return null;
    }

    public boolean registerUserImage(File bin){


        String name ="foto8";
        String pass ="123";
        String mail="aaaaa2486846a@a";
        String category= "cine";
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();

        formDataMultiPart.field("name", name);
        formDataMultiPart.field("password",pass);
        formDataMultiPart.field("email", mail);
        formDataMultiPart.field("categories",category);
        formDataMultiPart.bodyPart(new FileDataBodyPart("image", bin, MediaType.APPLICATION_OCTET_STREAM_TYPE));




        String registerUserUri = getLink(root.getLinks(),"create-user").getUri().toString()+"?role=registered";
        System.out.println(registerUserUri);
        WebTarget target = client.target(registerUserUri);
        Response response = target.request().post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE),Response.class);
        System.out.println(response.getStatus());
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()){
            String json = response.readEntity(String.class);
            authToken = (new Gson()).fromJson(json, AuthToken.class);
            System.out.println(authToken.getRole());
            Log.d(TAG,authToken.toString());
            return true;
        }
        else return false;

    }

    public boolean registerUser(String name,String pass,String mail,String photo,List<String> categories,String role) throws JSONException {
        //String [] categories = {"cine","teatro"};
        User user = new User();
        user.setName(name);
        user.setPassword(pass);
        user.setEmail(mail);
        user.setPhoto(photo);
        user.setCategories(categories);

        String registerUserUri = getLink(root.getLinks(),"create-user").getUri().toString()+"?role="+role;
        System.out.println(registerUserUri);
        WebTarget target = client.target(registerUserUri);
        System.out.println(user.getPassword());
        System.out.println(user.getCategories());
        String json = target.request().post(Entity.entity((new Gson().toJson(user)), EventsBCNMediaType.EVENTSBCN_USER),String.class);
        authToken = (new Gson()).fromJson(json, AuthToken.class);
        Log.d(TAG, json);
        return true;

    }

    public boolean registerCompany(String name, String description, String location, Float latitude, Float longitud ){
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        company.setLocation(location);
        company.setLatitude(latitude);
        company.setLongitude(longitud);
        company.setUserid(authToken.getUserid());

        String uri = BASE_URI+"/companies";
        //String registerCompanyUri = getLink(root.getLinks(),"create-company").getUri().toString();
        WebTarget target = client.target(uri);
        String jsonCompany = target.request().header("X-Auth-Token", authToken.getToken()).post(Entity.entity((new Gson().toJson(company)), EventsBCNMediaType.EVENTSBCN_COMPANY), String.class);
        comp = (new Gson()).fromJson(jsonCompany,Company.class);
        return true;
    }

    public boolean login(String username, String password) throws EventsBCNClientException {
        String loginUri = getLink(root.getLinks(), "login").getUri().toString();
        System.out.println(loginUri);
        WebTarget target = client.target(loginUri);
        Form form = new Form();
        form.param("login", username);
        form.param("password", password);
        System.out.println(username+"   "+password);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
        //String json = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
        System.out.println(response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println(response.getStatus());
            String json = response.readEntity(String.class);
            authToken = (new Gson()).fromJson(json, AuthToken.class);
            Log.d(TAG, json);
            if(authToken.getRole().equals("company")){
                String jsonCompany= getCompanyByUserid();
                comp = (new Gson()).fromJson(jsonCompany, Company.class);
            }
            return true;
        }else {

            return false;
        }
    }

    //public String getEventsByCompany()



    public String getEvents() throws EventsBCNClientException {

        String uri;
        String role = authToken.getRole();
        if(role.equals("registered")){
            uri = getLink(authToken.getLinks(), "events").getUri().toString();
            Log.d(TAG,uri);
        }
        else uri = getLink(comp.getLinks(), "companyevents").getUri().toString();

        WebTarget target = client.target(uri);
        System.out.println(authToken.getToken());
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println(response.getStatus());
            return response.readEntity(String.class);
        }else
            throw new EventsBCNClientException(response.readEntity(String.class));
    }

    public String getEvent(String uri) throws EventsBCNClientException {
        System.out.println(uri);
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {

            return response.readEntity(String.class);
        }else
            throw  new EventsBCNClientException((response.readEntity(String.class)));


    }

    public String getCompanyById(String id) throws EventsBCNClientException{
        WebTarget target = client.target(BASE_URI + "/companies/" + id);
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).get();
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {

            return response.readEntity(String.class);

        }else throw  new EventsBCNClientException(response.readEntity(String.class));

    }

    public String getCompanyByUserid() throws EventsBCNClientException{
        String uri =BASE_URI +"/companies?userid="+authToken.getUserid();
        System.out.println(uri);
        String companyid = null;
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).header("Content-Type",EventsBCNMediaType.EVENTSBCN_COMPANY).get();
        String jsonCompany= response.readEntity(String.class);
        comp = (new Gson()).fromJson(jsonCompany,Company.class);
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {


            return jsonCompany;


        }else throw  new EventsBCNClientException(response.readEntity(String.class));

    }

    public boolean createEvent(String title,String description,String photo,String category,String date,String companyid){
        Event event = new Event();
        System.out.println(title);
        System.out.println(description);
        System.out.println(photo);
        System.out.println(category);
        System.out.println(date);
        System.out.println(" LA COMPAÑIA DEL METODO!     "+companyid);

        event.setTitle(title);
        event.setDescription(description);
        event.setPhoto(photo);
        event.setCategory(category);
        event.setDate(date);
        event.setCompanyid(companyid);

        WebTarget target = client.target(BASE_URI+"/companies/"+companyid+"/events");
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).post(Entity.entity((new Gson().toJson(event)), EventsBCNMediaType.EVENTSBCN_EVENT), Response.class);
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
        //evt= new Gson().fromJson(jsonEvent,Event.class);
        return true;

    }

    public boolean assistEvent(String companyid, String eventid){
        System.out.println("ID COMPAÑIA---------"+companyid+"    ID EVENTO-------"+eventid);
        WebTarget target = client.target(BASE_URI + "/companies/"+companyid+"/events/"+eventid+"/assist");
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).post(null, Response.class);
        System.out.println(response.getStatus());
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {

            return true;
        }else return false;

    }

    public boolean wontAssistEvent(String companyid, String eventid){
        WebTarget target = client.target(BASE_URI + "/companies/"+companyid+"/events/"+eventid+"/wontassist");
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).delete();
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {

            return true;
        }else return false;

    }

    public boolean deleteEvent(String uri){
        WebTarget target = client.target(uri);
        Log.d(TAG,uri);
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).delete();
        System.out.println(response.getStatus());
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            return true;
        }else return false;

    }

    public String isUserInRole(){

        return authToken.getRole();

    }






}
