package com.naxesa.slowly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlaceActivity extends AppCompatActivity {

    // Views
    private EditText inputPlace;
    private Button btnCancel, btnNext;

    // Data
    Message message;

    // Request Code
    public static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        // New Data
        message = new Message();

        // View Reference
        inputPlace = (EditText)findViewById(R.id.edit_text_place);
        btnCancel = (Button)findViewById(R.id.button_cancel_in_place);
        btnNext = (Button)findViewById(R.id.button_next_in_place);

        // View Event
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = inputPlace.getText().toString().trim();
                if(TextUtils.isEmpty(place)){
                    Toast.makeText(getApplicationContext(), "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                message.setPlace(place);
                Intent intent = new Intent(getApplicationContext(), AddContentActivity.class);
                intent.putExtra("Message", message);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    // Close at once
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                finish();
            }
        }
    }
}