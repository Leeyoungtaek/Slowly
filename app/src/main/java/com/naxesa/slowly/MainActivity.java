package com.naxesa.slowly;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.*;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Const
    public static final int LOADING = 1000;

    // Count
    private int eventCount = 0;

    // Views
    private TextView name;
    private TextView btnAdd, btnBook;
    private TextView famousSaying, famousSayingPerson;
    private Button btnLogOut;
    private ProgressDialog progressDialog;
    private ProgressThread progressThread;

    // SQLite
    private SQLiteDatabase db;
    private LogInSQLiteOpenHelper helper;

    // Handler
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == ProgressThread.STATE_DONE){
                progressDialog.dismiss();
            }
        }
    };

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Reference
        name = (TextView)findViewById(R.id.name);
        btnAdd = (TextView)findViewById(R.id.add);
        btnBook = (TextView)findViewById(R.id.book);
        famousSaying = (TextView)findViewById(R.id.famous_saying);
        famousSayingPerson = (TextView)findViewById(R.id.famous_saying_person);
        btnLogOut = (Button)findViewById(R.id.log_out);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Random random = new Random();
        int index = random.nextInt(8) + 1;
        databaseReference = firebaseDatabase.getReference("famousSaying/" + index);

        // SQLite
        helper = new LogInSQLiteOpenHelper(getApplicationContext(), "login.db", null, 1);

        // Data Loading
        showDialog(LOADING);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String person = dataSnapshot.child("person").getValue().toString();
                String content = dataSnapshot.child("content").getValue().toString();
                famousSaying.setText(content);
                famousSayingPerson.setText("(" + person + ")");
                if(++eventCount==2){
                    progressThread.setmState(ProgressThread.STATE_DONE);
                    eventCount=0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // View Setting
        databaseReference = firebaseDatabase.getReference("user/" + user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = (User) dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                if(++eventCount==2){
                    progressThread.setmState(ProgressThread.STATE_DONE);
                    eventCount=0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // View Event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                startActivity(intent);
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Data
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case LOADING:
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();
                progressThread = new ProgressThread(mHandler);
                progressThread.start();
                return progressDialog;
            default:
                return null;
        }
    }

    private void logIn(){
        db = helper.getWritableDatabase();
        db.execSQL("delete from " + "login  ");
    }
}
