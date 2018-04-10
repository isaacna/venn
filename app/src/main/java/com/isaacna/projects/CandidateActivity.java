package com.isaacna.projects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        Intent intent = getIntent(); //get the passed candidate



        //concatenate string and value
        String val1 = intent.getStringExtra("f1") + ": " + intent.getStringExtra("p1");
        String val2 = intent.getStringExtra("f2") + ": " + intent.getStringExtra("p2");
        String val3 = intent.getStringExtra("f3") + ": " + intent.getStringExtra("p3");

        TextView name = findViewById(R.id.candidateName);
        ImageView pic = findViewById(R.id.candPic);
        TextView p1 = findViewById(R.id.p1);
        TextView p2 = findViewById(R.id.p2);
        TextView p3 = findViewById(R.id.p3);

        name.setText(intent.getStringExtra("candidate_name"));
        p1.setText(val1);
        p2.setText(val2);
        p3.setText(val3);

        //get and set picture
        byte [] picArray = intent.getByteArrayExtra("candidate_pic");
        Bitmap bm = BitmapFactory.decodeByteArray(picArray,0,picArray.length);
        pic.setImageBitmap(bm);
    }
}
