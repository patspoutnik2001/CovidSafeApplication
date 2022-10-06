package com.example.covidsafeapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LocalListAdapter extends BaseAdapter {

    Context context;
    Local[] locals;
    LayoutInflater inflater;

    public LocalListAdapter(Context ctx, ArrayList<Local> list){
        this.context = ctx;
        this.locals = new Local[list.size()];
        locals=list.toArray(locals);
        inflater  = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return locals.length;
    }

    @Override
    public Local getItem(int i) {
        return locals[i];
    }

    @Override
    public long getItemId(int i) {
        return locals[i].id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.local_custom_list_view,null);
        TextView txtViewLocal = (TextView) view.findViewById(R.id.local_name_custom);
        txtViewLocal.setText(locals[i].name);
        return view;
    }
}
