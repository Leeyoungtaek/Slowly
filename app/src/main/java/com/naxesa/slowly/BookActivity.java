package com.naxesa.slowly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class BookActivity extends AppCompatActivity {

    // Views
    private ImageButton btnDelete;
    private ImageView image;

    // ViewPager
    private ViewPager viewPager;
    private CustomPagerAdapter mAdapter;

    // Data
    private ArrayList<Message> messages;
    private Bitmap src, resize;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // View Reference
        btnDelete = (ImageButton) findViewById(R.id.button_delete);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        image = (ImageView) findViewById(R.id.image_view);

        // View Setting
        Random random = new Random();
        switch (random.nextInt(4)){
            case 0:
                drawBigImage(image, R.drawable.background1);
                break;
            case 1:
                drawBigImage(image, R.drawable.background2);
                break;
            case 2:
                drawBigImage(image, R.drawable.background3);
                break;
            case 3:
                drawBigImage(image, R.drawable.background4);
                break;
            default:
                break;
        }
        image.setScaleType(ImageView.ScaleType.FIT_XY);

        // New Data
        messages = new ArrayList<Message>();

        // Firebase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("message/" + user.getUid());

        // View Event
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position =  viewPager.getCurrentItem();
                if(messages.isEmpty()){
                    return;
                }
                Message message = messages.get(position);
                databaseReference.child(message.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_SHORT).show();
                    }
                });
                messages.remove(position);
                mAdapter = new CustomPagerAdapter(getLayoutInflater(), messages);
                viewPager.setAdapter(mAdapter);
            }
        });

        // Firebase Event
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                mAdapter = new CustomPagerAdapter(getLayoutInflater(), messages);
                viewPager.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void drawBigImage(ImageView imageView, int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
        options.inPurgeable = true;
        src = BitmapFactory.decodeResource(getResources(), resId, options);
        resize = Bitmap.createScaledBitmap(src, options.outWidth, options.outHeight, true);
        imageView.setImageBitmap(resize);
    }

    @Override
    protected void onDestroy() {
        src.recycle();
        src = null;
        resize.recycle();
        resize = null;
        super.onDestroy();
    }
}
