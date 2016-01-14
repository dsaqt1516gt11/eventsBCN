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
    private RegisterUserTask mRegisterTask = null;
    public List<String> categorias;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categorias = new ArrayList<>();

        role = (String) getIntent().getExtras().get("role");


        nombre = (EditText) findViewById(R.id.Username);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Pass);
        photo = (EditText) findViewById(R.id.Photo);

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
        String mFoto = photo.getText().toString();
        System.out.println("youuuu");



        mRegisterTask = new RegisterUserTask(mName, mPass, mMail, mPass, categorias, role);
        mRegisterTask.execute((Void) null);


    }

    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        String name;
        String pass;
        String mail;
        String foto;
        List<String> categories;
        String role;


        public RegisterUserTask(String name, String pass, String mail, String foto, List<String> categories, String role) {
            this.name = name;
            this.pass = pass;
            this.mail = mail;
            this.foto = foto;
            this.categories = categories;
            this.role = role;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            EventsBCNClient client = EventsBCNClient.getInstance();
            boolean result = false;
            try {
                result = client.registerUser(name, pass, mail, foto, categories, role);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return result;


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            //showProgress(false);

            if (success) {
                if(role.equals("registered"))
                startActivity(new Intent(RegisterUserActivity.this, EventsMainActivity.class));
                else startActivity(new Intent(RegisterUserActivity.this,RegisterCompanyActivity.class));
            } else {
                System.out.println("error");
            }
        }

    }
}