package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Intent intent = getIntent();

        TextView community = findViewById(R.id.matchCommunity);
        community.setText(intent.getStringExtra("community")); //display name of community the match page is for

        TextView match1 = findViewById(R.id.person1);
        TextView match2 = findViewById(R.id.person2);

        if(community.equals("Hockey")) {
            match1.setText("Tyler");
            match2.setText("Rohan");
        }

        else {
            match1.setText("Nathan");
            match2.setText("Isaac");
        }
    }
}
