package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventListAdapter;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClientException;
import edu.upc.eetac.dsa.eventsbcn.entity.Event;
import edu.upc.eetac.dsa.eventsbcn.entity.EventCollection;
import edu.upc.eetac.dsa.eventsbcn.entity.User;

public class UserProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String uri;
    private TextView name;
    private TextView mail;
    private TextView categorias;
    private ImageView photouser;
    private User user = new User();
    private GetUserTask mGetUserTask=null;
    private LogoutTask mLogoutTask=null;
    private GetEventsByAssistTask mGetEventByAssistTask =null;
    private EventCollection events = new EventCollection();
    private EventListAdapter adapter = null;
    private String uri_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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

        name = (TextView) findViewById(R.id.userprofile_name);
        mail = (TextView) findViewById(R.id.userprofile_mail);
        categorias = (TextView) findViewById(R.id.userprofile_categories);

        photouser = (ImageView) findViewById(R.id.user_photo);

        mGetUserTask = new GetUserTask(uri);
        mGetUserTask.execute((Void) null);


        // set list adapter
        ListView list = (ListView)findViewById(R.id.list_user_profile);
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
                Intent intent = new Intent(UserProfileActivity.this, EventDetailActivity.class);
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
        getMenuInflater().inflate(R.menu.user_profile, menu);
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
            startActivity(new Intent(UserProfileActivity.this,EventsMainActivity.class));

        } else if (id == R.id.nav_userprofile) {
            String role = EventsBCNClient.getInstance().isUserInRole();
            if(role.equals("registered")){
                String uri_login = EventsBCNClient.getInstance().getUserUri();
                Intent intent = new Intent(UserProfileActivity.this,UserProfileActivity.class);
                intent.putExtra("uri",uri_login);
                startActivity(intent);
            }
            else {
                String uri_comp = EventsBCNClient.getInstance().getUriCompany();
                Intent intent = new Intent(UserProfileActivity.this,CompanyProfileActivity.class);
                intent.putExtra("uri",uri_comp);
                startActivity(intent);
            }

        } else if (id == R.id.nav_edituser) {

        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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


    public class GetUserTask extends AsyncTask<Void,Void,String>{
        private String uri=null;

        public GetUserTask(String uri) {
            this.uri = uri;
        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonUser=null;

            try {
                jsonUser =EventsBCNClient.getInstance().getUser(uri);
            } catch (EventsBCNClientException e) {
                e.printStackTrace();
            }

            return jsonUser;
        }

        @Override
        protected void onPostExecute(String jsonUser){
            user = (new Gson()).fromJson(jsonUser, User.class);
            List<String> categories= user.getCategories();
            name.setText(user.getName());
            mail.setText(user.getEmail());
            for(String categoria : categories) {
                categorias.setText("#"+categoria+"  ");
            }
            uri_user = EventsBCNClient.getLink(user.getLinks(),"event-assist").getUri().toString();
            mGetEventByAssistTask = new GetEventsByAssistTask();
            mGetEventByAssistTask.execute((Void) null);


        }
    }

    public class GetEventsByAssistTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... params) {

            String jsonEventCollection = null;

            //String uri = EventsBCNClient.getLink(company.getLinks(), "companyevents").getUri().toString();

            jsonEventCollection = EventsBCNClient.getInstance().getEventsByAssist(uri_user);

            return jsonEventCollection;
        }

        protected void onPostExecute(String jsonEventCollection){
            EventCollection eventCollection = (new Gson()).fromJson(jsonEventCollection, EventCollection.class);
            for(Event event : eventCollection.getEvents()){
                events.getEvents().add(events.getEvents().size(), event);
            }
            adapter.notifyDataSetChanged();
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
            if(result){
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else Toast.makeText(UserProfileActivity.this, "Error, intentalo de nuevo",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
