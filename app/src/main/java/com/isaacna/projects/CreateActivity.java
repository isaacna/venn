package com.isaacna.projects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    public void viewMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        String s = "y";
        intent.putExtra("happened",s);
        startActivity(intent);
    }
//
//    community.setOnClickListener(new View.OnClickListener() { //bind function that sends to match page to button
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(CommunitiesActivity.this, MatchesActivity.class);
//            intent.putExtra("community", s2); //pass in community to remember for that match page
//            startActivity(intent);
//        }
//    });
}
