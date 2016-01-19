package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;

public class RegisterCompanyActivity extends AppCompatActivity {

    private RegisterCompanyTask mRegisterCompanyTask=null;
    private RegisterUserTask mRegisterUserTask=null;
    private GetCompanyByNameTask mGetCompanyByNameTask=null;
    private EditText mName;
    private EditText mDescription;
    private EditText mlocation;
    private Float latitude;
    private Float longitude;
    private String name_user;
    private String pass_user;
    private String mail_user;
    private String role;
    private File outputFile= null;
    public ArrayList<String> categorias;
    private View mProgressView;
    private View mFormView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (EditText) findViewById(R.id.Companyname);
        mDescription = (EditText) findViewById(R.id.Companydescription);
        mlocation = (EditText) findViewById(R.id.Companylocation);

        name_user = (String) getIntent().getExtras().get("nombre");
        pass_user = (String) getIntent().getExtras().get("pass");
        mail_user = (String) getIntent().getExtras().get("mail");
        role = (String) getIntent().getExtras().get("role");
        Bundle bundle = getIntent().getExtras();
        outputFile = (File) bundle.get("foto");
        if(outputFile==null) System.out.println("SHIEEEEEEEEEETTTTTTTTT");
        categorias = getIntent().getExtras().getStringArrayList("categorias");


        final Button registrarCompany = (Button) findViewById(R.id.btnRegisterCompany);
        registrarCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LA LOCALIZACION ES  " + mlocation.getText().toString());
                boolean result = getCoordinatesFromLocation(mlocation.getText().toString().concat(",Barcelona"));
                if(result) {
                    if(mName.getText().toString().equals("")||mDescription.getText().toString().equals("")||mlocation.getText().toString().equals("")){
                        Toast error = Toast.makeText(getApplicationContext(),"Faltan campos por rellenar", Toast.LENGTH_SHORT);
                        error.show();
                    }
                    else {
                        mGetCompanyByNameTask = new GetCompanyByNameTask(mName.getText().toString());
                        mGetCompanyByNameTask.execute((Void) null);
                        showProgress(true);
                    }
                }
                else{
                    Toast error = Toast.makeText(getApplicationContext(),"La direccion no existe, intentelo de nuevo", Toast.LENGTH_SHORT);
                    error.show();
                }

            }
        });

        mFormView = findViewById(R.id.form_company);
        mProgressView = findViewById(R.id.company_progress);





    }

    /**
     * Shows the progress UI and hides the RegisterCompany form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void registraCompany(){

        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        String location = mlocation.getText().toString().concat(",Barcelona");



        mRegisterCompanyTask = new RegisterCompanyTask(name, description, location, latitude,longitude);
        mRegisterCompanyTask.execute((Void) null);

    }

    public boolean getCoordinatesFromLocation(String location){

        System.out.println("METODO DE GEOLOCALIZACION");
        boolean result=false;

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);


        try {

            System.out.println("LA LOCALIZACION ES  "+ location);
            List<Address> geoResults = geocoder.getFromLocationName(location, 1);
            if (geoResults.size()==0) {
                result = false;
                System.out.println("NO LA ENCUENTRO");


            }
            if (geoResults.size()>0) {
                System.out.println("LA TENGO");

                Address addr = geoResults.get(0);
                latitude = (float) addr.getLatitude();
                longitude = (float) addr.getLongitude();
                System.out.println("LATITUD  "+latitude+"  LONGITUD  "+longitude);
                result = true;


            }
        } catch (Exception e) {
            System.out.println("PETO");

            System.out.print(e.getMessage());
        }
        return result;
    }

    public class RegisterCompanyTask extends AsyncTask<Void,Void,Integer>{
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
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(RegisterCompanyActivity.this, "Cargando...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            EventsBCNClient client = EventsBCNClient.getInstance();
            int result;
            result = client.registerCompany(name,description,location,latitude,longitude);
            return result;
        }

        @Override
        protected void onPostExecute(Integer success) {

            //showProgress(false);
            System.out.println("SUCCESSSSSSSSSSSS      "+ success);
            if (success==1) {
                showProgress(false);
                startActivity(new Intent(RegisterCompanyActivity.this, EventsMainActivity.class));
                finish();
            }
            else if(success==-1){
                showProgress(false);
                Toast.makeText(RegisterCompanyActivity.this, "El nombre de esta empresa ya existe",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                showProgress(false);
                Toast.makeText(RegisterCompanyActivity.this, "Ha ocurrido un error,intentelo de nuevo",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    public class GetCompanyByNameTask extends AsyncTask<Void,Void,Boolean>{

        private String name;
        public GetCompanyByNameTask(String name) {
            this.name = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result;
            result = EventsBCNClient.getInstance().getCompanyByName(name);
            return result;
        }

        @Override
        protected void onPreExecute(){
            Toast.makeText(RegisterCompanyActivity.this, "Cargando...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean result){

            if(result){
                showProgress(false);
                Toast.makeText(RegisterCompanyActivity.this, "El nombre de esta empresa ya existe",
                        Toast.LENGTH_SHORT).show();

            }
            else{
                mRegisterUserTask = new RegisterUserTask(outputFile, categorias, name_user, pass_user, mail_user, role);
                mRegisterUserTask.execute((Void) null);


            }
        }
    }


    public class RegisterUserTask extends AsyncTask<Void, Void, Integer> {
        File photo;
        public ArrayList<String> categorias;
        private String name;
        private String pass;
        private String mail;
        private String role;

        public RegisterUserTask(File photo, ArrayList<String> categorias, String name, String pass, String mail,String role) {
            this.photo = photo;
            this.categorias = categorias;
            this.name = name;
            this.pass = pass;
            this.mail = mail;
            this.role = role;
        }

        @Override
        protected Integer doInBackground(Void... params) {



            int result = EventsBCNClient.getInstance().registerUserImage(name,pass,mail,categorias,role,photo);


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(RegisterCompanyActivity.this, "Cargando...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result==1) {
                Toast.makeText(RegisterCompanyActivity.this, "Foto subida con éxito!",
                        Toast.LENGTH_SHORT).show();
                    registraCompany();
            }
            else if(result==-1){
                showProgress(false);
                Toast.makeText(RegisterCompanyActivity.this, "Este nombre de usuario ya existe",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterCompanyActivity.this, ChooseRoleACtivity.class));
                finish();
            }
            else {
                showProgress(false);
                Toast.makeText(RegisterCompanyActivity.this, "Este mail de usuario ya existe",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterCompanyActivity.this, ChooseRoleACtivity.class));
                finish();
            }


        }
    }

}
