package com.example.scripters_society.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scripters_society.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class AdapterPublications extends BaseAdapter {

    private ArrayList<Publication> listPublications;
    private Context context;
    private LayoutInflater inflater;

    public AdapterPublications(Context context, ArrayList<Publication> listPublications) {
        this.listPublications = listPublications;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listPublications.size();
    }

    @Override
    public Object getItem(int index) {
        return listPublications.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Publication publication = (Publication) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_public_with_image, null);
        ImageView imgFoto = (ImageView) convertView.findViewById(R.id.imgPublicacion);
        TextView tvNameUser = (TextView) convertView.findViewById(R.id.tvName_user);
        TextView tvHoraPublished = (TextView) convertView.findViewById(R.id.tvHora_pub);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);


        tvNameUser.setText(publication.getName());
        tvDescription.setText(publication.getDescription());
        tvHoraPublished.setText(dateTimeFormatted(publication.getUpdated_at()));

        return convertView;
    }

    private String dateTimeFormatted(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm | dd/MM/yyyy");
            Date date = inputFormat.parse(dateTime);
            String outputDate = outputFormat.format(date);
            return outputDate;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
}
