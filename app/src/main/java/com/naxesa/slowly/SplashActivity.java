package com.naxesa.slowly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Lee young teak on 2016-09-20.
 */
public class SplashActivity extends Activity {

    // Const
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    // State
    boolean isExist;

    // Database
    private SQLiteDatabase db;
    private LogInSQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.);

        helper = new LogInSQLiteOpenHelper(SplashActivity.this, "login.db", null, 1);

        isExist = checkFile();

        if(isExist){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, SPLASH_DISPLAY_LENGTH);
        } else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private boolean checkFile(){
        boolean isExist;
        db = helper.getReadableDatabase();

        Cursor cursor = db.query("login", null, null, null, null, null, null);

        if(cursor.getCount()==0){
            isExist = false;
        }else{
            isExist = true;
        }

        return isExist;
    }
}
