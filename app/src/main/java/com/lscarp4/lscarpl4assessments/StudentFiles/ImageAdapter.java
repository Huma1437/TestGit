package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lscarp4.lscarpl4assessments.R;

public class ImageAdapter extends BaseAdapter {
    Context context;
    String[] numbers;
    String[] not_ans;
    String[] marked;
    String[] not_visit;

    // Constructor
    public ImageAdapter(Context c, String[] numbers, String[] not_ans, String[] marked, String[] not_visit) {
        this.context = c;
        this.numbers = numbers;
        this.not_ans = not_ans;
        this.marked = marked;
        this.not_visit = not_visit;
    }

    public int getCount() {
        return numbers.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = new View(context);
            convertView = inflater.inflate(R.layout.grid_text, parent,false);

        } else {
            convertView = (View) convertView;
        }

        TextView textViewAndroid = (TextView) convertView.findViewById(R.id.gridText);
        textViewAndroid.setText(numbers[position]);

        if(marked[position].equals("1")){
            textViewAndroid.setBackgroundResource(R.drawable.purple_circle);
        }else if(not_visit[position].equals("0")){
            textViewAndroid.setBackgroundResource(R.drawable.grey_circle);
        } else if(not_ans[position].equals("0")){
            textViewAndroid.setBackgroundResource(R.drawable.red_circle);
        }else if(not_ans[position].equals("1")){
            textViewAndroid.setBackgroundResource(R.drawable.green_circle);
        }

        return convertView;

    }


}