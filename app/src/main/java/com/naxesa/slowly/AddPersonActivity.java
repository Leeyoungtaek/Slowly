package com.naxesa.slowly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPersonActivity extends AppCompatActivity {

    // Views
    private EditText inputPerson;
    private Button btnPrev, btnAdd;

    // Data
    Message message;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        // Get Intent from ContentActivity
        Intent getIntent = getIntent();
        message = (Message) getIntent.getSerializableExtra("Message");

        // View Reference
        inputPerson = (EditText)findViewById(R.id.edit_text_person);
        btnPrev = (Button)findViewById(R.id.button_prev_in_person);
        btnAdd = (Button)findViewById(R.id.button_add_in_person);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("message/" + user.getUid());

        // View Event
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String person = inputPerson.getText().toString().trim();
                if(TextUtils.isEmpty(person)){
                    Toast.makeText(getApplicationContext(), "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                message.setPerson(person);
                message.setUid(databaseReference.push().getKey());
                databaseReference.child(message.getUid()).setValue(message);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
