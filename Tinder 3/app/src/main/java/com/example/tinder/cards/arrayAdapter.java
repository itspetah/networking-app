package com.example.tinder.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.tinder.R;

import java.util.List;

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: arrayAdapter.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: Adapter main source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
        switch (card_item.getProfileImageUrl()){
            case "default":
                Glide.with(getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }
}
