package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClientException;
import edu.upc.eetac.dsa.eventsbcn.entity.Company;
import edu.upc.eetac.dsa.eventsbcn.entity.Event;
import edu.upc.eetac.dsa.eventsbcn.entity.User;

public class EventDetailActivity extends AppCompatActivity  {

    private final static String TAG = EventDetailActivity.class.toString();
    private GetEventTask mGetEventTask = null;
    private GetCompanyTask mGetCompanyTask = null;
    private AssistTask mAssistTask = null;
    private WontAssistTask mWontAssistTask = null;
    private Event event = new Event();
    private Company company = new Company();
    private TextView titulo;
    private TextView date;
    private TextView description;
    private TextView category;
    private TextView asistants;
    private TextView company_name;
    private TextView company_descripction;
    private ImageView imagen_evento;
    private String uri;
    private GoogleMap nMap;
    private Float latitude;
    private Float longitude;
    private String companyid;
    private Button asistencia;
    private List<User> asistentes;
    private String role;
    private Context context;





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        context=this;
        role = EventsBCNClient.getInstance().isUserInRole();

        uri = (String) getIntent().getExtras().get("uri");

        titulo = (TextView) findViewById(R.id.title);

        date = (TextView) findViewById(R.id.date);

        description = (TextView) findViewById(R.id.Description);

        category = (TextView) findViewById(R.id.Category);

        asistants = (TextView) findViewById(R.id.Asistentes);

        nMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        company_name = (TextView) findViewById(R.id.Company_name);

        company_descripction = (TextView) findViewById(R.id.Company_descripcion);

        imagen_evento = (ImageView) findViewById(R.id.photo_detail);





        // Execute AsyncTask
        mGetEventTask = new GetEventTask(null);
        mGetEventTask.execute((Void) null);



        asistencia = (Button) findViewById(R.id.assist_button);
        if(role.equals("company")) asistencia.setVisibility(View.GONE);
        asistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asistencia.getText().equals("ASISTIR")){
                    mAssistTask = new AssistTask();
                    mAssistTask.execute((Void) null);
                }
                else if(asistencia.getText().equals("DEJAR DE ASISTIR")){
                    mWontAssistTask = new WontAssistTask();
                    mWontAssistTask.execute((Void) null);
                }
            }
        });

        asistants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailActivity.this,UserListActivity.class);
                intent.putExtra("asistentes", (Serializable) asistentes);
                startActivity(intent);
            }
        });

        company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = EventsBCNClient.getLink(event.getLinks(),"company-profile").getUri().toString();
                Intent intent = new Intent(EventDetailActivity.this,CompanyProfileActivity.class);
                intent.putExtra("uri",uri);
                startActivity(intent);
            }
        });








    }
    private void handleNewLocation() {

        //double currentLatitude =
        float currentLatitude = latitude;
        float currentLongitude = longitude;

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(event.getTitle());
        nMap.addMarker(options);
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }




    ///////////////////GET COMPANY BY ID/////////////////////////
    class GetCompanyTask extends AsyncTask<Void, Void, String> {

        public GetCompanyTask(Object o) {

        }

        @Override
        protected String doInBackground(Void... params) {
            String jsoncompany = null;
            try{
                String uri = EventsBCNClient.getLink(event.getLinks(),"company-profile").getUri().toString();
                System.out.println("ID COMPAÑIA EN ASYNCTASK"+ companyid);
                jsoncompany = EventsBCNClient.getInstance().getCompanyById(uri);
            }catch(EventsBCNClientException e){
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsoncompany;
        }
        
        @Override
        protected void onPostExecute(String jsoncompany){
            Log.d(TAG, jsoncompany);
            company = (new Gson()).fromJson(jsoncompany, Company.class);
            latitude = company.getLatitude();
            longitude = company.getLongitude();
            //marco las coordenadas en el mapa
            handleNewLocation();
            company_name.setText(company.getName());
            company_descripction.setText(company.getDescription());

            
        }
        
    }
    /////////////////////////////////////////////////////////////

    ///////////////////GET EVENT////////////////////////////////


    class GetEventTask extends AsyncTask<Void, Void, String> {




        public GetEventTask(String uri) {


        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonEvent = null;
            try{
                jsonEvent = EventsBCNClient.getInstance().getEvent(uri);
            }catch(EventsBCNClientException e){
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsonEvent;

        }

        @Override
        protected void onPostExecute(String jsonEvent) {

            Log.d(TAG, jsonEvent);
            event = (new Gson()).fromJson(jsonEvent, Event.class);
            titulo.setText(event.getTitle());
            date.setText(event.getDate());
            description.setText(event.getDescription());
            category.setText(event.getCategory());
            companyid = event.getCompanyid();
            asistentes = event.getUsers();
            asistants.setText(asistentes.size()+" Asistentes");
            Picasso.with(context).load(event.getPhotoURL()).into(imagen_evento);
            System.out.println("ID COMPAÑIA" + companyid);
            System.out.println("ASISTO?_______" + event.isAssisted());
            if(event.isAssisted()==true) asistencia.setText("DEJAR DE ASISTIR");
            else asistencia.setText("ASISTIR");


            // PILLO COMPAÑIA
            mGetCompanyTask = new GetCompanyTask(null);
            mGetCompanyTask.execute((Void) null);



        }
    }
    ////////////////////////////////////////////////////////////////////////////////

    /////////////////////////ASISTIR A UN EVENTO////////////////////////////////////

    class AssistTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success;
            String uri = EventsBCNClient.getLink(event.getLinks(),"assist").getUri().toString();
            success = EventsBCNClient.getInstance().assistEvent(uri);
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                asistencia.setText("DEJAR DE ASISTIR");
                // Execute AsyncTask
                mGetEventTask = new GetEventTask(null);
                mGetEventTask.execute((Void) null);
            }
            else {
                Toast error = Toast.makeText(getApplicationContext(),"Error, intentelo mas tarde", Toast.LENGTH_SHORT);
                error.show();
            }

        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////DEJAR DE ASISTIR A UN EVENTO////////////////////////////////////////////////////

    class WontAssistTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success;
            String uri = EventsBCNClient.getLink(event.getLinks(),"wontassist").getUri().toString();
            success = EventsBCNClient.getInstance().wontAssistEvent(uri);
            System.out.println("ID COMPAÑIA---------"+companyid+"    ID EVENTO-------"+event.getId());
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success) {
                asistencia.setText("ASISTIR");
                // Execute AsyncTask
                mGetEventTask = new GetEventTask(null);
                mGetEventTask.execute((Void) null);
            }
            else {
                Toast error = Toast.makeText(getApplicationContext(),"Error, intentelo mas tarde", Toast.LENGTH_SHORT);
                error.show();
            }

        }
    }
}
