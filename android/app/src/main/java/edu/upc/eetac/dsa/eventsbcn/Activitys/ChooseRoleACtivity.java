package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.upc.eetac.dsa.eventsbcn.R;

public class ChooseRoleACtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mRegistreredButton = (Button) findViewById(R.id.registered_button);
        mRegistreredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRoleACtivity.this, RegisterUserActivity.class);
                String role = "registered";
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });

        Button mCompanyButton = (Button) findViewById(R.id.company_button);
        mCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRoleACtivity.this, RegisterUserActivity.class);
                String role = "company";
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
    }

}
