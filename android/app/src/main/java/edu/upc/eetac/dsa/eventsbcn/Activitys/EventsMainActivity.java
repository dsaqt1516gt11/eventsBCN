package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;



import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventListAdapter;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClientException;
import edu.upc.eetac.dsa.eventsbcn.entity.Event;
import edu.upc.eetac.dsa.eventsbcn.entity.EventCollection;

public class EventsMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = EventsMainActivity.class.toString();
    private GetEventsTask mGetEventsTask = null;
    private DeleteEventTask mDeleteEventTask = null;
    private LogoutTask mLogoutTask = null;
    private GetUserTask mGetUserTask = null;
    private EventCollection events = new EventCollection();
    private EventListAdapter adapter = null;
    private String uri_delete=null;
    private String uri_login=null;
    private String role=null;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        role = EventsBCNClient.getInstance().isUserInRole();
        System.out.println("ROL      "+role);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // BOTON DE CREAR EVENTO
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsMainActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });
        ////////////////

        // Execute AsyncTask
        mGetEventsTask = new GetEventsTask();
        mGetEventsTask.execute((Void) null);

        mGetUserTask = new GetUserTask();
        mGetUserTask.execute((Void) null);

        // set list adapter
        final ListView list = (ListView)findViewById(R.id.list_events);
        adapter = new EventListAdapter(this, events);
        list.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        events.getEvents().clear();
                        adapter.notifyDataSetChanged();
                        mGetEventsTask = new GetEventsTask();
                        mGetEventsTask.execute((Void) null);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },2500);

            }
        });

        /// set list OnItemClick listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventsMainActivity.this, EventDetailActivity.class);
                String uri = EventsBCNClient.getLink(events.getEvents().get(position).getLinks(), "self").getUri().toString();
                intent.putExtra("uri", uri);
                startActivity(intent);
            }
        });

        // MUESTRA WIDGETS EN FUNCION DEL ROL
        if(role.equals("company")) {
            getSupportActionBar().setTitle("Tus Eventos Creados");
            // PARA BORRAR EVENTOS ( SOLO COMPAÑIA)
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    uri_delete = EventsBCNClient.getLink(events.getEvents().get(position).getLinks(), "delete").getUri().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventsMainActivity.this);
                    builder.setTitle("Eliminarás este evento").setMessage("Estás seguro?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mDeleteEventTask = new DeleteEventTask(uri_delete);
                            mDeleteEventTask.execute((Void) null);


                        }
                    }).setNegativeButton("No", null).show();

                    return true;
                }
            });

        }else {
            getSupportActionBar().setTitle("Proximos Eventos");
            fab.hide();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(EventsMainActivity.this);
            builder.setTitle("Salir de EventsBCN").setMessage("Estás seguro?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mLogoutTask = new LogoutTask();
                    mLogoutTask.execute((Void) null);
                    finish();

                }
            }).setNegativeButton("No", null).show();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events_main, menu);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_userprofile) {
            if(role.equals("registered")){
                Intent intent = new Intent(EventsMainActivity.this,UserProfileActivity.class);
                intent.putExtra("uri",uri_login);
                startActivity(intent);
            }
            else {
                String uri_comp = EventsBCNClient.getInstance().getUriCompany();
                Intent intent = new Intent(EventsMainActivity.this,CompanyProfileActivity.class);
                intent.putExtra("uri",uri_comp);
                startActivity(intent);
            }


        } else if (id == R.id.nav_edituser) {

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EventsMainActivity.this);
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




    class GetEventsTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            String jsonEventCollection = null;
            try{
                jsonEventCollection = EventsBCNClient.getInstance().getEvents(null);
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

    class DeleteEventTask extends AsyncTask<Void,Void,Boolean>{
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

    public class GetUserTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... params) {

            String result = EventsBCNClient.getInstance().getUserByLogin();
            return result;
        }

        @Override
        protected void onPostExecute(String result){

            uri_login = result;
            System.out.println("URI DEL LOGIN       "+uri_login);

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
                Intent intent = new Intent(EventsMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else Toast.makeText(EventsMainActivity.this, "Error, intentalo de nuevo",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
