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
    }
}
