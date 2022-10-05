package com.example.covidsafeapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    Batiment[] batiments;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, ArrayList<Batiment> list ){
        this.context = ctx;
        this.batiments = new Batiment[list.size()];
        batiments=list.toArray(batiments);


        inflater  = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return batiments.length;
    }

    @Override
    public Batiment getItem(int i) {
        return batiments[i];
    }

    @Override
    public long getItemId(int i) {
        return batiments[i].idBatiment;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view,null);
        TextView txtView = (TextView) view.findViewById(R.id.display_text);
        txtView.setText(batiments[i].nomBatiment);
        return view;
    }
}
