package com.lscarp4.lscarpl4assessments;

import android.app.ProgressDialog;

import java.util.TimerTask;

public class CloseDialogueTimerTask extends TimerTask {

    //declare progress dialogue
    private ProgressDialog pd;

    //Passing the pDialog into the Constructor
    public CloseDialogueTimerTask(ProgressDialog pDialog) {
        this.pd = pDialog;
    }

    @Override
    public void run() {

        // Dismiss the progress dialog
        if (pd.isShowing())
            pd.dismiss();
    }
}
