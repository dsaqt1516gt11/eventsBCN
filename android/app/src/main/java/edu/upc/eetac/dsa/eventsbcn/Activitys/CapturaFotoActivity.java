package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;

public class CapturaFotoActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    ImageView picture;
    private Bitmap photo;
    Context context;
    public ArrayList<String> categorias;
    private String name;
    private String pass;
    private String mail;
    private String role;
    private File outputFile= null;
    private View mProgressView;
    private View mFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_foto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;

        // PRUEBA CON GALERIA Y FOTO A LA VEZ/////////////
        name = (String) getIntent().getExtras().get("nombre");
        pass = (String) getIntent().getExtras().get("pass");
        mail = (String) getIntent().getExtras().get("mail");
        role = (String) getIntent().getExtras().get("role");
        System.out.println("ROL EN CAPTURAFOTO       "+role);
        categorias = getIntent().getExtras().getStringArrayList("categorias");
        //////////////////////////////////////////////////


        picture = (ImageView) findViewById(R.id.picture);
        Button buttonCamera = (Button) findViewById(R.id.capturarfoto);
        buttonCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                selectImage();

            }
        });

        Button registro = (Button) findViewById(R.id.enviarfoto);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outputFile == null)
                    Toast.makeText(CapturaFotoActivity.this, "Debes añadir una foto!",
                            Toast.LENGTH_SHORT).show();
                else {
                    if (role.equals("registered")) {
                        showProgress(true);
                        (new RegisterUserTask(outputFile, categorias, name, pass, mail, role)).execute();


                    } else {
                        Intent intent = new Intent(CapturaFotoActivity.this, RegisterCompanyActivity.class);
                        intent.putExtra("nombre", name);
                        intent.putExtra("pass", pass);
                        intent.putExtra("mail", mail);
                        intent.putStringArrayListExtra("categorias", categorias);
                        intent.putExtra("role", role);
                        intent.putExtra("foto", outputFile);
                        startActivity(intent);

                    }
                }
            }
        });

        mFormView = findViewById(R.id.form_captura_foto);
        mProgressView = findViewById(R.id.captura_foto_progress);


    }


    /**
     * Shows the progress UI and hides the Registeruser form.
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



    private void selectImage() {
        final CharSequence[] items = { "Camara", "Galeria", "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CapturaFotoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camara")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else if (items[item].equals("Galeria")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FROM_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                byte[] imageBytes = bytes.toByteArray();

                File outputDir = context.getCacheDir(); // context being the Activity pointer
                outputFile = new File("test");
                try {
                    outputFile = File.createTempFile("prefix", "extension", outputDir);
                }catch (IOException e){

                }

                try {

                    FileOutputStream fos = new FileOutputStream(outputFile);
                    fos.write(imageBytes);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    Log.d("Fallo:   ", "Relleno fichero");
                }


                picture.setImageBitmap(thumbnail);
                //(new RegisterUserTask(outputFile,categorias,name,pass,mail,foto,role)).execute();

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                byte[] imageBytes = bytes.toByteArray();
                File outputDir = context.getCacheDir(); // context being the Activity pointer
                outputFile = new File("test");
                try {
                    outputFile = File.createTempFile("prefix", "extension", outputDir);
                }catch (IOException e){

                }

                try {

                    FileOutputStream fos = new FileOutputStream(outputFile);
                    fos.write(imageBytes);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    Log.d("Fallo:   ", "Relleno fichero");
                }

                picture.setImageBitmap(bm);
                //(new RegisterUserTask(outputFile,categorias,name,pass,mail,foto,role)).execute();
            }
        }
    }


        private class RegisterUserTask extends AsyncTask<Void, Void, Integer> {
            File photo;
            public ArrayList<String> categorias;
            private String name;
            private String pass;
            private String mail;
            private String foto;
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
            Toast.makeText(CapturaFotoActivity.this, "Cargando...",
                    Toast.LENGTH_LONG).show();
        }




        @Override
        protected void onPostExecute(Integer result) {
            showProgress(false);
            if(result==1) {
                Toast.makeText(CapturaFotoActivity.this, "Foto subida con éxito!",
                        Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(CapturaFotoActivity.this, EventsMainActivity.class));
                    finish();

            }
            else if(result==-1){
                Toast.makeText(CapturaFotoActivity.this, "Este nombre de usuario ya existe",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CapturaFotoActivity.this, ChooseRoleACtivity.class));
                finish();
            }
            else {
                Toast.makeText(CapturaFotoActivity.this, "Ha ocurrido un error, intente de nuevo",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CapturaFotoActivity.this, ChooseRoleACtivity.class));
                finish();
            }


        }
    }

}
