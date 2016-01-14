package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventListAdapter;
import edu.upc.eetac.dsa.eventsbcn.client.UserListAdapter;
import edu.upc.eetac.dsa.eventsbcn.entity.User;

public class UserListActivity extends AppCompatActivity {

    private final static String TAG = UserListActivity.class.toString();
    private List<User> asistentes;
    private UserListAdapter userListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        asistentes = (List<User>)intent.getSerializableExtra("asistentes");
        Log.d(TAG, asistentes.toString());



        // set list adapter
        ListView list = (ListView)findViewById(R.id.userlist);
        userListAdapter = new UserListAdapter(this, asistentes);
        list.setAdapter(userListAdapter);


    }

}
