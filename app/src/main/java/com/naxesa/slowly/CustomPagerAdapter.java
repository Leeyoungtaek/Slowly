package com.naxesa.slowly;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lee young teak on 2016-09-11.
 */
public class CustomPagerAdapter extends PagerAdapter {

    // Data
    private ArrayList<Message> messages;

    // Inflater
    private LayoutInflater inflater;

    // Constructor
    public CustomPagerAdapter(LayoutInflater inflater, ArrayList<Message> messages){
        this.inflater = inflater;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        view = inflater.inflate(R.layout.item_book_content, null);
        TextView person = (TextView)view.findViewById(R.id.text_view_person);
        TextView content = (TextView)view.findViewById(R.id.text_view_content);
        TextView place = (TextView)view.findViewById(R.id.text_view_place);
        person.setText("(" + messages.get(position).getPerson() + ")");
        content.setText(messages.get(position).getContent());
        place.setText(messages.get(position).getPlace());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
