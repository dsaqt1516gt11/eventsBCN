package edu.upc.eetac.dsa.eventsbcn.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.entity.User;

/**
 * Created by Joe on 6/1/16.
 */
public class UserListAdapter extends BaseAdapter {

    private List<User> users;
    private LayoutInflater layoutInflater;
    private Context mcontext;
    private final static String TAG = UserListAdapter.class.toString();

    public UserListAdapter(Context context, List<User> users){
        mcontext=context;
        layoutInflater = LayoutInflater.from(context);
        this.users = users;

    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.user_list_row, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d(TAG,users.toString());
        String name = users.get(position).getName();
        String image = users.get(position).getPhotoURL();
        System.out.println(name);
        System.out.println(image);

        viewHolder.textViewname.setText(name);
        Picasso.with(mcontext).load(image).into(viewHolder.imageViewphoto);

        //new DownloadImageTask(viewHolder.imageViewphoto).execute(image);

        return convertView;
    }

    class ViewHolder{
        TextView textViewname;
        ImageView imageViewphoto;


        ViewHolder(View row){
            this.textViewname = (TextView) row
                    .findViewById(R.id.username_row);

            this.imageViewphoto = (ImageView) row.findViewById(R.id.user_photo_row);

        }
    }

   /* public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private Bitmap img;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new URL(urldisplay).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                img = null;
            }
            return img;
        }

        @SuppressLint("NewApi")
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }        }*/
}
