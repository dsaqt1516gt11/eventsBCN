package edu.upc.eetac.dsa.eventsbcn.Activitys;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.client.EventsBCNClient;

public class CapturaFotoActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    ImageView picture;
    private Bitmap photo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_foto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;


        picture = (ImageView) findViewById(R.id.picture);
        Button buttonCamera = (Button) findViewById(R.id.capturarfoto);
        buttonCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                /*intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);*/
                try {

                    //intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);

                } catch (ActivityNotFoundException e) {
                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




            if (requestCode == PICK_FROM_CAMERA) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    photo = (Bitmap) extras.get("data");
                    picture.setImageBitmap(photo);

                    (new ImageUploader()).execute();

                }
            }

    }

    private class ImageUploader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            String filename = "test";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 60, bos);
            Log.d("File:  ", bos.toString());
            byte[] imageBytes = bos.toByteArray();

            File outputDir = context.getCacheDir(); // context being the Activity pointer
            File outputFile = new File(filename);
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
            FileBody bin = new FileBody(outputFile);

            boolean result = EventsBCNClient.getInstance().registerUserImage(outputFile);


        return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CapturaFotoActivity.this, "Subiendo...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Toast.makeText(CapturaFotoActivity.this, "Foto subida con éxito!",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CapturaFotoActivity.this,EventsMainActivity.class));
        }
    }

}
