package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.AnimationUtil;
import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.ItemClickListener;
import com.lscarp4.lscarpl4assessments.R;

import java.util.ArrayList;
import java.util.Arrays;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    ArrayList<Feedback_Pojo> feedbackPojoArrayList;
    private ItemClickListener clickListener;
    private Context mContext;
    int previousPosition = 0;
    private int[] state;
    DBHelper mydb;
    String textOption;
    String STUDENT_ID;
    String QnumInt2String;

    // Pass in the country array into the constructor
    public FeedbackAdapter(ArrayList<Feedback_Pojo> feedbackPojoArrayList, Context context,String get_stdId) {
        this.feedbackPojoArrayList = feedbackPojoArrayList;
        this.mContext = context;
        this.STUDENT_ID = get_stdId;

        this.state = new int[feedbackPojoArrayList.size()];
        Arrays.fill(this.state, -1);
    }


    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_cardview, parent, false);
        mydb = new DBHelper(mContext);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedbackAdapter.ViewHolder holder, final int position) {


        holder.question.setText(feedbackPojoArrayList.get(position).getQnum() + " . " + feedbackPojoArrayList.get(position).getQues());
        holder.op1.setText(feedbackPojoArrayList.get(position).getOption1());
        holder.op2.setText(feedbackPojoArrayList.get(position).getOption2());
        holder.op3.setText(feedbackPojoArrayList.get(position).getOption3());
        holder.op4.setText(feedbackPojoArrayList.get(position).getOption4());
        holder.op5.setText(feedbackPojoArrayList.get(position).getOption5());

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
                    holder.btn = (RadioButton) holder.radioGroup.getChildAt(i);
                    if (holder.btn.getId() == checkedId) {
                         textOption = (String) holder.btn.getText();

                        Integer qnum = feedbackPojoArrayList.get(position).getQnum();
                        QnumInt2String = String.valueOf(qnum);

                        boolean recordExists= mydb.checkFeedbackExists(mydb.feedback_table ,mydb.feed_col_1 ,QnumInt2String);
                        if(recordExists)
                        {

                            if(textOption == null){

                            }else{
                                UpdateFeedback(QnumInt2String,null,textOption);
                            }


                        }else {

                        }



                        Log.e("RADIOGRP1", " >>>>>>>>>>>>>>>" +feedbackPojoArrayList.get(position).getQnum() + " " + textOption + " " + QnumInt2String);

                        return;
                    } else {

                    }
                }
            }
        });



        //Getting the position of items in recyclerview
        if(position > previousPosition){ //we are scrolling DOWN

            AnimationUtil.animate(holder, true);

        }else{  //we are scrolling UP

            AnimationUtil.animate(holder, false);
        }

        previousPosition = position;
    }

    @Override
    public int getItemCount() {
        return feedbackPojoArrayList == null ? 0 : feedbackPojoArrayList.size();

    }

    //for updating the selected options for the question in database
    public void UpdateFeedback(String qid, String old_option, String new_option) {

        ContentValues contentValues = new ContentValues();

        final String whereClause = mydb.feed_col_1 + " =?";
        final String[] whereArgs = {
                qid
        };


        contentValues.put(mydb.feed_col_2, new_option);

        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
        sqLiteDatabase.update(mydb.feedback_table, contentValues,
                whereClause, whereArgs);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public Feedback_Pojo feedback_pojo(int position) {
        return feedbackPojoArrayList.get(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        String selectedoption = "";
        RadioGroup radioGroup;
        private RadioButton op1,op2,op3,op4,op5,btn;
        TextView question;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = (TextView) itemView.findViewById(R.id.ques);
            op1 = (RadioButton) itemView.findViewById(R.id.op1);
            op2 = (RadioButton) itemView.findViewById(R.id.op2);
            op3 = (RadioButton) itemView.findViewById(R.id.op3);
            op4 = (RadioButton) itemView.findViewById(R.id.op4);
            op5 = (RadioButton) itemView.findViewById(R.id.op5);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.rdg1);



            itemView.setTag(itemView);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
