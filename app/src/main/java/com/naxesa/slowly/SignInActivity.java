package com.naxesa.slowly;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    // Const
    final static int SIGN_IN = 1000;
    final static int FORGOT_PASSWORD = 1001;

    // Views
    private TextView title;
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignup, btnForgotPassword;

    // Progress
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

    private String email, password;

    // Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // View Reference
        title = (TextView)findViewById(R.id.title);
        inputEmail = (EditText)findViewById(R.id.edit_text_email);
        inputPassword = (EditText)findViewById(R.id.edit_text_password);
        btnSignIn = (Button)findViewById(R.id.button_sign_in);
        btnSignup = (Button)findViewById(R.id.button_sign_up);
        btnForgotPassword = (Button)findViewById(R.id.button_forgot_password);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();

        // View Setting
        Typeface tf = Typeface.createFromAsset(getAssets(), "beyond_the_mountains.ttf");
        title.setTypeface(tf);

        // View Event
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(SIGN_IN);
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    progressThread.setmState(ProgressThread.STATE_DONE);
                    return;
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    progressThread.setmState(ProgressThread.STATE_DONE);
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressThread.setmState(ProgressThread.STATE_DONE);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    progressThread.setmState(ProgressThread.STATE_DONE);
                                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(FORGOT_PASSWORD);
                email = inputEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "메일을 받을 이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    progressThread.setmState(ProgressThread.STATE_DONE);
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(SignInActivity.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressThread.setmState(ProgressThread.STATE_DONE);
                                Toast.makeText(getApplicationContext(), "이메일을 확인하세요", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // Data
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case SIGN_IN:
                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("SignIn");
                progressDialog.show();
                progressThread = new ProgressThread(mHandler);
                progressThread.start();
                return progressDialog;
            case FORGOT_PASSWORD:
                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Sending");
                progressDialog.show();
                progressThread = new ProgressThread(mHandler);
                progressThread.start();
                return progressDialog;
            default:
                return null;
        }
    }
}
