package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CandidateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        Intent intent = getIntent(); //get the passed candidate

        TextView name = findViewById(R.id.candidateName);

        name.setText(intent.getStringExtra("candidate"));
    }
}
