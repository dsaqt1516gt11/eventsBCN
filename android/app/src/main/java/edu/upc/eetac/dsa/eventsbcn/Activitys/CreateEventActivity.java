package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Calendar;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClientException;
import edu.upc.eetac.dsa.eventsbcn.entity.Company;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    // Widget GUI
    Button btnCalendar, btnTimePicker,btnCreateEvent;
    EditText txtDate, txtTime,etxtitle,etxdescription,etxphoto,etxcategory;


    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String date,companyid;
    private GetCompanyTask mGetCompanyTask;
    private CreateEventTask mCreateEventTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);

        etxtitle = (EditText) findViewById(R.id.Event_name);
        etxdescription = (EditText) findViewById(R.id.Event_description);
        etxphoto = (EditText) findViewById(R.id.Event_Photo);
        etxcategory = (EditText)  findViewById(R.id.Event_Category);

        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);

        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCreateEvent.setOnClickListener(this);

        // Execute AsyncTask para coger el companyid
       mGetCompanyTask = new GetCompanyTask();
        mGetCompanyTask.execute((Void) null);
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
    }

    public void creaEvento(){
        String title= etxtitle.getText().toString();
        String descripcion = etxdescription.getText().toString();
        String photo = etxphoto.getText().toString();
        String category = etxcategory.getText().toString();
        date = txtDate.getText().toString()+" "+txtTime.getText().toString();
        System.out.println(date);

        mCreateEventTask = new CreateEventTask(title,descripcion,photo,category,date,companyid);
        mCreateEventTask.execute((Void) null);


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
        String photo;
        String category;
        String date;
        String companyid;

        public CreateEventTask(String title, String descripcion, String photo, String category, String date, String companyid) {
            this.title = title;
            this.descripcion = descripcion;
            this.photo = photo;
            this.category = category;
            this.date = date;
            this.companyid = companyid;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            EventsBCNClient client = EventsBCNClient.getInstance();
            boolean result = false;

                result = client.createEvent(title,descripcion,photo,category,date,companyid);
                return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            startActivity(new Intent(CreateEventActivity.this, EventsMainActivity.class));

        }

    }

}
