package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.AnimationUtil;
import com.lscarp4.lscarpl4assessments.ItemClickListener;
import com.lscarp4.lscarpl4assessments.R;

import java.util.ArrayList;

public class BCompleted_Adapter extends RecyclerView.Adapter<BCompleted_Adapter.ViewHolder>  {

    ArrayList<Batches_Assigned_Pojo> batches_assigned_pojos;
    private ItemClickListener clickListener;
    private Context mContext;
    int previousPosition = 0;
    String tc_name,tb_name,batch_name,tb_start_date_time,tb_end_date_time,tb_assessment_status,tc_id,tb_exam_type,
            tb_id,tb_target,e_id,sscid,trade_id,tp_id,qp_shuffling,trade_title;
    // Pass in the country array into the constructor
    public BCompleted_Adapter(ArrayList<Batches_Assigned_Pojo> batches_assigned_pojos, Context context) {
        this.batches_assigned_pojos = batches_assigned_pojos;
        this.mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public BCompleted_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batches_completed_card, parent, false);

        return new BCompleted_Adapter.ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(BCompleted_Adapter.ViewHolder holder, final int position) {


        holder.batch_ID.setText("Batch ID: " + batches_assigned_pojos.get(position).getTb_name());

        if(batches_assigned_pojos.get(position).getBatch_name().equals("null")){
            holder.bname.setText("Batch Name: " + "");
        }else {
            holder.bname.setText("Batch Name: " + batches_assigned_pojos.get(position).getBatch_name());
        }

        holder.job_role.setText("Job Role: " + batches_assigned_pojos.get(position).getTrade_title());
        holder.start_date.setText("Start Date: " +batches_assigned_pojos.get(position).getTb_start_date_time());
        holder.end_date.setText("End Date: " + batches_assigned_pojos.get(position).getTb_end_date_time());
        holder.stdnum.setText("No. of Students: "+ batches_assigned_pojos.get(position).getTb_target());
        holder.bstatus.setText("Batch Status: " + batches_assigned_pojos.get(position).getTb_assessment_status());

        //Getting the position of items in recyclerview
        if(position > previousPosition){ //we are scrolling DOWN

            AnimationUtil.animate(holder, true);

        }else{  //we are scrolling UP

            AnimationUtil.animate(holder, false);
        }

        previousPosition = position;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return batches_assigned_pojos == null ? 0 : batches_assigned_pojos.size();
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public Batches_Assigned_Pojo batches_pojo(int position) {
        return batches_assigned_pojos.get(position);
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* Your holder should contain a member variable
     for any view that will be set as you render a row */
        TextView batch_ID,bname,job_role,start_date,end_date,stdnum,bstatus;


        /*constructor that accepts the entire item row
             and does the view lookups to find each subview */
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            batch_ID = (TextView) itemView.findViewById(R.id.batch_id);
            bname = (TextView) itemView.findViewById(R.id.batch_name);
            job_role = (TextView) itemView.findViewById(R.id.jrole);
            start_date = (TextView) itemView.findViewById(R.id.sdate);
            end_date = (TextView) itemView.findViewById(R.id.edate);
            stdnum = (TextView) itemView.findViewById(R.id.stdnum);
            bstatus = (TextView) itemView.findViewById(R.id.batch_status);

            itemView.setTag(itemView);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            // call the onClick in the OnItemClickListener

            tc_name = batches_pojo(getAdapterPosition()).getTc_name();
            tb_name = batches_pojo(getAdapterPosition()).getTb_name();
            batch_name = batches_pojo(getAdapterPosition()).getBatch_name();
            tb_start_date_time = batches_pojo(getAdapterPosition()).getTb_start_date_time();
            tb_end_date_time = batches_pojo(getAdapterPosition()).getTb_end_date_time();
            tb_assessment_status = batches_pojo(getAdapterPosition()).getTb_assessment_status();
            tc_id = batches_pojo(getAdapterPosition()).getTc_id();
            tb_exam_type = batches_pojo(getAdapterPosition()).getTb_exam_type();
            tb_id = batches_pojo(getAdapterPosition()).getTb_id();
            tb_target = batches_pojo(getAdapterPosition()).getTb_target();
            e_id = batches_pojo(getAdapterPosition()).getE_id();
            sscid =batches_pojo(getAdapterPosition()).getSscid();
            trade_id = batches_pojo(getAdapterPosition()).getTrade_id();
            tp_id = batches_pojo(getAdapterPosition()).getTp_id();
            qp_shuffling = batches_pojo(getAdapterPosition()).getQp_shuffling();
            trade_title = batches_pojo(getAdapterPosition()).getTrade_title();

        }

    }
}

