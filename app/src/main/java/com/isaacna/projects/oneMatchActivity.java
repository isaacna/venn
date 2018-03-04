package com.isaacna.projects;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class oneMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(getIntent().hasExtra("name")){
            String m = "Congrats, you matched with " + getIntent().getStringExtra("name") + " to send them a message, type in the box below";
            if(getIntent().hasExtra("community")){
                m = "Congrats, you matched with " + getIntent().getStringExtra("name") +  " in the "
                        + getIntent().getStringExtra("community")+ " community. " + "To send them a message, type in the box below";
            }
            TextView name = findViewById(R.id.matchText); //R.id.x refers to the textview with ID x
            name.setText(m);
        }
        else{
            TextView name = findViewById(R.id.matchText); //R.id.x refers to the textview with ID x
            name.setText("Write a message to your match");
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void sendMessage(View view){
        TextView textView = findViewById(R.id.editMessage);
        String m = textView.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("message", m);
        intent.putExtra("happened", "y");
        startActivity(intent);
    }

}
