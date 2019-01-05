package com.isaacna.projects;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TextView t = findViewById(R.id.chatPerson);
        t.setText(getIntent().getStringExtra("matchName"));
    }
}
