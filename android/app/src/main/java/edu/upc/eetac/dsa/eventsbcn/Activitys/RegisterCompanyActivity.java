package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.util.List;
import java.util.Locale;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;

public class RegisterCompanyActivity extends AppCompatActivity {

    private RegisterCompanyTask mRegisterCompanyTask=null;
    private EditText mName;
    private EditText mDescription;
    private EditText mlocation;
    private Float latitude;
    private Float longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (EditText) findViewById(R.id.Companyname);
        mDescription = (EditText) findViewById(R.id.Companydescription);
        mlocation = (EditText) findViewById(R.id.Companylocation);


        final Button registrarCompany = (Button) findViewById(R.id.btnRegisterCompany);
        registrarCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LA LOCALIZACION ES  "+mlocation.getText().toString());
                getCoordinatesFromLocation(mlocation.getText().toString());
                registraCompany();
            }
        });







    }

    public void registraCompany(){

        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        String location = mlocation.getText().toString();


        mRegisterCompanyTask = new RegisterCompanyTask(name, description, location, latitude,longitude);
        mRegisterCompanyTask.execute((Void) null);

    }

    public void getCoordinatesFromLocation(String location){

        System.out.println("METODO DE GEOLOCALIZACION");

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);


        try {
            List<Address> geoResults = geocoder.getFromLocationName(location, 1);
            while (geoResults.size()==0) {
                System.out.println("NO LA ENCUENTRO");

                geoResults = geocoder.getFromLocationName(location, 1);
            }
            if (geoResults.size()>0) {
                System.out.println("LA TENGO");

                Address addr = geoResults.get(0);
                latitude = (float) addr.getLatitude();
                longitude = (float) addr.getLongitude();
                System.out.println("LATITUD  "+latitude+"  LONGITUD  "+longitude);


            }
        } catch (Exception e) {
            System.out.println("PETO");

            System.out.print(e.getMessage());
        }
    }

    public class RegisterCompanyTask extends AsyncTask<Void,Void,Boolean>{
        String name;
        String description;
        String location;
        Float latitude;
        Float longitude;


        public RegisterCompanyTask(String name, String description, String location, Float latitude, Float longitude) {
            this.name = name;
            this.description = description;
            this.location = location;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            EventsBCNClient client = EventsBCNClient.getInstance();
            boolean result = false;
            result = client.registerCompany(name,description,location,latitude,longitude);
            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterCompanyTask = null;
            //showProgress(false);

            if (success) {
                startActivity(new Intent(RegisterCompanyActivity.this, EventsMainActivity.class));
            }
            else {
                System.out.println("error");
            }
        }


    }

}
