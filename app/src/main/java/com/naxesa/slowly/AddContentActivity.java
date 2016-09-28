package com.naxesa.slowly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContentActivity extends AppCompatActivity {

    // Views
    private EditText inputContent;
    private Button btnPrev, btnNext;

    // Data
    Message message;

    // Request Code
    public static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        // Get Intent From PlaceActivity
        Intent getIntent = getIntent();
        message = (Message) getIntent.getSerializableExtra("Message");

        // View Reference
        inputContent = (EditText)findViewById(R.id.edit_text_content);
        btnPrev = (Button)findViewById(R.id.button_prev_in_content);
        btnNext = (Button)findViewById(R.id.button_next_in_content);

        // View Event
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputContent.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(getApplicationContext(), "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                message.setContent(content);
                Intent intent = new Intent(getApplicationContext(), AddPersonActivity.class);
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
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
