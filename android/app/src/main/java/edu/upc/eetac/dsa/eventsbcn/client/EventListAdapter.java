package edu.upc.eetac.dsa.eventsbcn.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import edu.upc.eetac.dsa.eventsbcn.R;
import edu.upc.eetac.dsa.eventsbcn.entity.EventCollection;

/**
 * Created by juan on 8/12/15.
 */
public class EventListAdapter extends BaseAdapter {
    private EventCollection eventCollection;
    private LayoutInflater layoutInflater;


    public EventListAdapter(Context context, EventCollection eventCollection){
        layoutInflater = LayoutInflater.from(context);
        this.eventCollection = eventCollection;

    }

    @Override
    public int getCount() {
        return eventCollection.getEvents().size();
    }

    @Override
    public Object getItem(int position) {
        return eventCollection.getEvents().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.event_list_row, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String title = eventCollection.getEvents().get(position).getTitle();
        String date = eventCollection.getEvents().get(position).getDate();
        String category = eventCollection.getEvents().get(position).getCategory();

        viewHolder.textViewtitle.setText(title);
        viewHolder.textViewdate.setText(date);
        viewHolder.textViewcategory.setText(category);
        return convertView;
    }




    class ViewHolder{
        TextView textViewtitle;
        TextView textViewdate;
        TextView textViewcategory;

        ViewHolder(View row){
            this.textViewtitle = (TextView) row
                    .findViewById(R.id.title);
            this.textViewdate = (TextView) row
                    .findViewById(R.id.date);
            this.textViewcategory = (TextView) row
                    .findViewById(R.id.category);
        }
    }


}
