package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;

public class RegisterUserActivity extends AppCompatActivity {

    public EditText nombre;
    public EditText email;
    public EditText password;
    public EditText photo;
    public CheckBox cat_cine;
    public CheckBox cat_bar;
    public CheckBox cat_discoteca;
    public CheckBox cat_teatro;
    private String role;
    public ArrayList<String> categorias;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categorias = new ArrayList<>();

        role = (String) getIntent().getExtras().get("role");
        System.out.println("ROL EN RESGITERUSER        "+role);


        nombre = (EditText) findViewById(R.id.Username);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Pass);


        cat_bar = (CheckBox) findViewById(R.id.checkbox_bar);
        cat_discoteca = (CheckBox) findViewById(R.id.checkbox_discoteca);
        cat_teatro = (CheckBox) findViewById(R.id.checkbox_teatro);
        cat_cine = (CheckBox) findViewById(R.id.checkbox_cine);







        final Button registrar = (Button) findViewById(R.id.btnRegister);
        if(role.equals("company"))
            registrar.setText("Siguiente");
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    addCategories();
                    registrar();
            }
        });

//
    }

    // METODO PARA INTRODUCIR CATEGORIAS A LA LISTA QUE ENVIAMOS EN EL REGISTRO

   public void addCategories() {


        if(cat_teatro.isChecked())
            categorias.add("teatro");
        if(cat_discoteca.isChecked())
            categorias.add("discoteca");
        if(cat_bar.isChecked())
            categorias.add("bar");
        if(cat_cine.isChecked())
            categorias.add("cine");
   }

    public void registrar() {
        System.out.println("entrooooo");
        String mName = nombre.getText().toString();
        System.out.println(mName);
        String mPass = password.getText().toString();
        String mMail = email.getText().toString();
        System.out.println("youuuu");
        if(mName.equals("")||mPass.equals("")||mMail.equals("")||categorias.size()==0){
            Toast.makeText(RegisterUserActivity.this, "Faltan datos por rellenar",
                    Toast.LENGTH_SHORT).show();
            categorias = new ArrayList<>();

        }
        else {
            Intent intent = new Intent(RegisterUserActivity.this, CapturaFotoActivity.class);
            intent.putExtra("nombre", mName);
            intent.putExtra("pass", mPass);
            intent.putExtra("mail", mMail);
            intent.putStringArrayListExtra("categorias", categorias);
            intent.putExtra("role", role);
            startActivity(intent);
            
        }

        //mRegisterTask = new RegisterUserTask(mName, mPass, mMail, mPass, categorias, role);
        //mRegisterTask.execute((Void) null);


    }


}