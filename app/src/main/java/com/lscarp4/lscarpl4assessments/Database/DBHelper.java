package com.lscarp4.lscarpl4assessments.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    //Questions table with its column [Online working]
    public static final String DATABASE_NAME = "I-Vintage";
    public static final String TABLE_NAME = "StudentData";
    public static final String COLUMN_1 = "q_id";
    public static final String COLUMN_2 = "selected_option";
    public static final String COLUMN_3 = "mf_review";
    public static final String COLUMN_4 = "not_ans";
    public static final String COLUMN_5 = "not_visited";
    public static final String COLUMN_6 = "Question";
    public static final String COLUMN_7 = "optionA";
    public static final String COLUMN_8 = "optionB";
    public static final String COLUMN_9 = "optionC";
    public static final String COLUMN_10 = "optionD";
    public static final String COLUMN_11 = "qnum";
    public static final String COLUMN_12 = "hindi_Question";
    public static final String COLUMN_13 = "hindi_optionA";
    public static final String COLUMN_14 = "hindi_optionB";
    public static final String COLUMN_15 = "hindi_optionC";
    public static final String COLUMN_16 = "hindi_optionD";

    public static final String Answer_Table = "Answer_Submission";
    public static final String ans_col_1 = "api_key";
    public static final String ans_col_2 = "exam_id";
    public static final String ans_col_3 = "std_id";
    public static final String ans_col_4 = "std_name";
    public static final String ans_col_5 = "examdate";
    public static final String ans_col_6 = "ssc_id";
    public static final String ans_col_7 = "trade_id";
    public static final String ans_col_8 = "tb_id";
    public static final String ans_col_9 = "starttime";
    public static final String ans_col_10 = "endtime";
    public static final String ans_col_11 = "IP_address";
    public static final String ans_col_12 = "browser";
    public static final String ans_col_13 = "question_ids";
    public static final String ans_col_14 = "snapshot_image_name";
    public static final String ans_col_15 = "snapshot_image_date";
    public static final String ans_col_16 = "selected_ans";
    public static final String ans_col_17 = "snaps";

    //Feedback table with its column [Online working]
    public static final String feedback_table = "FeedbackQuesAns";
    public static final String feed_col_1 = "qid";
    public static final String feed_col_2 = "sel_ans";

    private Context mContext;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 24);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creating Question with Options table [Online Working]
        String createTable = "CREATE TABLE "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
               +COLUMN_1 +" TEXT , " +COLUMN_2 +" TEXT , " +COLUMN_3 + " INTEGER , " +COLUMN_4 + " INTEGER , " +COLUMN_5 + " INTEGER , "
        +COLUMN_6 + " TEXT , " +COLUMN_7 +" TEXT , " +COLUMN_8 + " TEXT , " +COLUMN_9 + " TEXT , "
        +COLUMN_10 + " TEXT , " +COLUMN_11  + " INTEGER , "+COLUMN_12 + " TEXT , " +COLUMN_13 +" TEXT , " +COLUMN_14 + " TEXT , " +COLUMN_15 + " TEXT , "
                +COLUMN_16 + " TEXT  " + ");";
        db.execSQL(createTable);

        //Creating Question with Options table [Online Working]
        String createAnswerTable = "CREATE TABLE "+ Answer_Table +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +ans_col_1 +" TEXT , " +ans_col_2 +" TEXT , " +ans_col_3 + " TEXT , " +ans_col_4 + " TEXT , " +ans_col_5 + " TEXT , "
                +ans_col_6 + " TEXT , " +ans_col_7 +" TEXT , " +ans_col_8 + " TEXT , " +ans_col_9 + " TEXT , "
                +ans_col_10 + " TEXT , " +ans_col_11  + " TEXT , "+ans_col_12 + " TEXT , " +ans_col_13 +" TEXT , "
                +ans_col_14 + " TEXT , " +ans_col_15 + " TEXT , "
                +ans_col_16 + " TEXT , "  +ans_col_17 + " TEXT  " + ");";
        db.execSQL(createAnswerTable);

        //Creating Question with Options table [Online Working]
        String createfeedbacktabele = "CREATE TABLE "+ feedback_table +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +feed_col_1 +" TEXT , " +feed_col_2 + " TEXT  " + ");";
        db.execSQL(createfeedbacktabele);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + Answer_Table );
        db.execSQL("DROP TABLE IF EXISTS " + feedback_table );
        onCreate(db);
    }

    //Adding data to the Questions table
    public boolean addData(String item1, String item2, Integer item3, Integer item4, Integer item5, String item6, String item7, String item8, String item9, String item10, Integer item11, String item12, String item13, String item14, String item15, String item16){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_1, item1);
        contentValues.put(COLUMN_2, item2);
        contentValues.put(COLUMN_3, item3);
        contentValues.put(COLUMN_4, item4);
        contentValues.put(COLUMN_5, item5);
        contentValues.put(COLUMN_6, item6);
        contentValues.put(COLUMN_7, item7);
        contentValues.put(COLUMN_8, item8);
        contentValues.put(COLUMN_9, item9);
        contentValues.put(COLUMN_10, item10);
        contentValues.put(COLUMN_11, item11);
        contentValues.put(COLUMN_12, item12);
        contentValues.put(COLUMN_13, item13);
        contentValues.put(COLUMN_14, item14);
        contentValues.put(COLUMN_15, item15);
        contentValues.put(COLUMN_16, item16);

        long result = db.insert(TABLE_NAME, null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Adding data to the Answer table
    public boolean addAnswers(String item1, String item2, String item3, String item4, String item5,
                              String item6, String item7, String item8, String item9, String item10,
                              String item11, String item12, String item13, String item14, String item15,
                              String item16, String item17){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ans_col_1, item1);
        contentValues.put(ans_col_2, item2);
        contentValues.put(ans_col_3, item3);
        contentValues.put(ans_col_4, item4);
        contentValues.put(ans_col_5, item5);
        contentValues.put(ans_col_6, item6);
        contentValues.put(ans_col_7, item7);
        contentValues.put(ans_col_8, item8);
        contentValues.put(ans_col_9, item9);
        contentValues.put(ans_col_10, item10);
        contentValues.put(ans_col_11, item11);
        contentValues.put(ans_col_12, item12);
        contentValues.put(ans_col_13, item13);
        contentValues.put(ans_col_14, item14);
        contentValues.put(ans_col_15, item15);
        contentValues.put(ans_col_16, item16);
        contentValues.put(ans_col_17, item17);

        long result = db.insert(Answer_Table, null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Adding data to the Questions table
    public boolean addFeedback(String item1, String item2){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(feed_col_1, item1);
        contentValues.put(feed_col_2, item2);

        long result = db.insert(feedback_table, null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }



//Delete row for list of question with options and other details [Online Ques Table]
    public void deleteRecord() {

        SQLiteDatabase db = this.getWritableDatabase();

//    Deleting all records from database table
        db.delete(TABLE_NAME, null, null);
        db.execSQL("delete from "+ TABLE_NAME);
    }

    //Delete rows from answer table
    public void delRecfromAnswerTable() {

        SQLiteDatabase db = this.getWritableDatabase();

//    Deleting all records from database table
        db.delete(Answer_Table, null, null);
        db.execSQL("delete from "+ Answer_Table);
    }

    //Delete row for list of question with options and other details [Online Ques Table]
    public void deleteFeedback() {

        SQLiteDatabase db = this.getWritableDatabase();

//    Deleting all records from database table
        db.delete(feedback_table, null, null);
        db.execSQL("delete from "+ feedback_table);
    }

    //For checking whether feedback exists for the same student or not
    public boolean checkFeedbackExists(String feedback_table, String feed_col_1, String item2)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor=db.rawQuery("SELECT "+feed_col_1+" FROM "+feedback_table+" WHERE "+feed_col_1+"='"+item2+"'",null);
            if (cursor.moveToFirst())
            {
                db.close();
                Log.d("Record  Already Exists", "Table is:"+feedback_table+" ColumnName:"+feed_col_1);
                return true;//record Exists

            }
            Log.d("New Record  ", "Table is:"+feedback_table+" ColumnName:"+feed_col_1+" Column Value:"+item2);
            db.close();
        }
        catch(Exception errorException)
        {

        }
        return false;
    }

    //Deleting only single row by its value
    public void deleteSingleAnswerData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Answer_Table + " WHERE " + ans_col_3 + "= '" + item + "'");
        db.close();
    }


    //Get all the values of particular row(Otp table)
    public Cursor getAnswerSingleVal(DBHelper dbHelper,String item1){

        if(dbHelper != null){

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Select all Query
            Cursor data = db.rawQuery("SELECT * FROM " + Answer_Table+" WHERE "+ans_col_3+"='"+item1+"'", null);
            return  data;

        }else{
            return null;
        }
    }

    //Get all the details from the answer table
    public Cursor getAnswerData(DBHelper dbHelper){

        if(dbHelper != null){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Select all Query
            Cursor ansData = db.rawQuery("SELECT * FROM " + Answer_Table, null);
            return  ansData;

        }else {

            return null;
        }
    }


    //Get all the details from Questions Table[Questions Table,Online]
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Select all Query
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return  data;
    }

    public Cursor getSingleContents(String item2){
        SQLiteDatabase db = this.getWritableDatabase();
        //Select all Query
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME+" WHERE "+COLUMN_11+"='"+item2+"'", null);
        return  data;
    }

    public Cursor getfeedbackContent(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Select all Query
        Cursor data = db.rawQuery("SELECT * FROM " + feedback_table, null);
        return  data;
    }

    //For counting the number of rows(questions) which is not visited my the student
    public int notVisitedCount() {
        String countQuery = "SELECT  * FROM StudentData WHERE not_visited='0'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //For counting the number of rows(questions) which is not answered by the student
    public int notAnsweredCount() {
        String countQuery = "SELECT  * FROM StudentData WHERE not_ans='0'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //For counting the number of rows(questions) which is answered by the student
    public int answeredCount() {
        String countQuery = "SELECT  * FROM StudentData WHERE not_ans='1'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //For counting the number of rows(questions) which is marked as review by the student
    public int reviewCount() {
        String countQuery = "SELECT  * FROM StudentData WHERE mf_review='1'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //For checking the value question number exists in table
    public boolean checkIfRecordExist(String TABLE_NAME, String COLUMN_1, String item2)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor=db.rawQuery("SELECT "+COLUMN_1+" FROM "+TABLE_NAME+" WHERE "+COLUMN_1+"='"+item2+"'",null);
            if (cursor.moveToFirst())
            {
                db.close();
                Log.d("Record  Already Exists", "Table is:"+TABLE_NAME+" ColumnName:"+COLUMN_1);
                return true;//record Exists

            }
            Log.d("New Record  ", "Table is:"+TABLE_NAME+" ColumnName:"+COLUMN_1+" Column Value:"+item2);
            db.close();
        }
        catch(Exception errorException)
        {

        }
        return false;
    }

}
