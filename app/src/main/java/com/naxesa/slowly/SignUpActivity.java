package com.naxesa.slowly;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.*;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    // Const
    public static final int SIGN_UP = 1000;

    // Views
    private EditText inputName;
    private Button btnCreate;
    private ProgressDialog progressDialog;
    private ProgressThread progressThread;

    // Handler
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == ProgressThread.STATE_DONE){
                progressDialog.dismiss();
            }
        }
    };

    // Data
    private String email, password, name;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get Intent from SignInActivity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        // View Reference
        inputName = (EditText)findViewById(R.id.edit_text_name);
        btnCreate = (Button)findViewById(R.id.button_create_user);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");

        // View Event
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(SIGN_UP);
                name = inputName.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    progressThread.setmState(ProgressThread.STATE_DONE);
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                user = auth.getCurrentUser();
                                User userData = new User(name, email);
                                databaseReference.child(user.getUid()).setValue(userData);
                                progressThread.setmState(ProgressThread.STATE_DONE);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressThread.setmState(ProgressThread.STATE_DONE);
                        finish();
                    }
                });
            }
        });
    }

    // Data
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case SIGN_UP:
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("SignUp");
                progressDialog.show();
                progressThread = new ProgressThread(mHandler);
                progressThread.start();
                return progressDialog;
            default:
                return null;
        }
    }
}
