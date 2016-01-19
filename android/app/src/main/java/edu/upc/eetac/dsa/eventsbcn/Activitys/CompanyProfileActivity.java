package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventListAdapter;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClientException;
import edu.upc.eetac.dsa.eventsbcn.entity.Company;
import edu.upc.eetac.dsa.eventsbcn.entity.Event;
import edu.upc.eetac.dsa.eventsbcn.entity.EventCollection;

public class CompanyProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = CompanyProfileActivity.class.toString();
    private TextView name;
    private TextView description;
    private ImageView photo;
    private GoogleMap nMap;
    private float latitude;
    private float longitude;
    private GetCompanyTask mGetCompanyTask=null;
    private String uri;
    private String uri2;
    private Company company = new Company();
    private GetEventsTask mGetEventsTask = null;
    private LogoutTask mLogoutTask=null;
    private EventCollection events = new EventCollection();
    private EventListAdapter adapter = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        uri = (String) getIntent().getExtras().get("uri");
        name = (TextView) findViewById(R.id.companyprofile_name);
        description = (TextView) findViewById(R.id.companyprofile_description);
        photo = (ImageView) findViewById(R.id.company_photo);
        nMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.companyprofile_map)).getMap();

        mGetCompanyTask = new GetCompanyTask(uri);
        mGetCompanyTask.execute((Void) null);



        // set list adapter
        ListView list = (ListView)findViewById(R.id.list_profile);
        adapter = new EventListAdapter(this, events);
        list.setAdapter(adapter);
        list.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        /// set list OnItemClick listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CompanyProfileActivity.this, EventDetailActivity.class);
                String uri = EventsBCNClient.getLink(events.getEvents().get(position).getLinks(), "self").getUri().toString();
                intent.putExtra("uri", uri);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.company_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_eventsmain) {
            startActivity(new Intent(CompanyProfileActivity.this,EventsMainActivity.class));
        } else if (id == R.id.nav_userprofile) {
            String role = EventsBCNClient.getInstance().isUserInRole();
            if(role.equals("registered")){
                String uri_login = EventsBCNClient.getInstance().getUserUri();
                Intent intent = new Intent(CompanyProfileActivity.this,UserProfileActivity.class);
                intent.putExtra("uri",uri_login);
                startActivity(intent);
            }
            else {
                String uri_comp = EventsBCNClient.getInstance().getUriCompany();
                Intent intent = new Intent(CompanyProfileActivity.this,CompanyProfileActivity.class);
                intent.putExtra("uri",uri_comp);
                startActivity(intent);
            }

        } else if (id == R.id.nav_edituser) {

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompanyProfileActivity.this);
            builder.setTitle("Salir de EventsBCN").setMessage("Estás seguro?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mLogoutTask = new LogoutTask();
                    mLogoutTask.execute((Void) null);
                    finish();

                }
            }).setNegativeButton("No", null).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






   private void handleNewLocation() {

        //double currentLatitude =
        float currentLatitude = latitude;
        float currentLongitude = longitude;

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(company.getName());
        nMap.addMarker(options);
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }


    ///////////////////GET COMPANY BY ID/////////////////////////
    class GetCompanyTask extends AsyncTask<Void, Void, String> {

        private String uri;

        public GetCompanyTask(String uri) {
                this.uri = uri;
        }

        @Override
        protected String doInBackground(Void... params) {
            String jsoncompany = null;
            try{
                System.out.println("ID COMPAÑIA EN ASYNCTASK"+ uri);
                jsoncompany = EventsBCNClient.getInstance().getCompanyById(uri);
            }catch(EventsBCNClientException e){
                // TODO: Handle gracefully

            }
            return jsoncompany;
        }

        @Override
        protected void onPostExecute(String jsoncompany){

            company = (new Gson()).fromJson(jsoncompany, Company.class);
            Log.d(TAG,jsoncompany);
            uri2 = EventsBCNClient.getLink(company.getLinks(),"companyevents").getUri().toString();
            System.out.println("URIIIIIIII           " + uri2);
            latitude = company.getLatitude();
            longitude = company.getLongitude();
            //marco las coordenadas en el mapa
            handleNewLocation();
            name.setText(company.getName());
            description.setText(company.getDescription());
            //Execute AsyncTask
            mGetEventsTask = new GetEventsTask();
            mGetEventsTask.execute((Void) null);



        }

    }



    class GetEventsTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            String jsonEventCollection = null;
            try{
                //String uri = EventsBCNClient.getLink(company.getLinks(), "companyevents").getUri().toString();
                Log.d(TAG,uri);
                jsonEventCollection = EventsBCNClient.getInstance().getEvents(uri2);
            }catch(EventsBCNClientException e){
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsonEventCollection;
        }

        @Override
        protected void onPostExecute(String jsonEventCollection) {
            Log.d(TAG, jsonEventCollection);
            EventCollection eventCollection = (new Gson()).fromJson(jsonEventCollection, EventCollection.class);
            for(Event event : eventCollection.getEvents()){
                events.getEvents().add(events.getEvents().size(), event);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /*class DeleteEventTask extends AsyncTask<Void,Void,Boolean>{
        private String uri;
        public DeleteEventTask(String uri) {
            this.uri = uri;

        }
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result;
            result = EventsBCNClient.getInstance().deleteEvent(uri);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){

            if(result) {
                Toast.makeText(EventsMainActivity.this, "Evento eliminado con exito",
                        Toast.LENGTH_SHORT).show();
                events.getEvents().clear();
                adapter.notifyDataSetChanged();
                mGetEventsTask = new GetEventsTask();
                mGetEventsTask.execute((Void) null);


            }
            else Toast.makeText(EventsMainActivity.this, "Error, intentalo de nuevo",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public class LogoutTask extends AsyncTask<Void,Void,Boolean>{


        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = EventsBCNClient.getInstance().logout();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result) startActivity(new Intent(EventsMainActivity.this,LoginActivity.class));
            else Toast.makeText(EventsMainActivity.this, "Error, intentalo de nuevo",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    public class LogoutTask extends AsyncTask<Void,Void,Boolean>{


        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = EventsBCNClient.getInstance().logout();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                Intent intent = new Intent(CompanyProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else Toast.makeText(CompanyProfileActivity.this, "Error, intentalo de nuevo",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
