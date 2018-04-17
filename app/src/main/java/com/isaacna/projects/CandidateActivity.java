package com.isaacna.projects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        Intent intent = getIntent(); //get the passed candidate

        //concatenate string and value
        TextView name = findViewById(R.id.candidateName);
        ImageView pic = findViewById(R.id.candPic);
        TextView p1 = findViewById(R.id.p11);
        TextView p2 = findViewById(R.id.p22);
        TextView p3 = findViewById(R.id.p33);

        if(!(intent.getStringExtra("f1").equals("null") || (intent.getStringExtra("f1").equals("")))) { //check if empty field
            String val1 = intent.getStringExtra("f1") + ": " + intent.getStringExtra("p1");
            p1.setText(val1);
            p1.setVisibility(View.VISIBLE);
        }
        else{
            p1.setVisibility(View.GONE);
        }
        if(!(intent.getStringExtra("f2").equals("null") || (intent.getStringExtra("f2").equals("")))) { //check if empty field
            String val2 = intent.getStringExtra("f2") + ": " + intent.getStringExtra("p2");
            p2.setText(val2);
            p2.setVisibility(View.VISIBLE);
        }//ouhouhoijs
        else{
            p2.setVisibility(View.GONE);
        }
        if(!(intent.getStringExtra("f3").equals("null") || (intent.getStringExtra("f3").equals("")))) { //check if empty field
            String val3 = intent.getStringExtra("f3") + ": " + intent.getStringExtra("p3");
            p3.setText(val3);
            p3.setVisibility(View.VISIBLE);
        }
        else{
            p3.setVisibility(View.GONE);
        }

        name.setText(intent.getStringExtra("candidate_name"));

        //get and set picture
        byte [] picArray = intent.getByteArrayExtra("candidate_pic");
        Bitmap bm = BitmapFactory.decodeByteArray(picArray,0,picArray.length);
        pic.setImageBitmap(bm);


    }
    public void goBackNo(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra("doUpdate", true);
        intent.putExtra("sayYes", false);
        startActivity(intent);
    }
    public void goBackYes(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra("doUpdate", true);
        intent.putExtra("sayYes", true);
        startActivity(intent);
    }
}
