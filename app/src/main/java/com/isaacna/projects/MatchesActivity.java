package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Intent intent = getIntent();

        TextView community = findViewById(R.id.matchCommunity);
        community.setText(intent.getStringExtra("community")); //display name of community the match page is for

        Button match1 = findViewById(R.id.person1);
        Button match2 = findViewById(R.id.person2);

        if(intent.getStringExtra("community").equals("Hockey")) {
            match1.setText("Tyler");
            match2.setText("Rohan");
        }

        else {
            match1.setText("Nathan");
            match2.setText("Isaac");
        }
    }

    public void chatPerson(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        String name = ((Button) view).getText().toString();
        intent.putExtra("matchName", name);
        startActivity(intent);
    }
}
