package com.example.user.assignment3_new;

import android.provider.BaseColumns;

/**
 * Created by user on 9/29/2016.
 */
public class FeedReaderContract {
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Students";
        public static final String COLUMN_NAME_Roll = "rollno";
        public static final String COLUMN_NAME_Name = "name";
    }
}


