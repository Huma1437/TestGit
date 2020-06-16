package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.AnimationUtil;
import com.lscarp4.lscarpl4assessments.ItemClickListener;
import com.lscarp4.lscarpl4assessments.R;

import java.util.ArrayList;

public class Viva_Ques_Adapter extends RecyclerView.Adapter<Viva_Ques_Adapter.ViewHolder>  {

    ArrayList<Viva_Ques_pojo> viva_ques_pojos;
    private ItemClickListener clickListener;
    private Context mContext;
    int previousPosition = 0;
    ArrayList<String> marks_given;
    String getEdval;


    // Pass in the country array into the constructor
    public Viva_Ques_Adapter(ArrayList<Viva_Ques_pojo> viva_ques_pojos, Context context) {
        this.viva_ques_pojos = viva_ques_pojos;
        this.mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public Viva_Ques_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viva_ques_card, parent, false);

        marks_given = new ArrayList<>();

        return new Viva_Ques_Adapter.ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final Viva_Ques_Adapter.ViewHolder holder, final int position) {

        holder.title.setText(viva_ques_pojos.get(position).getNos_code() + "-" + viva_ques_pojos.get(position).getNos_title());
        holder.snum.setText(viva_ques_pojos.get(position).getNos_id() + ". ");
        holder.ques.setText(viva_ques_pojos.get(position).getQuestion() + "(" + viva_ques_pojos.get(position).getQn_max_marks() + " Marks" +")");


        //Getting the position of items in recyclerview
        if(position > previousPosition){ //we are scrolling DOWN

            AnimationUtil.animate(holder, true);

        }else{  //we are scrolling UP

            AnimationUtil.animate(holder, false);
        }

        previousPosition = position;
    }



    public String getValue(int position){
        return  marks_given.get(position);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return viva_ques_pojos == null ? 0 : viva_ques_pojos.size();
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public Viva_Ques_pojo viva_ques_pojo(int position) {
        return viva_ques_pojos.get(position);
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView snum,ques,title;
        EditText marks;
        EditText_Values_model edt;

        /*constructor that accepts the entire item row
             and does the view lookups to find each subview */
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            snum = (TextView) itemView.findViewById(R.id.number);
            ques = (TextView) itemView.findViewById(R.id.ques);
            marks = (EditText) itemView.findViewById(R.id.marks);

            marks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    marks_given.add(getAdapterPosition(),s.toString());


                    if(!marks.getText().toString().isEmpty()){
                        int getVal  = Integer.parseInt(marks.getText().toString());
                        int pojoVal = Integer.parseInt(viva_ques_pojos.get(getAdapterPosition()).getQn_max_marks());

                        if( getVal > pojoVal){
                            marks.setError("Marks should not be greater than max marks");
                            marks.setText("");
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });


            itemView.setTag(itemView);

        }

        public String getValue(int position){
            return  marks_given.get(position);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            // call the onClick in the OnItemClickListener

        }

    }
}

