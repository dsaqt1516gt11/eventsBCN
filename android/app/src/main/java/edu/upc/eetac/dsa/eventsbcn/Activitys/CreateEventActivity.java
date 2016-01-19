package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClientException;
import edu.upc.eetac.dsa.eventsbcn.entity.Company;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    // Widget GUI
    Button btnCalendar, btnTimePicker,btnCreateEvent,btnPhotoEvent;
    EditText txtDate, txtTime,etxtitle,etxdescription;
    RadioButton radio_cine,radio_teatro,radio_bar,radio_disco;


    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String date,companyid,category;
    private GetCompanyTask mGetCompanyTask;
    private CreateEventTask mCreateEventTask;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private File outputFile;
    private Context context;
    private RadioGroup rgcat;
    private View mProgressView;
    private View mFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context =this;

        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);
        btnPhotoEvent = (Button) findViewById(R.id.btnphotoevent);

        etxtitle = (EditText) findViewById(R.id.Event_name);
        etxdescription = (EditText) findViewById(R.id.Event_description);

        rgcat = (RadioGroup) findViewById(R.id.rgcat);
        radio_bar = (RadioButton) findViewById(R.id.radio_bar);
        radio_cine = (RadioButton) findViewById(R.id.radio_cine);
        radio_disco = (RadioButton) findViewById(R.id.radio_disco);
        radio_teatro = (RadioButton) findViewById(R.id.radio_teatro);


        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);

        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCreateEvent.setOnClickListener(this);
        btnPhotoEvent.setOnClickListener(this);

        rgcat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_bar) {
                    category = radio_bar.getText().toString();
                } else if (checkedId == R.id.radio_cine) {
                    category = radio_cine.getText().toString();
                } else if (checkedId == R.id.radio_disco) {
                    category = radio_disco.getText().toString();
                } else if (checkedId == R.id.radio_teatro) {
                    category = radio_teatro.getText().toString();
                }
            }
        });

        // Execute AsyncTask para coger el companyid
        mGetCompanyTask = new GetCompanyTask();
        mGetCompanyTask.execute((Void) null);

        mFormView = findViewById(R.id.form_createevent);
        mProgressView = findViewById(R.id.createvent_progress);
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

    public void onClick(View v) {

        if (v == btnCalendar) {

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            txtDate.setText(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnTimePicker) {

            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // Display Selected time in textbox
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }
        if(v==btnCreateEvent){

            creaEvento();

        }
        if(v==btnPhotoEvent){

            selectImage();
        }

    }

    public void creaEvento(){
        String title= etxtitle.getText().toString();
        String descripcion = etxdescription.getText().toString();
        date = txtDate.getText().toString()+" "+txtTime.getText().toString();
        System.out.println(date);
        System.out.println("CATEGORIA EN CREATEEVENT        "+category);

        if(txtTime.getText().toString().equals("")||txtDate.getText().toString().equals("")||outputFile==null||title.equals("")||descripcion.equals("")||category.equals("")){
            Toast.makeText(CreateEventActivity.this, "Faltan datos por rellenar",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            showProgress(true);
            mCreateEventTask = new CreateEventTask(title, descripcion, category, date, companyid);
            mCreateEventTask.execute((Void) null);
        }

    }

    private void selectImage() {
        final CharSequence[] items = { "Camara", "Galeria", "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
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



            }
        }
    }

    class GetCompanyTask extends AsyncTask<Void,Void,String>{



        @Override
        protected String doInBackground(Void... params) {
            String jsonCompany=null;
            try {
                jsonCompany = EventsBCNClient.getInstance().getCompanyByUserid();
            } catch (EventsBCNClientException e) {
                e.printStackTrace();
            }
            return jsonCompany;
        }

        @Override
        protected void onPostExecute(String jsonEvent) {
            Company company = (new Gson()).fromJson(jsonEvent, Company.class);
            companyid = company.getId();

        }
    }

    class CreateEventTask extends AsyncTask<Void,Void,Boolean>{
        String title;
        String descripcion;
        String category;
        String date;
        String companyid;

        public CreateEventTask(String title, String descripcion,String category, String date, String companyid) {
            this.title = title;
            this.descripcion = descripcion;
            this.category = category;
            this.date = date;
            this.companyid = companyid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CreateEventActivity.this, "Cargando...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            EventsBCNClient client = EventsBCNClient.getInstance();
            boolean result = false;


                result = client.createEvent(title,descripcion,category,date,companyid,outputFile);
                return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                showProgress(false);
                startActivity(new Intent(CreateEventActivity.this, EventsMainActivity.class));
                finish();
            }
            else {
                showProgress(false);
                Toast.makeText(CreateEventActivity.this, "El nombre de este evento ya existe!",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

}
