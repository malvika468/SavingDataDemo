package com.example.user.assignment3_new;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    String filename = "myfile";
    String file1 = "external.txt";

    File file;
    FeedReaderDbHelper mDbHelper;
    SQLiteDatabase db;
    EditText ed2;
    EditText ed1;
    TextView v;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
       /*When rotation occurs
         */
            String state1 = savedInstanceState.getString("myString");
            String state2 = savedInstanceState.getString("myString1");
            String state3 = savedInstanceState.getString("myString2");

            ed1 = (EditText) findViewById(R.id.editText);
            ed1.setText(state1);
            ed2 = (EditText) findViewById(R.id.editText2);
            ed2.setText(state2);
            v=(TextView)findViewById(R.id.textView2);
            v.setText(state3);

        } else {
            ed2 = (EditText) findViewById(R.id.editText2);
            ed2.setText(null);
            v=(TextView)findViewById(R.id.textView2);
            v.setText(null);
            /*
            mDbHelper = new FeedReaderDbHelper(this.getApplicationContext());
            db = mDbHelper.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            ContentValues values2 = new ContentValues();
            values1.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Roll, "mt16045");
            values1.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Name, "malvika");
            values2.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Roll, "mt16044");
            values2.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Name, "malo");

         // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values1);
            long newRowId1 = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values2);
*/
        }


        }


    // save sqlite data
    public void save(View v) {

        mDbHelper = new FeedReaderDbHelper(this.getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);
        values1.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Roll, ed2.getText().toString());
        values1.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Name, ed1.getText().toString());
       // long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values1);
        // replacing duplicate values
         long row=db.insertWithOnConflict(FeedReaderContract.FeedEntry.TABLE_NAME, null, values1,SQLiteDatabase.CONFLICT_REPLACE);
    }


// shows sqlite data
    public void show(View v)
    {
        mDbHelper = new FeedReaderDbHelper(this.getApplicationContext());
        db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_Roll,
                FeedReaderContract.FeedEntry.COLUMN_NAME_Name
        };
       String [] p=new String[]{FeedReaderContract.FeedEntry.COLUMN_NAME_Name};

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_Roll + " = ?";
        String g=ed2.getText().toString();
        String[] selectionArgs = {g};

        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_Name + " DESC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        TextView tv1 = (TextView) findViewById(R.id.textView2);
        String  n="";
        if(c != null && c.moveToFirst()){
            String n1 = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_Name));
            tv1.setText(n1);
            c.close();
        }
        else
        {
            Toast.makeText(getBaseContext(),"record does  not exist",Toast.LENGTH_SHORT).show();
            TextView vv=(TextView)findViewById(R.id.textView2);
            vv.setText(null);
        }

//   To retrieve one whole column data into textView
/*
        TextView tv = (TextView) findViewById(R.id.textView2);
        Cursor csr=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, null, null,null,null,null,null);
        csr.moveToFirst();
        while(!csr.isAfterLast())
        {
            csr.moveToNext();
        }

        Cursor csrs=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, null, null,null,null,null,null);
        csrs.moveToFirst();
        while(!csrs.isAfterLast())
        {
            n=n.concat(" "+csrs.getString(2));
            tv.setText(n);
            csrs.moveToNext();
        }
*/

    }


// save a data as record then press delete button
    public void delete(View v)
    {
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_Roll + " LIKE ?";
        String d=ed2.getText().toString();
        String[] selectionArgs = {d};
       // Issue SQL statement.
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

    }

    // save a record then update and show to see new value
    public void update(View v)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        String v1=ed1.getText().toString();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Name, v1);

// Which row to update, based on the title
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_Roll + " LIKE ?";
        String u=ed2.getText().toString();
        String[] selectionArgs = {u};

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);


    }


    // Internal file write read

    public void internalWrite(View v)
    {

       Context context=getApplicationContext();
        File file = new File(context.getFilesDir(), "myfile");
        EditText ed=(EditText)findViewById(R.id.editText);
        String str=ed.getText().toString();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(str.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void internalRead(View v)
    {

        try{
            FileInputStream fin = openFileInput(filename);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            TextView tv=(TextView)findViewById(R.id.textView2);
            tv.setText(temp);
            Toast.makeText(getBaseContext(),"file read",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    // external file write read
    public void externalWrite(View v)
    {
        FileOutputStream outputStream;
        EditText ed=(EditText)findViewById(R.id.editText);
        String str1=ed.getText().toString();

        if(isExternalStorageWritable()) {
            //file = new File(Environment.getExternalStoragePublicDirectory(
                    //Environment.DIRECTORY_DOCUMENTS),file1);
            File file=Environment.getExternalStorageDirectory();
            try{
                outputStream = openFileOutput(file1, Context.MODE_PRIVATE);
                outputStream.write(str1.getBytes());
                outputStream.close();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void extPublic(View v)
    {
        FileOutputStream outputStream;
        EditText ed=(EditText)findViewById(R.id.editText);
        String str1=ed.getText().toString();

        if(isExternalStorageWritable()) {
            file = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS),file1);
           // File file=Environment.getExternalStoragePublicDirectory();
            try{
                outputStream = openFileOutput(file1, Context.MODE_PRIVATE);
                outputStream.write(str1.getBytes());
                outputStream.close();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void externalRead(View v)
    {
        try{
            FileInputStream fin = openFileInput(file1);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            TextView tv=(TextView)findViewById(R.id.textView2);
            tv.setText(temp);
            Toast.makeText(getBaseContext(),"file read",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.getStackTrace();
        }

    }



    // save data in shared preferences and view it
  public void sharedSave(View v)
  {
      sharedPref = this.getPreferences(Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      EditText ed = (EditText) findViewById(R.id.editText);
      String n = ed.getText().toString();
      editor.putString("key1", n);
      editor.commit();
      SharedPreferences  sharedPref1 = this.getPreferences(Context.MODE_PRIVATE);
      String name = sharedPref1.getString("key1", null);
      TextView tv = (TextView) findViewById(R.id.textView2);
      tv.setText(name);

  }

    // save value in shared preferences when app is stopped

    @Override
    public void onStop()
    {
        super.onStop();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        EditText ed = (EditText) findViewById(R.id.editText);
        String n = ed.getText().toString();
        editor.putString("key1", n);
        editor.commit();
    }

    // save value in shared preferences when app is paused

    public void onPause() {
        super.onPause();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        EditText ed = (EditText) findViewById(R.id.editText);
        String n = ed.getText().toString();
        editor.putString("key1", n);
        editor.commit();

    }


    // save value in shared preferences when app is resumed

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences  sharedPref1 = this.getPreferences(Context.MODE_PRIVATE);
        String name = sharedPref1.getString("key1", null);
        EditText e=(EditText)findViewById(R.id.editText);
        e.setText(name);

    }


    // for checking external availability
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

//for rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText ed1=(EditText)findViewById(R.id.editText);
        EditText ed2=(EditText)findViewById(R.id.editText2);
        TextView t=(TextView)findViewById(R.id.textView2);
        savedInstanceState.putCharSequence("myString",ed1.getText().toString());
        savedInstanceState.putCharSequence("myString1",ed2.getText().toString());
        savedInstanceState.putCharSequence("myString2",t.getText().toString());


    }

}

