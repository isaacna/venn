package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class CommunitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communities); //set layout
        LinearLayout ln = (LinearLayout) findViewById(R.id.communities);


        List<String> l = new LinkedList<String>();
        l.add("ssbm");
        l.add("workouts");

        int count = 0;
        for (String s : l) {
            final String s2= s; //make string final so it can be put in intent
            Button community  = new Button(this);
            community.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            community.setText(s); //add in name of community to button


            community.setOnClickListener(new View.OnClickListener() { //bind function that sends to match page to button
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommunitiesActivity.this, MatchesActivity.class);
                    intent.putExtra("community", s2); //pass in community to remember for that match page
                    startActivity(intent);
                }
            });



            ln.addView(community); //add the community
        }

    }


}
