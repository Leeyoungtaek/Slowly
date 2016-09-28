package com.naxesa.slowly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Lee young teak on 2016-09-20.
 */
public class SplashActivity extends Activity {

    // Const
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    // State
    private boolean isExist;

    // Data
    private String email, password;

    // View
    private TextView title;
    private Typeface tf;

    // Database
    private SQLiteDatabase db;
    private LogInSQLiteOpenHelper helper;

    // Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        title = (TextView)findViewById(R.id.title);
        tf = Typeface.createFromAsset(getAssets(), "beyond_the_mountains.ttf");
        title.setTypeface(tf);

        helper = new LogInSQLiteOpenHelper(SplashActivity.this, "login.db", null, 1);
        auth = FirebaseAuth.getInstance();

        isExist = checkFile();

        if(isExist){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingData();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }, SPLASH_DISPLAY_LENGTH*2);
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

    private void loadingData(){
        db = helper.getReadableDatabase();

        Cursor cursor = db.query("login", null, null, null, null, null, null);

        cursor.moveToNext();
        email = cursor.getString(cursor.getColumnIndex("email"));
        password = cursor.getString(cursor.getColumnIndex("password"));
    }
}
