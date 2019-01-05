package com.isaacna.projects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    public void viewMain(View view){
//        Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, JoinCommActivity.class);
        String s = "y";

        String first; String last; String bio;

        EditText editText = (EditText) findViewById(R.id.nameText);
        first = editText.getText().toString();

        editText = (EditText) findViewById(R.id.lastText);
        last = editText.getText().toString();

        editText = (EditText) findViewById(R.id.bioText);
        bio = editText.getText().toString();

        intent.putExtra("first", first);
        intent.putExtra("last", last);
        intent.putExtra("bio", bio);
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
