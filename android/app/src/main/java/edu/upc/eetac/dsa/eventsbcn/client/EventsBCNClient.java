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

    //private final static String BASE_URI = "http://192.168.1.134:8080/eventsBCN";
    private final static String BASE_URI = "http://147.83.7.207:8080/eventsBCN";
    private static EventsBCNClient instance;
    private Root root;
    private ClientConfig clientConfig = null;
    private Client client = null;
    private AuthToken authToken = null;
    private Company comp = null;
    private Event evt =null;
    private User user= null;
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

    public int registerUserImage(String name,String pass,String mail,List<String> categories,String role,File bin){

        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();

        formDataMultiPart.field("name", name);
        formDataMultiPart.field("password", pass);
        formDataMultiPart.field("email", mail);
        for(String category : categories) {
            System.out.println("CATEGORIAS       "+category);
            formDataMultiPart.field("categories", category);
        }
        formDataMultiPart.bodyPart(new FileDataBodyPart("image", bin, MediaType.APPLICATION_OCTET_STREAM_TYPE));




        String registerUserUri = getLink(root.getLinks(),"create-user").getUri().toString()+"?role="+role;
        System.out.println(registerUserUri);
        WebTarget target = client.target(registerUserUri);
        Response response = target.request().post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE),Response.class);
        System.out.println(response.getStatus());
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()){
            String json = response.readEntity(String.class);
            authToken = (new Gson()).fromJson(json, AuthToken.class);
            System.out.println(authToken.getRole());
            Log.d(TAG,authToken.toString());
            return 1;
        }
        else if(response.getStatus()== Response.Status.CONFLICT.getStatusCode()) return -1;
        else return 0;

    }



    public int registerCompany(String name, String description, String location, Float latitude, Float longitud ){
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        company.setLocation(location);
        company.setLatitude(latitude);
        company.setLongitude(longitud);
        System.out.println(authToken.getUserid());
        company.setUserid(authToken.getUserid());

        //String uri = BASE_URI+"/companies";
        String uri = getLink(root.getLinks(),"create-company").getUri().toString();
        //String registerCompanyUri = getLink(root.getLinks(),"create-company").getUri().toString();
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).post(Entity.entity((new Gson().toJson(company)), EventsBCNMediaType.EVENTSBCN_COMPANY), Response.class);
        System.out.println("REGISTRACOMPAÑIA             "+response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            String jsonCompany = response.readEntity(String.class);
            comp = (new Gson()).fromJson(jsonCompany, Company.class);
            System.out.println(comp.getName());
            Log.d(TAG,comp.toString());
            return 1;
        }
        else if(response.getStatus()== Response.Status.CONFLICT.getStatusCode()) return -1;
        else return 0;
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

    public boolean logout(){
        String uri = EventsBCNClient.getLink(authToken.getLinks(), "logout").getUri().toString();
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).delete();
        System.out.println(response.getStatus());
        return true;
    }

    public boolean getCompanyByName(String name){
        String uri = getLink(root.getLinks(),"create-company").getUri().toString()+"/search?name="+name;
        Log.d(TAG,uri);
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        System.out.println(response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return true;
        }
        else return false;

    }

    public String getUserByLogin(){
        String uri = getLink(authToken.getLinks(),"user-profile").getUri().toString();
        Log.d(TAG,uri);
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        System.out.println(response.getStatus());
        if(response.getStatus() == Response.Status.OK.getStatusCode()){
            String json = response.readEntity(String.class);
            user = (new Gson()).fromJson(json, User.class);
            String uri2 = getLink(user.getLinks(),"self").getUri().toString();
            return uri2;

        }
        return null;
    }

    public String getEventsByAssist(String uri){
        //String uri = getLink(user.getLinks(),"event-assist").getUri().toString();
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println(response.getStatus());
            return response.readEntity(String.class);
        }else
            return null;
    }

    public String getEvents(String uri) throws EventsBCNClientException {

        System.out.println("URI VALEEEEEEEEEE    " + uri);
        String role = authToken.getRole();
        if(uri==null) {
            if (role.equals("registered")) {
                uri = getLink(authToken.getLinks(), "events").getUri().toString();
                Log.d(TAG, uri);
            } else uri = getLink(comp.getLinks(), "companyevents").getUri().toString();
        }

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

    public String getUser(String uri) throws EventsBCNClientException {
        System.out.println(uri);
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {

            return response.readEntity(String.class);
        }else
            throw  new EventsBCNClientException((response.readEntity(String.class)));

    }

    public String getCompanyById(String uri) throws EventsBCNClientException{
        System.out.println("ENTRO POR EL EVENTS DETAIL");
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).get();
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {

            return response.readEntity(String.class);

        }else throw  new EventsBCNClientException(response.readEntity(String.class));

    }

    public String getCompanyByUserid() throws EventsBCNClientException{
        //String uri =BASE_URI +"/companies?userid="+authToken.getUserid();
        String uri = getLink(root.getLinks(),"create-company").getUri().toString()+"?userid="+authToken.getUserid();
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

    public boolean createEvent(String title,String description,String category,String date,String companyid,File image){

        System.out.println(title);
        System.out.println(description);
        System.out.println(category);
        System.out.println(date);
        System.out.println(" LA COMPAÑIA DEL METODO!     " + companyid);

        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();

        formDataMultiPart.field("title", title);
        formDataMultiPart.field("description", description);
        formDataMultiPart.field("category", category);
        formDataMultiPart.field("date", date);
        formDataMultiPart.field("companyid", companyid);
        formDataMultiPart.bodyPart(new FileDataBodyPart("image", image, MediaType.APPLICATION_OCTET_STREAM_TYPE));


        System.out.println(comp.getName());
        String uri = getLink(comp.getLinks(),"crear-evento").getUri().toString();
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE), Response.class);
        System.out.println(response.getStatus());
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {

            return true;
        }
        return false;




    }

    public boolean assistEvent(String uri){
        //System.out.println("ID COMPAÑIA---------"+companyid+"    ID EVENTO-------"+eventid);
        //WebTarget target = client.target(BASE_URI + "/companies/"+companyid+"/events/"+eventid+"/assist");
        Log.d(TAG,uri);
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token",authToken.getToken()).post(null, Response.class);
        System.out.println(response.getStatus());
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {

            return true;
        }else return false;

    }

    public boolean wontAssistEvent(String uri){
        Log.d(TAG,uri);
        //WebTarget target = client.target(BASE_URI + "/companies/"+companyid+"/events/"+eventid+"/wontassist");
        WebTarget target = client.target(uri);
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

    public String getUriCompany(){

        return getLink(comp.getLinks(),"self").getUri().toString();
    }

    public String getUserUri(){
        return getLink(user.getLinks(),"self").getUri().toString();
    }






}
